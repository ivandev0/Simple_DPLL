package cnf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CNF {
    private List<Disjunction> clauses;

    public CNF(List<Disjunction> clauses) {
        this.clauses = clauses;
    }

    public CNF(CNF toCopy) {
        this.clauses = new ArrayList<>(toCopy.clauses);
    }

    public CNF addSingleLiteralClause(Integer literal) {
        clauses.add(new Disjunction(literal));
        return this;
    }

    public List<Disjunction> getAllDisjunctions() {
        return clauses;
    }

    public CNF removeAllDisjunctionsWithLiteral(int literal) {
        clauses.removeIf(disjunction -> disjunction.contains(literal));
        return this;
    }

    public boolean isEmpty() {
        return clauses.isEmpty();
    }

    public boolean containsEmptyDisjunction() {
        return clauses.stream().anyMatch(disjunction -> disjunction.isEmpty);
    }

    public List<Integer> getUnitLiterals() {
        return clauses.stream()
                .filter(Disjunction::hasUnitSize)
                .map(Disjunction::getFirst)
                .collect(Collectors.toList());
    }

    public List<Integer> getPureLiterals() {
        Set<Integer> literalsList = clauses.stream()
                .flatMap(disjunction -> disjunction.values.stream())
                .collect(Collectors.toSet());

        List<Integer> result = new ArrayList<>();
        for (Integer literal : literalsList) {
            if (!literalsList.contains(-literal)) {
                result.add(literal);
            }
        }

        return result;
    }
}
