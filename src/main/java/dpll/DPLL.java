package dpll;

import cnf.CNF;
import cnf.SingleLiteralDisjunction;
import resolution.Resolution;

import java.util.Comparator;

public class DPLL {
    public static Model solve(CNF cnf) {
        if (cnf == CNF.FALSE || cnf.containsFalseClause()) return null;
        return solve(cnf, new Model());
    }

    public static Resolution solveWithResolution(CNF cnf) {
        while (cnf.getUnitLiterals().size() != 0) {
            SingleLiteralDisjunction unitLiteral = cnf.getUnitLiterals().get(0);
            cnf = unitPropagate(cnf, unitLiteral);
        }

        for (SingleLiteralDisjunction pureLiteral : cnf.getPureLiterals()) {
            cnf = eliminatePureLiteral(cnf, pureLiteral);
        }

        if (cnf.isEmpty()) return null;
        if (cnf.containsEmptyDisjunction()) return cnf.getAllDisjunctions().get(0).res;

        Resolution result = new Resolution(0);
        SingleLiteralDisjunction literal = chooseLiteral(cnf);

        CNF cnfWithLiteralTrue = new CNF(cnf).addSingleLiteralClause(literal);
        result.addLeftParent(solveWithResolution(cnfWithLiteralTrue));

        CNF cnfWithLiteralFalse = new CNF(cnf).addSingleLiteralClause(literal.negate());
        result.addRightParent(solveWithResolution(cnfWithLiteralFalse));

        if (result.getLeftParent() == null || result.getRightParent() == null) return null;
        result.setEntryFromParentsAndRemoveLiteral(literal);
        return result;
    }

    private static Model solve(CNF cnf, Model model) {
        for (SingleLiteralDisjunction unitLiteral: cnf.getUnitLiterals()) {
            cnf = unitPropagate(cnf, unitLiteral);
            model = model.addInterpretation(unitLiteral);
        }

        for (SingleLiteralDisjunction pureLiteral : cnf.getPureLiterals()) {
            cnf = eliminatePureLiteral(cnf, pureLiteral);
            model = model.addInterpretation(pureLiteral);
        }

        if (cnf.isEmpty()) {
            return model;
        }
        if (cnf.containsEmptyDisjunction()) {
            return null;
        }

        SingleLiteralDisjunction literal = chooseLiteral(cnf);

        CNF cnfWithLiteralTrue = new CNF(cnf).addSingleLiteralClause(literal);
        Model result = solve(cnfWithLiteralTrue, model.addInterpretation(literal));
        if (result != null) {
            return result;
        }

        CNF cnfWithLiteralFalse = new CNF(cnf).addSingleLiteralClause(literal.negate());
        return solve(cnfWithLiteralFalse, model.addInterpretation(literal.negate()));
    }

    private static CNF unitPropagate(CNF cnf, SingleLiteralDisjunction literal) {
        return eliminatePureLiteral(new CNF(cnf).removeLiteralInAllDisjunctions(literal.negate()), literal);
    }

    private static CNF eliminatePureLiteral(CNF cnf, SingleLiteralDisjunction literal) {
        return new CNF(cnf).removeAllDisjunctionsWithLiteral(literal);
    }

    private static SingleLiteralDisjunction chooseLiteral(CNF cnf) {
        if (cnf.getUnitLiterals().size() != 0) {
            return cnf.getUnitLiterals().get(0);
        }
        int literal = cnf.getAllDisjunctions().stream()
                .flatMap(disjunction -> disjunction.values.stream())
                .map(Math::abs)
                .min(Comparator.naturalOrder())
                .get();
        return new SingleLiteralDisjunction(literal).setAsSynthetic();
    }
}
