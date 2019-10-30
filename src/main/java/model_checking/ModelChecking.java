package model_checking;

import cnf.CNF;
import dpll.DPLL;
import dpll.Model;
import resolution.interpolation.Interpolation;

public class ModelChecking {
    public static boolean SKIP_CHECKS = true;
    /**
     * @return <code>true</code> if unsafe;
     *         <code>false</code> otherwise;
     */
    public static boolean infiniteRun(FiniteStateMachine machine) {
        //if I ∧ F is satisfiable, return True
        Model model = DPLL.solve(machine.getBoundedModel(0));
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
     * @return <code>true</code> if unsafe;
     *         <code>false</code> if safe;
     *         <code>null</code> if unknown, need further investigation;
     */
    public static Boolean finiteRun(FiniteStateMachine machine, int k) {
        if (DPLL.solve(machine.getBoundedModel(k)) != null) return true;

        CNF R = machine.getInit();
        CNF oldInit = new CNF(machine.getInit());
        while (true) {
            machine.setInit(R);
            CNF pref = machine.getPref();
            CNF suff = machine.getSuff(k);

            assert SKIP_CHECKS || DPLL.solve(pref) != null : "Pref must be satisfiable";
            Model full = DPLL.solve(new CNF(pref, suff));
            if (full != null) {
                //satisfiable
                machine.setInit(oldInit);
                return null;
            } else {
                //not satisfiable
                CNF interpolation = Interpolation.calculate(pref, suff);
                assert SKIP_CHECKS || DPLL.solve(pref.inverse(machine.getPool()).disjunction(interpolation)) != null : "Perf must implies interpolation";
                assert SKIP_CHECKS || DPLL.solve(new CNF(interpolation, suff)) == null : "Suff must not be satisfiable with interpolation";

                machine.replaceBackward(interpolation, k);

                //check that R' -> R is not satisfiable
                CNF inverseImplication = new CNF(interpolation, R.inverse(machine.getPool()));
                if (DPLL.solve(inverseImplication) == null) {
                    return false;
                }
                R = R.disjunction(interpolation).removeDuplicates();
                assert SKIP_CHECKS || DPLL.solve(R) != null : "R must always be satisfiable";
            }
        }
    }
}
