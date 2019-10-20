package dpll;

import cnf.CNF;
import resolution.Resolution;

import java.util.Comparator;

public class DPLL {
    public static Model solve(CNF cnf) {
        return solve(cnf, new Model());
    }

    public static Model solveWithResolution(CNF cnf, Resolution res) {
        return solveWithResolution(cnf, new Model(), res);
    }

    private static Model solveWithResolution(CNF cnf, Model model, Resolution res) {
        CNF unmodifiedCnf = new CNF(cnf);
        if (cnf.getUnitLiterals().size() != 0) {
            Integer unitLiteral = cnf.getUnitLiterals().get(0);
            cnf = unitPropagate(cnf, unitLiteral);
            model = model.addInterpretation(unitLiteral);

            if (cnf.containsEmptyDisjunction()) {
                res.setNewEntry(unmodifiedCnf.getFirstDisjunctionWithLiteral(-unitLiteral).original);
                return null;
            }
        }

        for (Integer pureLiteral : cnf.getPureLiterals()) {
            cnf = eliminatePureLiteral(cnf, pureLiteral);
            model = model.addInterpretation(pureLiteral);

            if (cnf.containsEmptyDisjunction() || cnf.isEmpty()) {
                res.setNewEntry(unmodifiedCnf.getFirstDisjunctionWithLiteral(pureLiteral).original);
            }
        }

        if (cnf.isEmpty()) return model;
        if (cnf.containsEmptyDisjunction()) return null;

        int literal = chooseLiteral(cnf);

        CNF cnfWithLiteralTrue = new CNF(cnf).addSingleLiteralClause(literal);
        Resolution left = new Resolution(-literal);
        res.addLeftParent(left);
        Model resultLeft = solveWithResolution(cnfWithLiteralTrue, model.addInterpretation(literal), left);

        CNF cnfWithLiteralFalse = new CNF(cnf).addSingleLiteralClause(-literal);
        Resolution right = new Resolution(literal);
        res.addRightParent(right);
        Model resultRight = solveWithResolution(cnfWithLiteralFalse, model.addInterpretation(-literal), right);

        res.combineParents(literal);
        return resultLeft != null ? resultLeft : resultRight;
    }

    private static Model solve(CNF cnf, Model model) {
        for (Integer unitLiteral: cnf.getUnitLiterals()) {
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
        return eliminatePureLiteral(new CNF(cnf).removeLiteralInAllDisjunctions(-literal), literal);
    }

    private static CNF eliminatePureLiteral(CNF cnf, int literal) {
        return new CNF(cnf).removeAllDisjunctionsWithLiteral(literal);
    }

    private static Integer chooseLiteral(CNF cnf) {
        return cnf.getAllDisjunctions().stream()
                .flatMap(disjunction -> disjunction.values.stream())
                .map(Math::abs)
                .min(Comparator.naturalOrder())
                .get();
    }
}

