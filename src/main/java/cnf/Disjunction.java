package cnf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Disjunction {
    public List<Integer> values;
    boolean isEmpty = false;

    public Disjunction(Integer... values) {
        this.values = Arrays.asList(values);
    }

    public Disjunction(Disjunction other) {
        this.values = new ArrayList<>(other.values);
        this.isEmpty = other.isEmpty;
    }

    public Disjunction(Integer singleLiteral) {
        this.values = new ArrayList<>();
        this.values.add(singleLiteral);
    }

    public Disjunction() {
        values = new ArrayList<>();
    }

    public boolean hasUnitSize() {
        return values.size() == 1;
    }

    public Integer getFirst() {
        return values.get(0);
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
