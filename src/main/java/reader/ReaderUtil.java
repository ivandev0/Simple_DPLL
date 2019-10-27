package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

class ReaderUtil {
    static List<String> readFromFile(File file) throws IOException {
        BufferedReader br = Files.newBufferedReader(file.toPath());
        return br.lines().collect(Collectors.toList());
    }

    static List<String> readFromString(String str) {
        StringTokenizer st = new StringTokenizer(str, "\n");

        List<String> lines = new ArrayList<>();
        while (st.hasMoreTokens()) {
            lines.add(st.nextToken());
        }

        return lines;
    }
}
