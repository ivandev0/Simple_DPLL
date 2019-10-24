package util;

import cnf.SingleLiteralDisjunction;

import java.util.HashSet;
import java.util.Set;

public class CombineUtils {
    public static Integer getComplementaryLiteral(Set<Integer> first, Set<Integer> second) {
        for (Integer leftSetLiteral : first) {
            for (Integer rightSetLiteral : second) {
                if (leftSetLiteral == -rightSetLiteral) {
                    return leftSetLiteral;
                }
            }
        }

        return null;
    }

    public static Set<Integer> combineBy(Set<Integer> first, Set<Integer> second, Integer literal) {
        Set<Integer> result = new HashSet<>(first);
        result.addAll(second);
        result.remove(literal);
        result.remove(-literal);
        return result;
    }
}
