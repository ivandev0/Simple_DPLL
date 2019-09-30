package tseytin;

import cnf.CNF;
import cnf.Disjunction;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class FormulaVisitorTest {
    @Test
    public void parseDisjunctionTest() {
        CNF cnf = TseytinTransformation.transform("p1 v p2");

        CNF expected = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(-3, 1, 2));
            add(new Disjunction(-1, 3));
            add(new Disjunction(-2, 3));
            add(new Disjunction(3));
        }});
        Assert.assertEquals(expected, cnf);
    }

    @Test
    public void parseConjunctionTest() {
        CNF cnf = TseytinTransformation.transform("p1 ^ p2");

        CNF expected = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(3, -1, -2));
            add(new Disjunction(1, -3));
            add(new Disjunction(2, -3));
            add(new Disjunction(3));
        }});
        Assert.assertEquals(expected, cnf);
    }

    @Test
    public void parseImplicationTest() {
        CNF cnf = TseytinTransformation.transform("p1 -> p2");

        CNF expected = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(-3, -1, 2));
            add(new Disjunction(2, 3));
            add(new Disjunction(-1, 3));
            add(new Disjunction(3));
        }});
        Assert.assertEquals(expected, cnf);
    }

    @Test
    public void parseNotTest() {
        CNF cnf = TseytinTransformation.transform("!p1");

        CNF expected = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(-1));
        }});
        Assert.assertEquals(expected, cnf);
    }

    @Test
    public void parseTaskFromClassTest() {
        CNF cnf = TseytinTransformation.transform("!(q1 ^ (q2 v !q3))");

        CNF expected = new CNF(new ArrayList<Disjunction>() {{
            add(new Disjunction(-4, 2, -3));
            add(new Disjunction(-2, 4));
            add(new Disjunction(3, 4));
            add(new Disjunction(5, -1, -4));
            add(new Disjunction(1, -5));
            add(new Disjunction(4, -5));
            add(new Disjunction(-5));
        }});
        Assert.assertEquals(expected, cnf);
    }
}