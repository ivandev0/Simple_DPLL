package resolution.interpolation;

import cnf.CNF;
import cnf.Disjunction;
import dpll.DPLL;
import resolution.Resolution;
import tseytin.TseytinTransformation;
import util.CombineUtils;
import util.IDPool;

import java.util.Set;

public class Interpolation {
    public static CNF calculate(String phiAsText, String psiAsText) {
        IDPool pool = new IDPool();
        return calculate(phiAsText, psiAsText, pool);
    }

    public static CNF calculate(String phiAsText, String psiAsText, IDPool pool) {
        CNF phi = TseytinTransformation.transform(phiAsText, pool);
        CNF psi = TseytinTransformation.transform("!(" + psiAsText + ")", pool);

        CNF combination = new CNF(phi, psi);

        Resolution resProof = DPLL.solveWithResolution(combination);
        if (resProof == null)
            throw new UnsupportedOperationException("Interpolation can be found only in not feasible formula");

        Set<Integer> onlyPhiAtoms = phi.getAtoms();
        onlyPhiAtoms.removeAll(psi.getAtoms());

        Set<Integer> onlyPsiAtoms = psi.getAtoms();
        onlyPsiAtoms.removeAll(phi.getAtoms());

        Set<Integer> commonAtoms = phi.getAtoms();
        commonAtoms.retainAll(psi.getAtoms());

        return getInterpolation(resProof, onlyPhiAtoms, onlyPsiAtoms, commonAtoms, psi, phi);
    }

    private static CNF getInterpolation(
            Resolution res,
            Set<Integer> phiAtoms, Set<Integer> psiAtoms, Set<Integer> commonAtoms,
            CNF psi, CNF phi
    ) {
        if (res.getLeftParent() == null && res.getRightParent() == null) {
            if (phi.containsClause(res.entry)) {
                return CNF.FALSE; //false
            } else if (psi.containsClause(res.entry)) {
                return CNF.TRUE; //true
            } else {
                throw new IllegalArgumentException("Resolution proof contains literal which is neither in psi nor in phi");
            }
        }

        CNF left = getInterpolation(res.getLeftParent(), phiAtoms, psiAtoms, commonAtoms, psi, phi); //I1
        CNF right = getInterpolation(res.getRightParent(), phiAtoms, psiAtoms, commonAtoms, psi, phi); //I2

        Integer complementaryLiteral = CombineUtils.getComplementaryLiteral(res.getLeftParent().entry, res.getRightParent().entry); //p
        if (complementaryLiteral == null) {
            throw new IllegalArgumentException("Resolution proof contains wrong decision");
        }

        if (commonAtoms.contains(Math.abs(complementaryLiteral))) {
            // (p v I1) ^ (!p v I2)
            CNF first = left.disjunction(new CNF(new Disjunction(complementaryLiteral)));
            CNF second = right.disjunction(new CNF(new Disjunction(-complementaryLiteral)));
            return first.addClausesAndSimplify(second);
        } else if (phiAtoms.contains(Math.abs(complementaryLiteral))) {
            // I1 v I2
            return left.disjunction(right);
        } else if (psiAtoms.contains(Math.abs(complementaryLiteral))) {
            // I1 ^ I2
            return left.addClausesAndSimplify(right);
        } else {
            throw new IllegalArgumentException("Complementary literal isn't present neither in psi nor in phi");
        }
    }
}


