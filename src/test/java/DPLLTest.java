import cnf.CNF;
import org.junit.Assert;
import org.junit.Test;
import reader.DimacsReader;
import resolution.Resolution;

import java.util.HashSet;

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

    @Test
    public void unsat() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 2\n" +
                "1 0\n" +
                "-1 0"
        );

        Assert.assertNull(DPLL.solve(cnf));
    }

    @Test
    public void resolutionUnsat() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 2\n" +
                        "-1 3 0\n" +
                        "1 3 0\n" +
                        "-2 -3 0\n" +
                        "2 -3 0"
        );

        Resolution res = new Resolution(0);
        Assert.assertNull(DPLL.solveWithResolution(cnf, res));
        Assert.assertEquals(0, res.entry.size());
    }

    @Test
    public void resolutionUnsat2() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 8\n" +
                        "-1 2 3 0\n" +
                        "1 3 4 0\n" +
                        "1 3 -4 0\n" +
                        "1 -3 4 0\n" +
                        "1 -3 -4 0\n" +
                        "-2 -3 4 0\n" +
                        "-1 2 -3 0\n" +
                        "-1 -2 3 0\n" +
                        "-1 -2 -3 0"
        );

        Resolution res = new Resolution(0);
        Assert.assertNull(DPLL.solveWithResolution(cnf, res));
        Assert.assertEquals(0, res.entry.size());
    }

    @Test
    public void resolutionUnsat3() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 7\n" +
                        "-3 4 0\n" +
                        "-5 -4 0\n" +
                        "-6 3 0\n" +
                        "4 -5 6 0\n" +
                        "1 2 0\n" +
                        "-2 5 0\n" +
                        "-1 5 0"
        );

        Resolution res = new Resolution(0);
        Assert.assertNull(DPLL.solveWithResolution(cnf, res));
        Assert.assertEquals(0, res.entry.size());
    }

    @Test
    public void resolutionSat() {
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

        Resolution res = new Resolution(0);
        Assert.assertEquals(new Model(1, 2, 3, 4), DPLL.solveWithResolution(cnf, res));
        Assert.assertEquals(new HashSet<Integer>() {{ add(2); add(3); }}, res.entry);
    }
}