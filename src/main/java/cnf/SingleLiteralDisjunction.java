package cnf;

public class SingleLiteralDisjunction extends Disjunction {
    static SingleLiteralDisjunction TRUE = new SingleLiteralDisjunction(Integer.MAX_VALUE);
    static SingleLiteralDisjunction FALSE = new SingleLiteralDisjunction(Integer.MIN_VALUE);

    private int literal;

    public SingleLiteralDisjunction(Integer value) {
        super(value);
        literal = value;
    }

    SingleLiteralDisjunction(Disjunction toCopy) {
        super(toCopy);
        if (!toCopy.hasUnitSize()) {
            throw new UnsupportedOperationException("SingleLiteralClause must contain single literal");
        }
        literal = toCopy.getFirst().get();
    }

    public int get() {
        return literal;
    }

    public SingleLiteralDisjunction negate() {
        SingleLiteralDisjunction result = new SingleLiteralDisjunction(-literal);
        result.res = this.res;
        return result;
    }

    @Override
    public SingleLiteralDisjunction setAsSynthetic() {
        return (SingleLiteralDisjunction) super.setAsSynthetic();
    }

    @Override
    public boolean hasUnitSize() {
        return true;
    }

    @Override
    public String toString() {
        if (this == FALSE) return "FALSE";
        if (this == TRUE) return "TRUE";
        return super.toString();
    }
}
