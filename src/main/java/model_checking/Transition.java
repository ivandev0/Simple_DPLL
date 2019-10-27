package model_checking;

import cnf.CNF;
import util.IDPool;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class Transition {
    private CNF transition;
    Map<Integer, Integer> atomsToNext;

    public Transition(CNF transition, Map<String, String> atomsToNext, IDPool pool) {
        this.transition = transition;
        this.atomsToNext = atomsToNext.entrySet().stream().collect(Collectors.toMap(
                e -> pool.idByName(e.getKey()),
                e -> pool.idByName(e.getValue())
        ));
    }

    public Transition(CNF transition, Map<Integer, Integer> atomsToNext) {
        this.transition = transition;
        this.atomsToNext = atomsToNext;
    }

    public CNF getCnf() {
        return new CNF(transition);
    }

    public Transition getNext(IDPool pool) {
        CNF newTransition = new CNF(transition);

        Map<Integer, Integer> newMap = new HashMap<>();
        for (Integer value : atomsToNext.values()) {
            newMap.put(value, pool.getTempVar());
        }

        //v' -> v''
        newMap.forEach(newTransition::replaceInPlace);

        //v -> v'
        atomsToNext.forEach(newTransition::replaceInPlace);

        return new Transition(newTransition, newMap);
    }
}
