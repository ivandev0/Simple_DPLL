import cnf.CNF;
import org.junit.Assert;
import org.junit.Test;
import reader.DimacsReader;

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

        Assert.assertEquals(new Model(1, 2, 3, 4), DPLL.solve(cnf));
    }

    @Test
    public void test1() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 2\n" +
                "1 2 0\n" +
                "-1 3 0"
        );

        Assert.assertEquals(new Model(2, 3), DPLL.solve(cnf));
    }

    @Test
    public void test2() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 2\n" +
                "1 -2 0\n" +
                "-1 3 0"
        );

        Assert.assertEquals(new Model(-2, 3), DPLL.solve(cnf));
    }
}