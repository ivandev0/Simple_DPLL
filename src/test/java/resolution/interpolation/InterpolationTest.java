package resolution.interpolation;

import cnf.CNF;
import cnf.Disjunction;
import dpll.DPLL;
import dpll.Model;
import org.junit.Assert;
import org.junit.Test;
import tseytin.TseytinTransformation;
import util.IDPool;

import java.util.List;

public class InterpolationTest {
    @Test
    public void interpolationFromClass() {
        String phi = "(q -> p) ^ (p -> !r) ^ q";
        String psi = "(q ^ !r) v (!q ^ !s) v s";

        IDPool pool = new IDPool();
        CNF interpolationCnf = Interpolation.calculate(phi, psi, pool);
        checkInterpolationCorrectness(phi, psi, interpolationCnf);
        Assert.assertEquals("!r ^ q", interpolationCnf.getSymbolic(pool));
    }

    @Test
    public void interpolationFromClassSimplified() {
        String phi = "(!q v p) ^ (!p v !r) ^ q";
        String psi = "(q ^ !r) v (!q ^ !s) v s";

        IDPool pool = new IDPool();
        CNF interpolationCnf = Interpolation.calculate(phi, psi, pool);
        checkInterpolationCorrectness(phi, psi, interpolationCnf);
        Assert.assertEquals("!r ^ q", interpolationCnf.getSymbolic(pool));
    }

    private void checkInterpolationCorrectness(String phi, String psi, CNF interpolationCnf) {
        IDPool pool = new IDPool(); //using common pool to be sure that common variables have equal designation
        List<Model> phiModels = TseytinTransformation.transform(phi, pool).getAllModels();

        //check if interpolation is satisfiable in phi model
        for (Model phiModel : phiModels) {
            Model interpolationModel = DPLL.solve(interpolationCnf.applyModel(phiModel));
            Assert.assertNotNull(interpolationModel);
        }

        //check if phi is satisfiable in interpolation model
        List<Model> interpolationModels = interpolationCnf.getAllModels();
        for (Model interpolationModel : interpolationModels) {
            Assert.assertNotNull(DPLL.solve(TseytinTransformation.transform(psi, pool).applyModel(interpolationModel)));
        }
    }
}
