package model_checking;

import cnf.CNF;
import dpll.DPLL;
import dpll.Model;
import resolution.interpolation.Interpolation;

public class ModelChecking {
    /**
     * @return true if unsafe, false otherwise
     */
    public static boolean infiniteRun(FiniteStateMachine machine) {
        //if I ∧ F is satisfiable, return True
        Model model = DPLL.solve(new CNF(machine.getInit(), machine.getErr()));
        if (model != null) {
            return true;
        }

        int k = 1;
        while (true) {
            Boolean result = finiteRun(machine, k);
            if (result != null) return result;
            k++;
        }
    }

    /**
     * @return true if unsafe, false otherwise
     */
    public static Boolean finiteRun(FiniteStateMachine machine, int k) {
        if (DPLL.solve(machine.getBoundedModel(k)) != null) return true;

        CNF R = machine.getInit();
        while (true) {
            machine.setInit(R);
            CNF pref = machine.getPref();
            CNF suff = machine.getSuff(k);

            Model full = DPLL.solve(new CNF(pref, suff));
            if (full != null) {
                //satisfiable
                return null;
            } else {
                //not satisfiable
                CNF interpolation = Interpolation.calculate(pref, suff, machine.getPool());
                CNF inverseImplication = new CNF(interpolation, R.inverse(machine.getPool()));
                if (DPLL.solve(inverseImplication) == null) {
                    return false;
                }
                R = R.disjunction(interpolation);
            }
        }
    }
}
