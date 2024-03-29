package cnf;

import dpll.Model;
import resolution.Resolution;
import util.CombineUtils;
import util.IDPool;

import java.util.*;
import java.util.stream.Collectors;

public class Disjunction {
    public Set<Integer> values;
    public Resolution res;

    public Disjunction(Integer... values) {
        this(new HashSet<>(Arrays.asList(values)));
    }

    public Disjunction(Set<Integer> values) {
        this.values = new HashSet<>(values);
        res = new Resolution(values);
    }

    Disjunction(Disjunction other) {
        this.values = new HashSet<>(other.values);
        if (other.isNotSynthetic()) {
            this.res = new Resolution(other.res);
        }
    }

    public Disjunction setAsSynthetic() {
        res = null;
        return this;
    }

    boolean isNotSynthetic() {
        return res != null;
    }

    boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean hasUnitSize() {
        return values.size() == 1;
    }

    SingleLiteralDisjunction getFirst() {
        return new SingleLiteralDisjunction(values.iterator().next());
    }

    boolean contains(SingleLiteralDisjunction literal) {
        return values.contains(literal.get());
    }

    boolean isEqualTo(Set<Integer> clause) {
        return this.values.equals(clause);
    }

    Disjunction disjunction(Disjunction other) {
        if (this.contains(SingleLiteralDisjunction.TRUE) || other.contains(SingleLiteralDisjunction.TRUE)) {
            return SingleLiteralDisjunction.TRUE;
        }

        if (CombineUtils.getComplementaryLiteral(this.values, other.values) != null) {
            return SingleLiteralDisjunction.TRUE;
        }

        Disjunction result = new Disjunction(this);
        result.values.addAll(other.values);
        result.remove(SingleLiteralDisjunction.FALSE);
        if (result.isEmpty()) return SingleLiteralDisjunction.FALSE;

        result.res.entry = new HashSet<>(result.values);
        return result;
    }

    void remove(SingleLiteralDisjunction literal) {
        values.remove(literal.get());
    }

    void replaceInPlace(Integer oldLiteral, Integer newLiteral) {
        if (values.contains(oldLiteral)) {
            values.remove(oldLiteral);
            values.add(newLiteral);
        }
        res.setNewEntry(values);
    }

    Disjunction combineByLiteral(Disjunction other, Integer literal) {
        Set<Integer> newValues = CombineUtils.combineBy(this.res.entry, other.res.entry, literal);
        Disjunction result = new Disjunction(newValues);
        result.res.addRightParent(this.res);
        result.res.addLeftParent(other.res);
        return result;
    }

    boolean areSingleLiteralComplementary(Disjunction other) {
        return this.hasUnitSize() && other.hasUnitSize() && CombineUtils.getComplementaryLiteral(values, other.values) != null;
    }

    Disjunction applyModel(Model model) {
        Set<Integer> newSet = new HashSet<>();
        for (Integer literal : values) {
            if (model.containsLiteral(literal)) {
                return SingleLiteralDisjunction.TRUE;
            } else if (!model.containsLiteral(-literal)) {
                //if model doesn't contains neither literal nor -literal
                newSet.add(literal);
            }
        }

        //if model contains only -literal
        if (newSet.isEmpty()) return SingleLiteralDisjunction.FALSE;

        return new Disjunction(newSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disjunction other = (Disjunction) o;
        return Objects.equals(values, other.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    String getSymbolic(IDPool pool) {
        return values.stream().map(pool::nameById).map(it -> it.replace("@", "")).collect(Collectors.joining(" v "));
    }

    @Override
    public String toString() {
        return "[" +
                values.stream().map(String::valueOf).collect(Collectors.joining(", ")) +
                ']';
    }
}
