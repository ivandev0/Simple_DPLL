package resolution.interpolation;

import cnf.CNF;
import cnf.Disjunction;
import dpll.DPLL;
import resolution.Resolution;
import tseytin.TseytinTransformation;
import util.CombineUtils;
import util.IDPool;

import java.util.ArrayList;
import java.util.Set;

public class Interpolation {
    public static CNF calculate(String phiAsText, String psiAsText) {
        IDPool pool = new IDPool();
        CNF phi = TseytinTransformation.transform(phiAsText, pool);
        CNF psi = TseytinTransformation.transform("!(" + psiAsText + ")", pool);

        /*phi = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(1, -2));
            add(new Disjunction(-1, -3));
            add(new Disjunction(2));
        }});
        psi = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(-2, 3));
            add(new Disjunction(2, 4));
            add(new Disjunction(-4));
        }});*/
        CNF combination = phi.addClauses(psi);

        Resolution resProof = DPLL.solveWithResolution(combination);
        //TODO check for unsat

        Set<Integer> onlyPhiAtoms = phi.getAtoms();
        onlyPhiAtoms.removeAll(psi.getAtoms());

        Set<Integer> onlyPsiAtoms = psi.getAtoms();
        onlyPsiAtoms.removeAll(phi.getAtoms());

        Set<Integer> commonAtoms = phi.getAtoms();
        commonAtoms.retainAll(psi.getAtoms());

        try {
            return getInterpolation(resProof, onlyPhiAtoms, onlyPsiAtoms, commonAtoms, psi, phi);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CNF getInterpolation(
            Resolution res,
            Set<Integer> phiAtoms, Set<Integer> psiAtoms, Set<Integer> commonAtoms,
            CNF psi, CNF phi
    ) throws Exception {
        if (res.getLeftParent() == null && res.getRightParent() == null) {
            if (phi.containsClause(res.entry)) {
                return CNF.FALSE; //false
            } else if (psi.containsClause(res.entry)) {
                return CNF.TRUE; //true
            } else {
                throw new Exception("");
            }
        }

        CNF left = getInterpolation(res.getLeftParent(), phiAtoms, psiAtoms, commonAtoms, psi, phi); //I1
        CNF right = getInterpolation(res.getRightParent(), phiAtoms, psiAtoms, commonAtoms, psi, phi); //I2

        Integer complementaryLiteral = CombineUtils.getComplementaryLiteral(res.getLeftParent().entry, res.getRightParent().entry);
        //todo check for null

        if (commonAtoms.contains(Math.abs(complementaryLiteral))) {
            // (p v I1) ^ (!p v I2)
            CNF first = left.disjunction(new CNF(new ArrayList<Disjunction>() {{ add(new Disjunction(complementaryLiteral)); }}));
            CNF second = right.disjunction(new CNF(new ArrayList<Disjunction>() {{ add(new Disjunction(-complementaryLiteral)); }}));
            return first.addClauses(second);

        } else if (phiAtoms.contains(Math.abs(complementaryLiteral))) {
            // I1 v I2
            return left.disjunction(right);
        } else if (psiAtoms.contains(Math.abs(complementaryLiteral))){
            // I1 ^ I2
            return left.addClauses(right);
        } else {
            throw new Exception("");
        }
    }
}


