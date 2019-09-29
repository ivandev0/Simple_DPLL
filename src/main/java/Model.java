import java.util.*;
import java.util.stream.Collectors;

public class Model {
    Set<Integer> model;

    public Model() {
        model = new TreeSet<>();
    }

    public Model(Set<Integer> model) {
        this.model = new TreeSet<>(model);
    }

    public Model addInterpretation(int literal) {
        Model newModel = new Model(model);
        newModel.model.add(literal);
        return newModel;
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
