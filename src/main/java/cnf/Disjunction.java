package cnf;

import resolution.Resolution;
import util.CombineUtils;
import util.IDPool;

import java.util.*;
import java.util.stream.Collectors;

public class Disjunction {
    public Set<Integer> values;
    public Resolution res;
    boolean isEmpty = false;

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
            this.res = other.res;
        }
        this.isEmpty = other.isEmpty;
    }

    public Disjunction setAsSynthetic() {
        res = null;
        return this;
    }

    boolean isNotSynthetic() {
        return res != null;
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

        if (CombineUtils.getComplementaryLiteral(this.values, other.values) != null) return SingleLiteralDisjunction.FALSE;

        Disjunction result = new Disjunction(this);
        result.values.addAll(other.values);
        result.remove(SingleLiteralDisjunction.FALSE);
        return result;
    }

    void remove(SingleLiteralDisjunction literal) {
        values.remove(literal.get());
        if (values.size() == 0) {
            isEmpty = true;
        }
    }

    Disjunction combineByLiteral(Disjunction other, Integer literal) {
        Set<Integer> newValues = CombineUtils.combineBy(this.res.entry, other.res.entry, literal);
        Disjunction result = new Disjunction(newValues);
        result.res.addRightParent(this.res);
        result.res.addLeftParent(other.res);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disjunction other = (Disjunction) o;
        return isEmpty == other.isEmpty &&
                Objects.equals(values, other.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, isEmpty);
    }

    String getSymbolic(IDPool pool) {
        return values.stream().map(pool::nameById).collect(Collectors.joining(" v "));
    }

    @Override
    public String toString() {
        return "[" +
                values.stream().map(String::valueOf).collect(Collectors.joining(", ")) +
                ']';
    }
}
