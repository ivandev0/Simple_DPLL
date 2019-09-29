import cnf.CNF;

import java.util.Comparator;

public class DPLL {
    public static Model solve(CNF cnf, Model model) {
        if (cnf.isEmpty()) {
            return model;
        }
        if (cnf.containsEmptyDisjunction()) {
            return null;
        }

        for (Integer unitLiteral : cnf.getUnitLiterals()) {
            cnf = unitPropagate(cnf, unitLiteral);
            model = model.addInterpretation(unitLiteral);
        }

        for (Integer pureLiteral : cnf.getPureLiterals()) {
            cnf = eliminatePureLiteral(cnf, pureLiteral);
            model = model.addInterpretation(pureLiteral);
        }

        if (cnf.isEmpty()) {
            return model;
        }
        if (cnf.containsEmptyDisjunction()) {
            return null;
        }

        int literal = chooseLiteral(cnf);

        CNF cnfWithLiteralTrue = new CNF(cnf).addSingleLiteralClause(literal);
        Model result = solve(cnfWithLiteralTrue, model.addInterpretation(literal));
        if (result != null) {
            return result;
        }

        CNF cnfWithLiteralFalse = new CNF(cnf).addSingleLiteralClause(-literal);
        return solve(cnfWithLiteralFalse, model.addInterpretation(-literal));
    }

    private static CNF unitPropagate(CNF cnf, int literal) {
        return new CNF(cnf).removeAllDisjunctionsWithLiteral(literal).removeLiteralInAllDisjunctions(-literal);
    }

    private static CNF eliminatePureLiteral(CNF cnf, int literal) {
        return new CNF(cnf).removeAllDisjunctionsWithLiteral(literal).removeLiteralInAllDisjunctions(-literal);
    }

    private static Integer chooseLiteral(CNF cnf) {
        return cnf.getAllDisjunctions().stream()
                .flatMap(disjunction -> disjunction.values.stream())
                .map(Math::abs)
                .min(Comparator.naturalOrder())
                .get();
    }
}
