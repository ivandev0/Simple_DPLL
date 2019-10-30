package model_checking;

import cnf.CNF;
import cnf.Disjunction;
import dpll.DPLL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reader.FiniteStateMachineReader;
import util.IDPool;

import java.util.Arrays;

public class ModelCheckingTest {
    @Before
    public void setUp() {
        ModelChecking.SKIP_CHECKS = false;
    }

    @Test
    public void simpleTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2\n" +
                "!v1\n" +
                "(v1' <-> (!v1 v v2)) ^ (v2' <-> (v1 ^ v2))\n" +
                "v1 ^ v2"
        );

        Assert.assertEquals(false, ModelChecking.finiteRun(fsm, 1));
        Assert.assertFalse(ModelChecking.infiniteRun(fsm));
    }

    @Test
    public void simpleUnsafeRunTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "x y z\n" +
                "!x v !y v !z\n" +
                "(x' <-> y) ^ (y' <-> z) ^ z'\n" +
                "x ^ y ^ z"
        );

        Assert.assertEquals(true, ModelChecking.finiteRun(fsm, 1));
        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
    }

    @Test
    public void simpleUnsafeTestFromCnf() {
        String[] vars = new String[] {"x", "y", "z"};
        IDPool pool = new IDPool();
        Arrays.stream(vars).forEach(pool::idByName);
        Arrays.stream(vars).forEach(it -> pool.idByName(it + "'"));

        String transition = "(x' <-> y) ^ (y' <-> z) ^ z'";
        CNF init = new CNF(new Disjunction(-1, -2, -3));
        CNF err = new CNF(new Disjunction(1), new Disjunction(2), new Disjunction(3));
        FiniteStateMachine fsm = new FiniteStateMachine(vars, init, transition, err, pool);

        Assert.assertEquals(true, ModelChecking.finiteRun(fsm, 1));
        Assert.assertEquals(true, ModelChecking.finiteRun(fsm, 2));

        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
    }

    @Test
    // -> 00 -> 01 -> 10 -> 00;
    // 11 -> 11;
    public void straightLineUnsafeTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2\n" +
                "!v1 ^ !v2\n" +
                "(v1' <-> v2) ^ (v2' <-> (v1 <-> v2))\n" +
                "v1 ^ !v2"
        );

        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(0)));
        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(1)));
        Assert.assertNotNull(DPLL.solve(fsm.getBoundedModel(2)));
        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
    }

    @Test
    // -> 00 -> 01 -> 10 -> 00;
    // 11 -> 11;
    public void straightLineSafeTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2\n" +
                        "!v1 ^ !v2\n" +
                        "(v1' <-> v2) ^ (v2' <-> ((!v1 ^ !v2) v (v1 ^ v2)))\n" +
                        "v1 ^ v2"
        );

        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(0)));
        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(1)));
        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(2)));
        Assert.assertFalse(ModelChecking.infiniteRun(fsm));
    }

    @Test
    // -> 00 -> 01 -> 10 -> 11 -> 11;
    public void straightLineUnsafeTest2() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2\n" +
                        "!v1 ^ !v2\n" +
                        "(v1' <-> (v1 v v2)) ^ (v2' <-> (v1 v !v2))\n" +
                        "v1 ^ v2"
        );

        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(0)));
        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(1)));
        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(2)));
        Assert.assertNotNull(DPLL.solve(fsm.getBoundedModel(3)));
        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
    }

    @Test
    /*
           |-------|
           v       |
       -> 000 --> 010
           | -|    |
           |  ---| |
           v     - v
          001 --> 011      111 -> 111;
     */
    public void recursiveSafeTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2 v3\n" +
                        "!v1 ^ !v2 ^ !v3\n" +
                        "( (!v1 ^ !v2 ^ !v3) -> ((!v1' ^ !v2' ^ v3') v (!v1' ^ v2' ^ v3') v (!v1' ^ v2' ^ !v3')) ) " +
                        "v ((!v1 ^ !v2 ^ v3) -> (!v1' ^ v2' ^ v3)) " +
                        "v ((!v1 ^ v2 ^ !v3) -> (!v1' ^ v2' ^ v3')) " +
                        "v ((!v1 ^ v2 ^ v3) -> (!v1' ^ !v2' ^ !v3')) " +
                        "v ((v1 ^ v2 ^ v3) -> (v1' ^ v2' ^ v3'))\n" +
                        "v1 ^ v2 ^ v3"
        );

        Assert.assertFalse(ModelChecking.infiniteRun(fsm));
    }

    @Test
    /*
           |-------|
           v       |
       -> 000 --> 010
           | -|    |
           |  ---| |
           v     - v
          001 --> 011 ---> 111 -> 111;
     */
    public void recursiveUnsafeTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2 v3\n" +
                        "!v1 ^ !v2 ^ !v3\n" +
                        "( (!v1 ^ !v2 ^ !v3) -> ((!v1' ^ !v2' ^ v3') v (!v1' ^ v2' ^ v3') v (!v1' ^ v2' ^ !v3')) ) " +
                        "v ((!v1 ^ !v2 ^ v3) -> (!v1' ^ v2' ^ v3)) " +
                        "v ((!v1 ^ v2 ^ !v3) -> (!v1' ^ v2' ^ v3')) " +
                        "v ((!v1 ^ v2 ^ v3) -> (v1' ^ v2' ^ v3')) " + //unsafe
                        "v ((v1 ^ v2 ^ v3) -> (v1' ^ v2' ^ v3'))\n" +
                        "v1 ^ v2 ^ v3"
        );

        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
    }

    @Test
    // -> 000 -> 001 -> 010 -> 011 -> 100 -> 101 -> 110 -> 000;
    // 111 -> 111;
    public void longStraightLineSafeTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2 v3\n" +
                        "!v1 ^ !v2 ^ !v3\n" +
                        "(v1' <-> ( (!v1 ^ v2 ^ v3) v (v1 ^ !v2 ^ !v3) v (v1 ^ !v2 ^ v3) v (v1 ^ v2 ^ v3) )) " +
                        "^ (v2' <-> ( (!v1 ^ !v2 ^ v3) v (!v1 ^ v2 ^ !v3) v (v1 ^ !v2 ^ v3) v (v1 ^ v2 ^ v3) )) " +
                        "^ (v3' <-> ( (!v1 ^ !v2 ^ !v3) v (!v1 ^ v2 ^ !v3) v (v1 ^ !v2 ^ !v3) v (v1 ^ v2 ^ v3) ))\n" +
                        "v1 ^ v2 ^ v3"
        );

        Assert.assertFalse(ModelChecking.infiniteRun(fsm));
    }

    @Test
    // -> 000 -> 001 -> 010 -> 011 -> 100 -> 101 -> 110 -> 111 -> 111;
    public void longStraightLineUnsafeTest() {
        System.out.println("This test is long, please wait.");
        ModelChecking.SKIP_CHECKS = true;
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2 v3\n" +
                        "!v1 ^ !v2 ^ !v3\n" +
                        "(v1' <-> ( (!v1 ^ v2 ^ v3) v (v1 ^ !v2 ^ !v3) v (v1 ^ !v2 ^ v3) v (v1 ^ v2 ^ !v3) v (v1 ^ v2 ^ v3) )) " +
                        "^ (v2' <-> ( (!v1 ^ !v2 ^ v3) v (!v1 ^ v2 ^ !v3) v (v1 ^ !v2 ^ v3) v (v1 ^ v2 ^ !v3) v (v1 ^ v2 ^ v3) )) " +
                        "^ (v3' <-> ( (!v1 ^ !v2 ^ !v3) v (!v1 ^ v2 ^ !v3) v (v1 ^ !v2 ^ !v3) v (v1 ^ v2 ^ !v3) v (v1 ^ v2 ^ v3) ))\n" +
                        //another way to encode this graph transition
                        /*"( (!v1 ^ !v2 ^ !v3) -> (!v1' ^ !v2' ^ v3') ) " +
                        "v ( (!v1 ^ !v2 ^ v3) -> (!v1' ^ v2' ^ !v3') ) " +
                        "v ( (!v1 ^ v2 ^ !v3) -> (!v1' ^ v2' ^ v3') ) " +
                        "v ( (!v1 ^ v2 ^ v3) -> (v1' ^ !v2' ^ !v3') ) " +
                        "v ( (v1 ^ !v2 ^ !v3) -> (v1' ^ !v2' ^ v3') ) " +
                        "v ( (v1 ^ !v2 ^ v3) -> (v1' ^ v2' ^ !v3') ) " +
                        "v ( (v1 ^ v2 ^ !v3) -> (v1' ^ v2' ^ v3') ) " +
                        "v ( (v1 ^ v2 ^ v3) -> (v1' ^ v2' ^ v3) )\n" +*/
                        "v1 ^ v2 ^ v3"
        );

        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(0)));
        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(1)));
        Assert.assertNull(DPLL.solve(fsm.getBoundedModel(6)));
        //Assert.assertNull(DPLL.solve(fsm.getBoundedModel(7)));

        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
        ModelChecking.SKIP_CHECKS = false;
    }
}