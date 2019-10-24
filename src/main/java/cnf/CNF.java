package cnf;

import util.CombineUtils;

import java.util.*;
import java.util.stream.Collectors;

public class CNF {
    public static CNF TRUE = new CNF(new ArrayList<Disjunction>() {{
        add(SingleLiteralDisjunction.TRUE);
    }});
    public static CNF FALSE = new CNF(new ArrayList<Disjunction>() {{
        add(SingleLiteralDisjunction.FALSE);
    }});

    private List<Disjunction> clauses;

    public CNF(List<Disjunction> clauses) {
        this.clauses = clauses;
    }

    public CNF(CNF toCopy) {
        this.clauses = toCopy.clauses.stream().map(Disjunction::new).collect(Collectors.toList());
    }

    public CNF addSingleLiteralClause(SingleLiteralDisjunction literal) {
        if (!clauses.contains(literal)) {
            clauses.add(0, literal);
        }
        return this;
    }

    public CNF disjunction(CNF other) {
        if (this == TRUE || other == TRUE) {
            return TRUE;
        }
        List<Disjunction> clauses = this.clauses.stream()
                .map(e1 -> other.clauses.stream().map(e1::disjunction).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (clauses.contains(SingleLiteralDisjunction.FALSE)) return CNF.FALSE;

        return new CNF(clauses);
    }

    /**
     * Conjunction operation
     */
    public CNF addClauses(CNF other) {
        if (this == FALSE || other == FALSE) {
            return FALSE;
        }

        List<Disjunction> newList = new ArrayList<>();
        for (Disjunction first : this.clauses.stream().filter(it -> it != SingleLiteralDisjunction.TRUE).collect(Collectors.toList())) {
            Integer complementaryLiteral = null;
            for (Disjunction second : other.clauses.stream().filter(it -> it != SingleLiteralDisjunction.TRUE).collect(Collectors.toList())) {
                complementaryLiteral = CombineUtils.getComplementaryLiteral(first.values, second.values);
                if (complementaryLiteral != null) {
                    if (first.hasUnitSize() && second.hasUnitSize()) {
                        continue;
                    }
                    if (first.hasUnitSize()) {
                        newList.add(second.conjunctionWithComplementaryLiteral(first.getFirst()));
                    }
                    if (second.hasUnitSize()) {
                        newList.add(first.conjunctionWithComplementaryLiteral(second.getFirst()));
                    }
                    continue;
                }
                newList.add(second);
            }
            if (complementaryLiteral == null) {
                newList.add(first);
            }
        }

        if (newList.size() == 0) return CNF.FALSE;
        return new CNF(newList);
    }

    public List<Disjunction> getAllDisjunctions() {
        return clauses;
    }

    public Set<Integer> getAtoms() {
        Set<Integer> atoms = new HashSet<>();
        for (Disjunction clause : clauses) {
            atoms.addAll(clause.res.entry.stream().map(Math::abs).collect(Collectors.toList()));
        }
        return atoms;
    }

    public CNF removeAllDisjunctionsWithLiteral(SingleLiteralDisjunction literal) {
        clauses.removeIf(disjunction -> disjunction.contains(literal));
        return this;
    }

    public CNF removeLiteralInAllDisjunctions(SingleLiteralDisjunction unitDisjunction) {
        if (unitDisjunction.isNotSynthetic()) {
            clauses.forEach(disjunction -> {
                if (disjunction.contains(unitDisjunction)) {
                    disjunction.res = disjunction.combineByLiteral(unitDisjunction, unitDisjunction.get()).res;
                    disjunction.remove(unitDisjunction);
                }
            });
        } else {
            clauses.forEach(disjunction -> disjunction.remove(unitDisjunction));
        }
        return this;
    }

    public boolean isEmpty() {
        return clauses.isEmpty();
    }

    public boolean containsEmptyDisjunction() {
        return clauses.stream().anyMatch(disjunction -> disjunction.isEmpty);
    }

    public boolean containsClause(Set<Integer> clause) {
        for (Disjunction disjunction : clauses) {
            if (disjunction.isEqualTo(clause)) {
                return true;
            }
        }
        return false;
    }

    public List<SingleLiteralDisjunction> getUnitLiterals() {
        return clauses.stream()
                .filter(Disjunction::hasUnitSize)
                .map(SingleLiteralDisjunction::new)
                .collect(Collectors.toList());
    }

    public List<SingleLiteralDisjunction> getPureLiterals() {
        Set<Integer> literalsList = clauses.stream()
                .flatMap(disjunction -> disjunction.values.stream())
                .collect(Collectors.toSet());

        List<SingleLiteralDisjunction> result = new ArrayList<>();
        for (Integer literal : literalsList) {
            if (!literalsList.contains(-literal)) {
                result.add(new SingleLiteralDisjunction(literal));
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
