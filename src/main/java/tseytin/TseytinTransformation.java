package tseytin;

import antlr.generated.CNFLexer;
import antlr.generated.CNFParser;
import cnf.CNF;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class TseytinTransformation {
    public static CNF transform(String str) {
        CharStream inputStream = CharStreams.fromString(str);
        CNFLexer markupLexer = new CNFLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(markupLexer);
        CNFParser markupParser = new CNFParser(commonTokenStream);
        FormulaVisitor visitor = new FormulaVisitor();
        return visitor.parse(markupParser.cnf());
    }
}
