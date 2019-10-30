package cnf;

import dpll.Model;
import tseytin.TseytinTransformation;
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

        if (clauses.contains(SingleLiteralDisjunction.FALSE)) return FALSE;
        clauses.remove(SingleLiteralDisjunction.TRUE);

        return new CNF(clauses);
    }

    /**
     * Conjunction operation with minor optimizations
     */
    public CNF addClausesAndSimplify(CNF other) {
        if (this == FALSE || other == FALSE) {
            return FALSE;
        }

        //if (clauses.size() == 1 && other.clauses.size() == 1)
        if (clauses.get(0).areSingleLiteralComplementary(other.clauses.get(0))) {
            return FALSE;
        }

        List<Set<Integer>> clauses = new ArrayList<>();
        clauses.addAll(this.clauses.stream().map(it -> it.values).collect(Collectors.toList()));
        clauses.addAll(other.clauses.stream().map(it -> it.values).collect(Collectors.toList()));
        clauses.removeIf(it -> it.contains(SingleLiteralDisjunction.TRUE.get()));

        if (clauses.size() == 0) return TRUE;
        return new CNF(new ArrayList<>(clauses.stream().map(Disjunction::new).collect(Collectors.toList())));
    }

    public List<Disjunction> getAllDisjunctions() {
        return clauses;
    }

    public Set<Integer> getAtoms() {
        return clauses.stream()
                .flatMap(it -> it.res.entry.stream())
                .map(Math::abs)
                .collect(Collectors.toSet());
    }

    public CNF removeAllDisjunctionsWithLiteral(SingleLiteralDisjunction literal) {
        clauses.removeIf(disjunction -> disjunction.contains(literal));
        return this;
    }

    public CNF removeLiteralInAllDisjunctions(SingleLiteralDisjunction unitDisjunction) {
        if (unitDisjunction.isNotSynthetic()) {
            clauses.stream()
                    .filter(disjunction -> disjunction.contains(unitDisjunction))
                    .forEach(disjunction -> {
                        disjunction.res = disjunction.combineByLiteral(unitDisjunction, unitDisjunction.get()).res;
                        disjunction.remove(unitDisjunction);
                    });
        } else {
            clauses.forEach(disjunction -> disjunction.remove(unitDisjunction));
        }
        return this;
    }

    public CNF removeDuplicates() {
        clauses = new ArrayList<>(new HashSet<>(this.clauses));
        return this;
    }

    public void replaceInPlace(Integer oldLiteral, Integer newLiteral) {
        clauses.forEach(it -> {
            it.replaceInPlace(oldLiteral, newLiteral);
            it.replaceInPlace(-oldLiteral, -newLiteral);
        });
    }

    public boolean isEmpty() {
        return clauses.isEmpty();
    }

    public boolean containsEmptyDisjunction() {
        return clauses.stream().anyMatch(Disjunction::isEmpty);
    }

    public boolean containsClause(Set<Integer> clause) {
        return clauses.stream()
                .anyMatch(disjunction -> disjunction.isEqualTo(clause));
    }

    public boolean containsFalseClause() {
        return clauses.stream().anyMatch(disjunction -> disjunction == SingleLiteralDisjunction.FALSE);
    }


    public CNF inverse(IDPool pool) {
        return TseytinTransformation.transform("!( " + this.getSymbolic(pool) + " )", pool);
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

        return literalsList.stream()
                .filter(literal -> !literalsList.contains(-literal))
                .map(SingleLiteralDisjunction::new)
                .collect(Collectors.toList());
    }

    public CNF applyModel(Model model) {
        List<Disjunction> newClauses = new ArrayList<>();
        for (Disjunction clause : clauses) {
            Disjunction disjunction = clause.applyModel(model);
            if (disjunction == SingleLiteralDisjunction.FALSE) {
                return FALSE;
            } else if (disjunction != SingleLiteralDisjunction.TRUE) {
                newClauses.add(disjunction);
            }
        }

        if (newClauses.size() == 0) return TRUE;
        return new CNF(newClauses);
    }

    public List<Model> getAllModels() {
        List<Model> result = new ArrayList<>();
        List<Integer> atoms = new ArrayList<>(getAtoms());

        for (int i = 0; i < Math.pow(2, atoms.size()); i++) {
            Model model = new Model();
            for (int j = 0; j < atoms.size(); j++) {
                int bit = (i >> j) & 1;
                SingleLiteralDisjunction literal = new SingleLiteralDisjunction(atoms.get(j));
                model = (bit == 1) ? model.addInterpretation(literal) : model.addInterpretation(literal.negate());
            }
            if (this.applyModel(model) == TRUE) {
                result.add(model);
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
        return clauses.stream().map(it -> {
            String disjunction = it.getSymbolic(pool);
            return disjunction.contains("v") ? " ( " + disjunction + " ) " : disjunction;
        }).collect(Collectors.joining(" ^ "));
    }

    @Override
    public String toString() {
        return "[" +
                clauses.stream().map(String::valueOf).collect(Collectors.joining(" ")) +
                ']';
    }
}
