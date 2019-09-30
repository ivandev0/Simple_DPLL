package tseytin;

import antlr.generated.CNFLexer;
import antlr.generated.CNFParser;
import cnf.CNF;
import cnf.Disjunction;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class FormulaVisitorTest {
    private CNF getCNF(String str) {
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

        CNF expected = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(-3, 1, 2));
            add(new Disjunction(-1, 3));
            add(new Disjunction(-2, 3));
        }});
        Assert.assertEquals(expected, cnf);
    }
}