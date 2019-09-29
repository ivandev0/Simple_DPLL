package reader;

import cnf.CNF;
import cnf.Disjunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class DimacsReader {
    public static CNF readFromFile(File file) throws IOException {
        BufferedReader br = Files.newBufferedReader(file.toPath());

        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }

        return deserialize(lines);
    }

    public static CNF readFromString(String str) {
        StringTokenizer st = new StringTokenizer(str, "\n");

        List<String> lines = new ArrayList<>();
        while (st.hasMoreTokens()) {
            lines.add(st.nextToken());
        }

        return deserialize(lines);
    }

    private static CNF deserialize(List<String> lines) {
        List<Disjunction> clauses = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("c") || line.startsWith("p")) {
                continue;
            }

            Disjunction clause = new Disjunction();
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                int literal = Integer.parseInt(st.nextToken());
                if (literal != 0) {
                    clause.addLiteral(literal);
                } else {
                    clauses.add(clause);
                    break;
                }
            }
        }

        return new CNF(clauses);
    }
}
