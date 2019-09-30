package tseytin;

import cnf.CNF;
import cnf.Disjunction;
import antlr.generated.CNFLexer;
import antlr.generated.CNFParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormulaVisitorTest {
    public CNF getCNF(String str) {
        CharStream inputStream = CharStreams.fromString(str);
        CNFLexer markupLexer = new CNFLexer(inputStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(markupLexer);
        CNFParser markupParser = new CNFParser(commonTokenStream);
        FormulaVisitor visitor = new FormulaVisitor();
        return visitor.parse(markupParser.cnf());
    }

    @Test
    public void parseTest() {
        CNF cnf = getCNF("p1 v p2");

        System.out.println(cnf);
    }
}