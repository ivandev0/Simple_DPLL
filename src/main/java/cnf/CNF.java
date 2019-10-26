package cnf;

import util.CombineUtils;
import util.IDPool;

import java.util.*;
import java.util.stream.Collectors;

public class CNF {
    public static CNF TRUE = new CNF(SingleLiteralDisjunction.TRUE);
    public static CNF FALSE = new CNF(SingleLiteralDisjunction.FALSE);

    private List<Disjunction> clauses;

    public CNF(Disjunction... clauses) {
        this(Arrays.asList(clauses));
    }

    public CNF(List<Disjunction> clauses) {
        this.clauses = clauses;
    }

    public CNF(CNF toCopy) {
        this.clauses = toCopy.clauses.stream().map(Disjunction::new).collect(Collectors.toList());
    }

    public CNF(CNF first, CNF second) {
        this.clauses = new ArrayList<>(first.clauses);
        this.clauses.addAll(second.clauses);
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
     * Conjunction operation with minor optimizations
     */
    public CNF addClausesAndSimplify(CNF other) {
        if (this == FALSE || other == FALSE) {
            return FALSE;
        }

        List<Set<Integer>> clauses = new ArrayList<>();
        clauses.addAll(this.clauses.stream().map(it -> it.values).collect(Collectors.toList()));
        clauses.addAll(other.clauses.stream().map(it -> it.values).collect(Collectors.toList()));
        clauses.removeIf(it -> it.contains(SingleLiteralDisjunction.TRUE.get()));
        for (int i = 0; i < clauses.size(); i++) {
            for (int j = 0; j < clauses.size(); j++) {
                if (j != i) {
                    Set<Integer> first = clauses.get(i);
                    Set<Integer> second = clauses.get(j);

                    Integer complementaryLiteral = CombineUtils.getComplementaryLiteral(first, second);
                    if (complementaryLiteral != null) {
                        if (first.size() == 1) {
                            Integer next = first.iterator().next();
                            second.remove(-next);
                            if (second.size() == 0) return CNF.FALSE;
                        } else if (second.size() == 1) {
                            Integer next = second.iterator().next();
                            first.remove(-next);
                            if (first.size() == 0) return CNF.FALSE;
                        }
                        i = j = 0;
                    }
                }
            }
        }

        return new CNF(new ArrayList<>(clauses.stream().map(Disjunction::new).collect(Collectors.toList())));
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

    public String getSymbolic(IDPool pool) {
        return clauses.stream().map(it -> it.getSymbolic(pool)).collect(Collectors.joining(" ^ "));
    }

    @Override
    public String toString() {
        return "[" +
                clauses.stream().map(String::valueOf).collect(Collectors.joining(" ")) +
                ']';
    }
}
