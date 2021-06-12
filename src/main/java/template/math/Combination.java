package template.math;

/**
 * Composition
 */
public class Combination implements IntCombination {
    final Factorial factorial;
    int modVal;
    Modular modular;

    public Combination(Factorial factorial) {
        this.factorial = factorial;
        this.modVal = factorial.getMod();
        this.modular = new Modular(modVal);
    }


    public Combination(int limit, int mod) {
        this(new Factorial(limit, mod));
    }

    public int combination(int m, int n) {
        if (n > m || n < 0) {
            return 0;
        }
        return modular.mul(factorial.fact(m), factorial.invFact(n), factorial.invFact(m - n));
    }

    /**
     * return 1 / composite(m, n)
     */
    public int invCombination(int m, int n) {
        return modular.mul(factorial.invFact(m), factorial.fact(n), factorial.fact(m - n));
    }
}
