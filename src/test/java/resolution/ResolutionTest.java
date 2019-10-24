package resolution;

import cnf.CNF;
import dpll.DPLL;
import org.junit.Assert;
import org.junit.Test;
import reader.DimacsReader;
import util.CombineUtils;

import java.util.Set;

public class ResolutionTest {
    @Test
    public void resolutionUnsat() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 2\n" +
                        "-1 3 0\n" +
                        "1 3 0\n" +
                        "-2 -3 0\n" +
                        "2 -3 0"
        );

        Resolution res = DPLL.solveWithResolution(cnf);
        Assert.assertNotNull(res);
        Assert.assertTrue(checkResolutionTree(res));
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

        Resolution res = DPLL.solveWithResolution(cnf);
        Assert.assertNotNull(res);
        Assert.assertTrue(checkResolutionTree(res));
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

        Resolution res = DPLL.solveWithResolution(cnf);
        Assert.assertNotNull(res);
        Assert.assertTrue(checkResolutionTree(res));
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

        Assert.assertNull(DPLL.solveWithResolution(cnf));
    }

    @Test
    public void resolutionWithInstantUnit() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 6\n" +
                        "1 -2 0\n" +
                        "-1 -3 0\n" +
                        "2 0\n" +
                        "-2 3 0\n" +
                        "2 4 0\n" +
                        "-4 0"
        );

        Resolution res = DPLL.solveWithResolution(cnf);
        Assert.assertNotNull(res);
        Assert.assertTrue(checkResolutionTree(res));
    }

    @Test
    public void resolutionWithInstantUnit2() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 5\n" +
                        "-1 -2 -3 4 0\n" +
                        "1 0\n" +
                        "2 0\n" +
                        "3 0\n" +
                        "-1 -2 -3 -4 0"
        );

        Resolution res = DPLL.solveWithResolution(cnf);
        Assert.assertNotNull(res);
        Assert.assertTrue(checkResolutionTree(res));
    }

    @Test
    public void resolutionWithInstantUnit3() {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 6\n" +
                        "-1 -2 -3 4 0\n" +
                        "1 0\n" +
                        "2 0\n" +
                        "3 0\n" +
                        "-5 -4 0\n" +
                        "5 0"
        );

        Resolution res = DPLL.solveWithResolution(cnf);
        Assert.assertNotNull(res);
        Assert.assertTrue(checkResolutionTree(res));
    }

    private boolean checkResolutionTree(Resolution res) {
        if (res.getRightParent() == null && res.getLeftParent() == null) {
            return true;
        }

        boolean left = checkResolutionTree(res.getLeftParent());
        boolean right = checkResolutionTree(res.getRightParent());

        Integer complementaryLiteral = CombineUtils.getComplementaryLiteral(res.getLeftParent().entry, res.getRightParent().entry);
        if (complementaryLiteral == null) return false;

        Set<Integer> entry = CombineUtils.combineBy(res.getLeftParent().entry, res.getRightParent().entry, complementaryLiteral);

        return left && right && res.entry.equals(entry);
    }
}
