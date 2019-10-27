package model_checking;

import cnf.CNF;
import util.IDPool;

import java.util.Map;

public class FiniteStateMachine {
    private CNF init;
    private Transition transition;
    private CNF err;
    private IDPool pool;

    public FiniteStateMachine(CNF init, Transition transition, CNF err, IDPool pool) {
        this.init = init;
        this.transition = transition;
        this.err = err;
        this.pool = pool;
    }

    CNF getInit() {
        return init;
    }

    void setInit(CNF init) {
        this.init = init;
    }

    public Transition getTransition() {
        return transition;
    }

    CNF getErr() {
        return err;
    }

    public IDPool getPool() {
        return pool;
    }

    CNF getBoundedModel(int k) {
        return new CNF(getPref(), getSuff(k));
    }

    CNF getPref() {
        return new CNF(init, transition.getCnf());
    }

    CNF getSuff(int k) {
        CNF cnf = new CNF();
        Transition next = transition;
        CNF newErr = new CNF(err);
        for (int i = 1; i < k; i++) {
            for (Map.Entry<Integer, Integer> entry : next.atomsToNext.entrySet()) {
                newErr.replaceInPlace(entry.getKey(), entry.getValue());
            }
            next = transition.getNext(pool);
            cnf = new CNF(cnf, next.getCnf());
        }
        for (Map.Entry<Integer, Integer> entry : next.atomsToNext.entrySet()) {
            newErr.replaceInPlace(entry.getKey(), entry.getValue());
        }

        return new CNF(cnf, newErr);
    }

}
