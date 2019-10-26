package resolution.interpolation;

import cnf.CNF;
import cnf.Disjunction;
import org.junit.Assert;
import org.junit.Test;
import util.IDPool;

public class InterpolationTest {
    @Test
    public void interpolationFromClass() {
        String phi = "(q -> p) ^ (p -> !r) ^ q";
        String psi = "(q ^ !r) v (!q ^ !s) v s";

        IDPool pool = new IDPool();
        String expected = "!r ^ q";
        CNF actual = Interpolation.calculate(phi, psi, pool);
        System.out.println(actual);
        Assert.assertEquals(expected, actual.getSymbolic(pool));
    }
}
