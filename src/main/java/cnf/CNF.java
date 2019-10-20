package cnf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CNF {
    private List<Disjunction> clauses;

    public CNF(List<Disjunction> clauses) {
        this.clauses = clauses;
    }

    public CNF(CNF toCopy) {
        this.clauses = toCopy.clauses.stream().map(Disjunction::new).collect(Collectors.toList());
    }

    public CNF addSingleLiteralClause(Integer literal) {
        clauses.add(0, new Disjunction(literal).setOriginalToNull());
        return this;
    }

    public List<Disjunction> getAllDisjunctions() {
        return clauses;
    }

    public Disjunction getFirstDisjunctionWithLiteral(Integer literal) {
        return clauses.stream().filter(dis -> dis.contains(literal) && dis.isNotSynthetic()).findFirst().get();
    }

    public CNF removeAllDisjunctionsWithLiteral(int literal) {
        clauses.removeIf(disjunction -> disjunction.contains(literal));
        return this;
    }

    public CNF removeLiteralInAllDisjunctions(int literal) {
        clauses.forEach(disjunction -> disjunction.remove(literal));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CNF cnf = (CNF) o;
        return Objects.equals(clauses, cnf.clauses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clauses);
    }

    @Override
    public String toString() {
        return "[" +
                clauses.stream().map(String::valueOf).collect(Collectors.joining(" ")) +
                ']';
    }
}
