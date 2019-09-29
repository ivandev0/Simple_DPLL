package cnf;

import java.util.ArrayList;
import java.util.List;

public class Disjunction {
    public List<Integer> values;
    boolean isEmpty = false;

    public Disjunction(List<Integer> values) {
        this.values = values;
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
}
