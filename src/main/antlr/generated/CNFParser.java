// Generated from E:/!PROJECTS/IntelliJ_IDEA/dpll/src/main/antlr\CNF.g4 by ANTLR 4.7.2
package generated;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CNFParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, IDENTIFIER=8, 
		WS=9;
	public static final int
		RULE_cnf = 0;
	private static String[] makeRuleNames() {
		return new String[] {
			"cnf"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'!'", "'v'", "'^'", "'->'", "'<->'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "IDENTIFIER", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "CNF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CNFParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class CnfContext extends ParserRuleContext {
		public CnfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cnf; }
	 
		public CnfContext() { }
		public void copyFrom(CnfContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NotContext extends CnfContext {
		public CnfContext cnf() {
			return getRuleContext(CnfContext.class,0);
		}
		public NotContext(CnfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CNFVisitor ) return ((CNFVisitor<? extends T>)visitor).visitNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EquivalenceContext extends CnfContext {
		public List<CnfContext> cnf() {
			return getRuleContexts(CnfContext.class);
		}
		public CnfContext cnf(int i) {
			return getRuleContext(CnfContext.class,i);
		}
		public EquivalenceContext(CnfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CNFVisitor ) return ((CNFVisitor<? extends T>)visitor).visitEquivalence(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConjunctionContext extends CnfContext {
		public List<CnfContext> cnf() {
			return getRuleContexts(CnfContext.class);
		}
		public CnfContext cnf(int i) {
			return getRuleContext(CnfContext.class,i);
		}
		public ConjunctionContext(CnfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CNFVisitor ) return ((CNFVisitor<? extends T>)visitor).visitConjunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierAtomContext extends CnfContext {
		public TerminalNode IDENTIFIER() { return getToken(CNFParser.IDENTIFIER, 0); }
		public IdentifierAtomContext(CnfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CNFVisitor ) return ((CNFVisitor<? extends T>)visitor).visitIdentifierAtom(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DisjunctionContext extends CnfContext {
		public List<CnfContext> cnf() {
			return getRuleContexts(CnfContext.class);
		}
		public CnfContext cnf(int i) {
			return getRuleContext(CnfContext.class,i);
		}
		public DisjunctionContext(CnfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CNFVisitor ) return ((CNFVisitor<? extends T>)visitor).visitDisjunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ImplicationContext extends CnfContext {
		public List<CnfContext> cnf() {
			return getRuleContexts(CnfContext.class);
		}
		public CnfContext cnf(int i) {
			return getRuleContext(CnfContext.class,i);
		}
		public ImplicationContext(CnfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CNFVisitor ) return ((CNFVisitor<? extends T>)visitor).visitImplication(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesizedContext extends CnfContext {
		public CnfContext cnf() {
			return getRuleContext(CnfContext.class,0);
		}
		public ParenthesizedContext(CnfContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CNFVisitor ) return ((CNFVisitor<? extends T>)visitor).visitParenthesized(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CnfContext cnf() throws RecognitionException {
		return cnf(0);
	}

	private CnfContext cnf(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		CnfContext _localctx = new CnfContext(_ctx, _parentState);
		CnfContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_cnf, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				_localctx = new IdentifierAtomContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(3);
				match(IDENTIFIER);
				}
				break;
			case T__0:
				{
				_localctx = new ParenthesizedContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(4);
				match(T__0);
				setState(5);
				cnf(0);
				setState(6);
				match(T__1);
				}
				break;
			case T__2:
				{
				_localctx = new NotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(8);
				match(T__2);
				setState(9);
				cnf(5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(26);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(24);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new DisjunctionContext(new CnfContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cnf);
						setState(12);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(13);
						match(T__3);
						setState(14);
						cnf(5);
						}
						break;
					case 2:
						{
						_localctx = new ConjunctionContext(new CnfContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cnf);
						setState(15);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(16);
						match(T__4);
						setState(17);
						cnf(4);
						}
						break;
					case 3:
						{
						_localctx = new ImplicationContext(new CnfContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cnf);
						setState(18);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(19);
						match(T__5);
						setState(20);
						cnf(3);
						}
						break;
					case 4:
						{
						_localctx = new EquivalenceContext(new CnfContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cnf);
						setState(21);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(22);
						match(T__6);
						setState(23);
						cnf(2);
						}
						break;
					}
					} 
				}
				setState(28);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return cnf_sempred((CnfContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean cnf_sempred(CnfContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 2);
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\13 \4\2\t\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2\r\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\7\2\33\n\2\f\2\16\2\36\13\2\3\2\2\3\2\3\2\2\2\2$\2\f\3"+
		"\2\2\2\4\5\b\2\1\2\5\r\7\n\2\2\6\7\7\3\2\2\7\b\5\2\2\2\b\t\7\4\2\2\t\r"+
		"\3\2\2\2\n\13\7\5\2\2\13\r\5\2\2\7\f\4\3\2\2\2\f\6\3\2\2\2\f\n\3\2\2\2"+
		"\r\34\3\2\2\2\16\17\f\6\2\2\17\20\7\6\2\2\20\33\5\2\2\7\21\22\f\5\2\2"+
		"\22\23\7\7\2\2\23\33\5\2\2\6\24\25\f\4\2\2\25\26\7\b\2\2\26\33\5\2\2\5"+
		"\27\30\f\3\2\2\30\31\7\t\2\2\31\33\5\2\2\4\32\16\3\2\2\2\32\21\3\2\2\2"+
		"\32\24\3\2\2\2\32\27\3\2\2\2\33\36\3\2\2\2\34\32\3\2\2\2\34\35\3\2\2\2"+
		"\35\3\3\2\2\2\36\34\3\2\2\2\5\f\32\34";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}