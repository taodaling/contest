package template.math;

/**
 * Composition
 */
public class Combination {
    final Factorial factorial;
    final Modular modular;

    public Combination(Factorial factorial) {
        this.factorial = factorial;
        this.modular = factorial.getModular();
    }


    public Combination(int limit, Modular modular) {
        this(new Factorial(limit, modular));
    }

    public int combination(int m, int n) {
        if (n > m) {
            return 0;
        }
        return modular.mul(modular.mul(factorial.fact(m), factorial.invFact(n)), factorial.invFact(m - n));
    }

    /**
     * return 1 / composite(m, n)
     */
    public int invCombination(int m, int n) {
        return modular.mul(modular.mul(factorial.invFact(m), factorial.fact(n)), factorial.fact(m - n));
    }
}
