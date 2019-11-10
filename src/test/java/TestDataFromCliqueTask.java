import cnf.CNF;
import dpll.DPLL;
import dpll.Model;
import org.junit.Assert;
import org.junit.Test;
import reader.DimacsReader;

import java.io.File;
import java.io.IOException;

public class TestDataFromCliqueTask {
    private String dataFolder = "data_from_clique_task";

    private void basicTest(String fileName, boolean mustBeUnsat) throws IOException {
        CNF cnf = DimacsReader.readFromFile(new File(getClass().getResource(dataFolder + "/" + fileName).getFile()));

        Model model = DPLL.solve(cnf);
        if (mustBeUnsat){
            Assert.assertNull(model);
        } else {
            Assert.assertNotNull(model);
        }
    }

    @Test
    public void testN3K2() throws IOException {
        basicTest("cnf_n3_k2", false);
    }

    @Test
    public void testN4K2() throws IOException {
        basicTest("cnf_n4_k2", false);
    }

    @Test
    public void testN5K2() throws IOException {
        basicTest("cnf_n5_k2", false);
    }

    @Test
    public void testN6K2() throws IOException {
        basicTest("cnf_n6_k2", true);
    }

    @Test
    public void testN8K3() throws IOException {
        long time = System.nanoTime();
        basicTest("k3/cnf_n8_k3", false);
        System.out.println("calculated in " + (System.nanoTime() - time) * 1e-9 + " seconds");
    }
}
