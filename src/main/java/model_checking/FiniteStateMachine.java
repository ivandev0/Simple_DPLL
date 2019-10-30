package model_checking;

import cnf.CNF;
import tseytin.TseytinTransformation;
import util.IDPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiniteStateMachine {
    private String[] vars;
    private CNF init;
    private String transition;
    private CNF err;
    private IDPool pool;

    private List<CNF> transitionCache = new ArrayList<>();

    public FiniteStateMachine(String[] vars, CNF init, String transition, CNF err, IDPool pool) {
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

    public IDPool getPool() {
        return pool;
    }

    private CNF getTransition(int i) {
        if (transitionCache.size() > i) {
            return transitionCache.get(i);
        }

        CNF cnf = replaceForward(TseytinTransformation.transform(transition, pool), i);
        transitionCache.add(cnf);
        return cnf;
    }

    CNF getBoundedModel(int k) {
        if (k == 0) {
            return new CNF(init, err);
        }
        return new CNF(getPref(), getSuff(k));
    }

    CNF getPref() {
        return new CNF(init, getTransition(0));
    }

    CNF getSuff(int k) {
        CNF cnf = new CNF();
        for (int i = 1; i < k; i++) {
            cnf = new CNF(cnf, getTransition(i));
        }

        return new CNF(cnf, replaceForward(new CNF(err), k));
    }

    private CNF replaceForward(CNF cnf, int k) {
        //v' -> v''
        for (int i = 0; i < k; i++) {
            String quote = new String(new char[i + 1]).replace("\0", "'");
            Arrays.stream(vars).forEach(it -> cnf.replaceInPlace(pool.idByName(it + quote), pool.idByName(it + quote + "'")));
        }
        //v -> v'
        for (int i = 0; i < k; i++) {
            String quote = new String(new char[i]).replace("\0", "'");
            Arrays.stream(vars).forEach(it -> cnf.replaceInPlace(pool.idByName(it + quote), pool.idByName(it + quote + "'")));
        }
        return cnf;
    }

    CNF replaceBackward(CNF cnf, int k) {
        //v' -> v
        while (k != 0) {
            String sym = new String(new char[k]).replace("\0", "'");
            Arrays.stream(vars).forEach(it -> cnf.replaceInPlace(pool.idByName(it + sym), pool.idByName(it)));
            k--;
        }
        return cnf;
    }
}
