import cnf.CNF;
import org.junit.Assert;
import org.junit.Test;
import reader.DimacsReader;

import java.util.Set;
import java.util.TreeSet;

public class DPLLTest {
    @Test
    public void taskFromClass() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 8\n" +
                "-1 2 3 0\n" +
                "1 3 4 0\n" +
                "1 3 -4 0\n" +
                "1 -3 4 0\n" +
                "1 -3 -4 0\n" +
                "-2 -3 4 0\n" +
                "-1 2 -3 0\n" +
                "-1 -2 3 0"
        );

        Set<Integer> set = new TreeSet<Integer>() {{ add(1); add(2); add(3); add(4); }};
        Assert.assertEquals(new Model(set), DPLL.solve(cnf, new Model()));
    }

    @Test
    public void test1() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 2\n" +
                "1 2 0\n" +
                "-1 3 0"
        );

        Set<Integer> set = new TreeSet<Integer>() {{ add(2); add(3); }};
        Assert.assertEquals(new Model(set), DPLL.solve(cnf, new Model()));
    }

    @Test
    public void test2() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 2\n" +
                "1 -2 0\n" +
                "-1 3 0"
        );

        Set<Integer> set = new TreeSet<Integer>() {{ add(-2); add(3); }};
        Assert.assertEquals(new Model(set), DPLL.solve(cnf, new Model()));
    }
}