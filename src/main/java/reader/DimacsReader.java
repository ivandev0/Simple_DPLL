package reader;

import cnf.CNF;
import cnf.Disjunction;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DimacsReader {
    public static CNF readFromFile(File file) throws IOException {
        return deserialize(ReaderUtil.readFromFile(file));
    }

    public static CNF readFromString(String str) {
        return deserialize(ReaderUtil.readFromString(str));
    }

    private static CNF deserialize(List<String> lines) {
        List<Disjunction> clauses = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("c") || line.startsWith("p")) {
                continue;
            }

            Set<Integer> clause = new HashSet<>();
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                int literal = Integer.parseInt(st.nextToken());
                if (literal != 0) {
                    clause.add(literal);
                } else {
                    clauses.add(new Disjunction(clause));
                    break;
                }
            }
        }

        return new CNF(clauses);
    }
}
