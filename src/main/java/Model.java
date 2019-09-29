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
    public String toString() {
        return model.stream().map(String::valueOf).collect(Collectors.joining("; "));
    }
}
