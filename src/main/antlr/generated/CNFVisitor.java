// Generated from E:/!PROJECTS/IntelliJ_IDEA/dpll/src/main/antlr\CNF.g4 by ANTLR 4.7.2
package generated;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CNFParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CNFVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code not}
	 * labeled alternative in {@link CNFParser#cnf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(CNFParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conjunction}
	 * labeled alternative in {@link CNFParser#cnf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(CNFParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierAtom}
	 * labeled alternative in {@link CNFParser#cnf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierAtom(CNFParser.IdentifierAtomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code disjunction}
	 * labeled alternative in {@link CNFParser#cnf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(CNFParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code implication}
	 * labeled alternative in {@link CNFParser#cnf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplication(CNFParser.ImplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesized}
	 * labeled alternative in {@link CNFParser#cnf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesized(CNFParser.ParenthesizedContext ctx);
}