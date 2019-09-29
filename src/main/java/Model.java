import java.util.List;
import java.util.stream.Collectors;

public class Model {
    List<Pair> model;

    public Model(List<Pair> model) {
        this.model = model;
    }

    class Pair {
        Integer literal, value;

        Pair(Integer literal, Integer value) {
            this.literal = literal;
            this.value = value;
        }

        @Override
        public String toString() {
            return literal + "=" + value;
        }
    }

    public void addInterpretation(int literal, int value) {
        model.add(new Pair(literal, value));
    }

    public Model addTrueInterpretation(int literal) {
        Model newModel = new Model(model);
        newModel.model.add(new Pair(literal, 1));
        return newModel;
    }

    public Model addFalseInterpretation(int literal) {
        Model newModel = new Model(model);
        newModel.model.add(new Pair(literal, 0));
        return newModel;
    }

    @Override
    public String toString() {
        return model.stream().map(String::valueOf).collect(Collectors.joining());
    }
}
