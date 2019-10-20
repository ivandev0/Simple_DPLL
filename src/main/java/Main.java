import cnf.CNF;
import dpll.DPLL;
import reader.DimacsReader;
import tseytin.TseytinTransformation;

public class Main {
    public static void main(String[] args) {
        CNF cnf = DimacsReader.readFromString(
                "p cnf 3\n" +
                "1 2 0\n" +
                "-1 3 0"
        );
        System.out.println(DPLL.solve(cnf));

        CNF fromTseytin = TseytinTransformation.transform("(p1 ^ p2) -> (!p3 v p4)");
        System.out.println(DPLL.solve(fromTseytin));
    }
}
