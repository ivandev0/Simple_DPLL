package dpll;

import cnf.SingleLiteralDisjunction;

import java.util.*;
import java.util.stream.Collectors;

public class Model {
    private Set<Integer> model;

    Model() {
        model = new TreeSet<>();
    }

    public Model(Integer... model) {
        this.model = new TreeSet<>(Arrays.asList(model));
    }

    Model(Set<Integer> model) {
        this.model = new TreeSet<>(model);
    }

    public Model addInterpretation(SingleLiteralDisjunction literal) {
        Model newModel = new Model(model);
        newModel.model.add(literal.get());
        return newModel;
    }

    public boolean containsLiteral(Integer literal) {
        return model.contains(literal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model other = (Model) o;
        return Objects.equals(model, other.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    @Override
    public String toString() {
        return model.stream().map(String::valueOf).collect(Collectors.joining("; "));
    }
}
