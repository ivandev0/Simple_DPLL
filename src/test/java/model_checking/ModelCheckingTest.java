package model_checking;

import cnf.CNF;
import cnf.Disjunction;
import org.junit.Assert;
import org.junit.Test;
import reader.FiniteStateMachineReader;
import util.IDPool;

import java.util.Arrays;

public class ModelCheckingTest {

    @Test
    public void finiteRunSimpleTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2\n" +
                "!v1\n" +
                "(v1' <-> (!v1 v v2)) ^ (v2' <-> (v1 ^ v2))\n" +
                "v1 ^ v2"
        );

        Assert.assertEquals(false, ModelChecking.finiteRun(fsm, 1));
    }

    @Test
    public void infiniteRunSimpleTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "v1 v2\n" +
                "!v1\n" +
                "(v1' <-> (!v1 v v2)) ^ (v2' <-> (v1 ^ v2))\n" +
                "v1 ^ v2"
        );

        Assert.assertFalse(ModelChecking.infiniteRun(fsm));
    }

    @Test
    public void finiteSimpleUnsafeRunTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "x y z\n" +
                "!x v !y v !z\n" +
                "(x' <-> y) ^ (y' <-> z) ^ z'\n" +
                "x ^ y ^ z"
        );

        Assert.assertEquals(true, ModelChecking.finiteRun(fsm, 1));
    }

    @Test
    public void infiniteSimpleUnsafeRunTest() {
        FiniteStateMachine fsm = FiniteStateMachineReader.readFromString(
                "x y z\n" +
                        "!x v !y v !z\n" +
                        "(x' <-> y) ^ (y' <-> z) ^ z'\n" +
                        "x ^ y ^ z"
        );

        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
    }

    @Test
    public void finiteSimpleUnsafeTestFromCnf() {
        String[] vars = new String[] {"x", "y", "z"};
        IDPool pool = new IDPool();
        Arrays.stream(vars).forEach(pool::idByName);
        Arrays.stream(vars).forEach(it -> pool.idByName(it + "'"));

        //"(x' <-> y) ^ (y' <-> z) ^ z'"
        CNF transition = new CNF(
                new Disjunction(4, -2), new Disjunction(2, -4),
                new Disjunction(5, -3), new Disjunction(-5, 3),
                new Disjunction(6)
        );
        CNF init = new CNF(new Disjunction(-1, -2, -3));
        CNF err = new CNF(new Disjunction(1), new Disjunction(2), new Disjunction(3));
        FiniteStateMachine fsm = new FiniteStateMachine(vars, init, transition, err, pool);

        Assert.assertNull(ModelChecking.finiteRun(fsm, 0));
        Assert.assertEquals(true, ModelChecking.finiteRun(fsm, 1));
        Assert.assertEquals(true, ModelChecking.finiteRun(fsm, 2));
    }

    @Test
    public void infiniteSimpleUnsafeTestFromCnf() {
        String[] vars = new String[] {"x", "y", "z"};
        IDPool pool = new IDPool();
        Arrays.stream(vars).forEach(pool::idByName);
        Arrays.stream(vars).forEach(it -> pool.idByName(it + "'"));

        //"(x' <-> y) ^ (y' <-> z) ^ z'"
        CNF transition = new CNF(
                new Disjunction(4, -2), new Disjunction(2, -4),
                new Disjunction(5, -3), new Disjunction(-5, 3),
                new Disjunction(6)
        );
        CNF init = new CNF(new Disjunction(-1, -2, -3));
        CNF err = new CNF(new Disjunction(1), new Disjunction(2), new Disjunction(3));
        FiniteStateMachine fsm = new FiniteStateMachine(vars, init, transition, err, pool);

        Assert.assertTrue(ModelChecking.infiniteRun(fsm));
    }
}