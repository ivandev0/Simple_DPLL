package model_checking;

import cnf.CNF;
import util.IDPool;

import java.util.Arrays;
import java.util.List;

public class FiniteStateMachine {
    private String[] vars;
    private CNF init;
    private CNF transition;
    private CNF err;
    private IDPool pool;

    public FiniteStateMachine(String[] vars, CNF init, CNF transition, CNF err, IDPool pool) {
        this.vars = vars;
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

    public CNF getTransition() {
        return transition;
    }

    CNF getErr() {
        return err;
    }

    public IDPool getPool() {
        return pool;
    }

    CNF getBoundedModel(int k) {
        if (k == 0) {
            return new CNF(init, err);
        }
        return new CNF(getPref(), getSuff(k));
    }

    CNF getPref() {
        return new CNF(init, transition);
    }

    CNF getSuff(int k) {
        List<String> currentVars = Arrays.asList(vars);
        CNF cnf = new CNF();
        CNF next;
        CNF newErr = new CNF(err);
        for (int i = 1; i < k; i++) {
            currentVars.forEach(it -> newErr.replaceInPlace(pool.idByName(it), pool.idByName(it + "'")));
            next = getNext(currentVars);
            cnf = new CNF(cnf, next);
        }
        currentVars.forEach(it -> newErr.replaceInPlace(pool.idByName(it), pool.idByName(it + "'")));

        return new CNF(cnf, newErr);
    }

    private CNF getNext(List<String> currentVars) {
        CNF newTransition = new CNF(transition);

        //v' -> v''
        currentVars.forEach(it -> newTransition.replaceInPlace(pool.idByName(it + "'"), pool.idByName(it + "''")));
        //v -> v'
        currentVars.forEach(it -> newTransition.replaceInPlace(pool.idByName(it), pool.idByName(it + "'")));

        currentVars.forEach(it -> it += "'");

        return newTransition;
    }
}
