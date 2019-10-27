package resolution;

import cnf.SingleLiteralDisjunction;
import util.CombineUtils;

import java.util.HashSet;
import java.util.Set;

public class Resolution {
    private Resolution leftParent = null, rightParent = null;
    public Set<Integer> entry;

    public Resolution(Integer entry) {
        this.entry = new HashSet<Integer>() {{ add(entry); }};
    }

    public Resolution(Set<Integer> literals) {
        this.entry = new HashSet<>(literals);
    }

    public Resolution(Resolution res) {
        if (res.leftParent != null) {
            this.leftParent = new Resolution(res.leftParent);
        }
        if (res.rightParent != null) {
            this.rightParent = new Resolution(res.rightParent);
        }
        this.entry = new HashSet<>(res.entry);
    }

    public void setNewEntry(Set<Integer> literals) {
        this.entry = new HashSet<>(literals);
    }

    public void addLeftParent(Resolution resolution) {
        leftParent = resolution;
    }

    public void addRightParent(Resolution resolution) {
        rightParent = resolution;
    }

    public Resolution getLeftParent() {
        return leftParent;
    }

    public Resolution getRightParent() {
        return rightParent;
    }

    public void setEntryFromParentsAndRemoveLiteral(SingleLiteralDisjunction complementaryLiteral) {
        entry = CombineUtils.combineBy(leftParent.entry, rightParent.entry, complementaryLiteral.get());
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
