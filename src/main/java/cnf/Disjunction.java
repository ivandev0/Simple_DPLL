package cnf;

import resolution.Resolution;
import util.CombineUtils;

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

    Integer getFirst() {
        return values.iterator().next();
    }

    boolean contains(int literal) {
        return values.contains(literal);
    }

    public void addLiteral(Integer literal) {
        values.add(literal);
    }

    void remove(Integer literal) {
        values.remove(literal);
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

    @Override
    public String toString() {
        return "[" +
                values.stream().map(String::valueOf).collect(Collectors.joining(", ")) +
                ']';
    }
}
