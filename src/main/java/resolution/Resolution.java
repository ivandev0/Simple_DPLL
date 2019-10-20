package resolution;

import cnf.Disjunction;

import java.util.HashSet;
import java.util.Set;

public class Resolution {
    private Resolution leftParent = null, rightParent = null;
    public Set<Integer> entry;

    public Resolution(Integer entry) {
        this.entry = new HashSet<Integer>() {{ add(entry); }};
    }

    public void setNewEntry(Set<Integer> literals) {
        this.entry = new HashSet<>(literals);
    }

    public Resolution addLeftParent(Resolution resolution) {
        leftParent = resolution;
        return this;
    }

    public Resolution addRightParent(Resolution resolution) {
        rightParent = resolution;
        return this;
    }

    public void combineParents(Integer complementaryLiteral) {
        Set<Integer> leftEntry = new Disjunction(leftParent.entry).values;
        leftEntry.removeIf(num -> num.equals(complementaryLiteral) || num.equals(-complementaryLiteral));
        Set<Integer> rightEntry = new Disjunction(rightParent.entry).values;
        rightEntry.removeIf(num -> num.equals(complementaryLiteral) || num.equals(-complementaryLiteral));
        leftEntry.addAll(rightEntry);
        entry = new HashSet<>(leftEntry);
    }

    @Override
    public String toString() {
        if (leftParent == null && rightParent == null) {
            return "entry: " + entry;
        }

        String left = (leftParent != null) ? leftParent.toString().replace("\n", "\n\t") : "null";
        String right = (rightParent != null) ? rightParent.toString().replace("\n", "\n\t") : "null";

        return "entry: " + entry + "\n" +
                "left: " + "\n    " + left + "\n" +
                "right: " + "\n    " + right;
    }
}
