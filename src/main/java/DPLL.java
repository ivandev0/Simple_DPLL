import cnf.CNF;
import cnf.Disjunction;

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
            model = model.addTrueInterpretation(unitLiteral);
        }

        for (Integer unitLiteral : cnf.getPureLiterals()) {
            cnf = eliminatePureLiteral(cnf, unitLiteral);
            model = model.addTrueInterpretation(unitLiteral);
        }

        int literal = chooseLiteral(cnf);

        CNF cnfWithLiteralTrue = new CNF(cnf).addSingleLiteralClause(literal);
        Model result = solve(cnfWithLiteralTrue, model.addTrueInterpretation(literal));
        if (result != null) {
            return result;
        }

        CNF cnfWithLiteralFalse = new CNF(cnf).addSingleLiteralClause(-literal);
        return solve(cnfWithLiteralFalse, model.addFalseInterpretation(-literal));
    }

    private static CNF unitPropagate(CNF cnf, int literal) {
        CNF newCnf = new CNF(cnf);
        return eliminatePureLiteral(newCnf.removeAllDisjunctionsWithLiteral(literal), -literal);
    }

    private static CNF eliminatePureLiteral(CNF cnf, int literal) {
        CNF newCnf = new CNF(cnf);
        for (Disjunction disjunction : newCnf.getAllDisjunctions()) {
            if (disjunction.contains(literal)) {
                disjunction.remove(literal);
            }
        }
        return newCnf;
    }

    private static Integer chooseLiteral(CNF cnf) {
        return cnf.getAllDisjunctions().stream()
                .flatMap(disjunction -> disjunction.values.stream())
                .min(Comparator.naturalOrder())
                .get();
    }
}
