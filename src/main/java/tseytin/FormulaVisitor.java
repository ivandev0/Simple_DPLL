package tseytin;

import cnf.CNF;
import cnf.Disjunction;
import antlr.generated.CNFBaseVisitor;
import antlr.generated.CNFParser;
import util.IDPool;

import java.util.ArrayList;
import java.util.List;

public class FormulaVisitor extends CNFBaseVisitor<Integer> {
    private List<Disjunction> disjunctions;
    private IDPool pool;

    CNF parse(CNFParser.CnfContext ctx, IDPool pool) {
        this.pool = pool;
        disjunctions = new ArrayList<>();
        int mainVar = visit(ctx);
        disjunctions.add(new Disjunction(mainVar));
        return new CNF(disjunctions);
    }

    @Override
    public Integer visitIdentifierAtom(CNFParser.IdentifierAtomContext ctx) {
        return pool.idByName(ctx.IDENTIFIER().toString());
    }

    @Override
    public Integer visitNot(CNFParser.NotContext ctx) {
        Integer node = visit(ctx.cnf());
        return -node;
    }

    @Override
    public Integer visitConjunction(CNFParser.ConjunctionContext ctx) {
        Integer left = visit(ctx.cnf(0));
        Integer right = visit(ctx.cnf(1));
        Integer additionalVar = pool.getTempVar();

        disjunctions.add(new Disjunction(additionalVar, -left, -right));
        disjunctions.add(new Disjunction(left, -additionalVar));
        disjunctions.add(new Disjunction(right, -additionalVar));

        return additionalVar;
    }

    @Override
    public Integer visitDisjunction(CNFParser.DisjunctionContext ctx) {
        Integer left = visit(ctx.cnf(0));
        Integer right = visit(ctx.cnf(1));
        Integer additionalVar = pool.getTempVar();

        disjunctions.add(new Disjunction(-additionalVar, left, right));
        disjunctions.add(new Disjunction(-left, additionalVar));
        disjunctions.add(new Disjunction(-right, additionalVar));

        return additionalVar;
    }

    @Override
    public Integer visitImplication(CNFParser.ImplicationContext ctx) {
        Integer left = visit(ctx.cnf(0));
        Integer right = visit(ctx.cnf(1));
        Integer additionalVar = pool.getTempVar();

        disjunctions.add(new Disjunction(-additionalVar, -left, right));
        disjunctions.add(new Disjunction(right, additionalVar));
        disjunctions.add(new Disjunction(-left, additionalVar));

        return additionalVar;
    }

    @Override
    public Integer visitEquivalence(CNFParser.EquivalenceContext ctx) {
        Integer left = visit(ctx.cnf(0));
        Integer right = visit(ctx.cnf(1));
        Integer additionalVar = pool.getTempVar();

        disjunctions.add(new Disjunction(additionalVar, left, -right));
        disjunctions.add(new Disjunction(additionalVar, -left, right));
        disjunctions.add(new Disjunction(additionalVar, -left, -right));
        disjunctions.add(new Disjunction(-additionalVar, left, right));
        disjunctions.add(new Disjunction(-additionalVar, left, -right));
        disjunctions.add(new Disjunction(-additionalVar, -left, right));

        return additionalVar;
    }

    @Override
    public Integer visitParenthesized(CNFParser.ParenthesizedContext ctx) {
        return visit(ctx.cnf());
    }
}
