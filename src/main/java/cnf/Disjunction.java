package cnf;

import java.util.*;
import java.util.stream.Collectors;

public class Disjunction {
    public Set<Integer> values, original;
    boolean isEmpty = false;

    public Disjunction(Integer... values) {
        this.values = new HashSet<>(Arrays.asList(values));
        this.original = new HashSet<>(this.values);
    }

    public Disjunction(Set<Integer> values) {
        this.values = new HashSet<>(values);
        this.original = new HashSet<>(this.values);
    }

    public Disjunction(Disjunction other) {
        this.values = new HashSet<>(other.values);
        if (other.isNotSynthetic()) {
            this.original = new HashSet<>(other.original);
        }
        this.isEmpty = other.isEmpty;
    }

    public Disjunction setOriginalToNull() {
        original = null;
        return this;
    }

    public boolean isNotSynthetic() {
        return original != null;
    }

    public boolean hasUnitSize() {
        return values.size() == 1;
    }

    public Integer getFirst() {
        return values.iterator().next();
    }

    public boolean contains(int literal) {
        return values.contains(literal);
    }

    public void addLiteral(Integer literal) {
        values.add(literal);
    }

    public void remove(Integer literal) {
        values.remove(literal);
        if (values.size() == 0) {
            isEmpty = true;
        }
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
