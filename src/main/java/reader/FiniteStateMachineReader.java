package reader;

import cnf.CNF;
import model_checking.FiniteStateMachine;
import tseytin.TseytinTransformation;
import util.IDPool;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Parser for finite state machine. Structure:
 * <ul>
 *  <li>Set of space-separated propositional variables</li>
 *  <li>Init condition cnf</li>
 *  <li>Transition cnf in format T(x, y, z, ..., x', y', z', ...)</li>
 *  <li>Error cnf</li>
 * </ul>
 */
public class FiniteStateMachineReader {
    public static FiniteStateMachine readFromFile(File file) throws IOException {
        return deserialize(ReaderUtil.readFromFile(file));
    }

    public static FiniteStateMachine readFromString(String str) {
        return deserialize(ReaderUtil.readFromString(str));
    }

    private static FiniteStateMachine deserialize(List<String> lines) {
        assert lines.size() == 4 : "Finite state machine file must contains 4 different lines. See reader documentation.";
        String[] vars = lines.get(0).split(" ");

        IDPool pool = new IDPool();
        Arrays.stream(vars).forEach(pool::idByName);

        CNF init = TseytinTransformation.transform(lines.get(1), pool);
        String transition = lines.get(2);
        CNF err = TseytinTransformation.transform(lines.get(3), pool);

        return new FiniteStateMachine(vars, init, transition, err, pool);
    }
}
