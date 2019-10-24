package cnf;

public class SingleLiteralDisjunction extends Disjunction {
    public static class TRUE {}
    public static class FALSE {}

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
        literal = toCopy.getFirst();
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
    public void addLiteral(Integer literal) {
        throw new UnsupportedOperationException("SingleLiteralDisjunction must contain only one literal");
    }
}
