package template.math;

/**
 * Composition
 */
public class PrimeCombination implements IntCombination{
    final Factorial factorial;
    final Modular modular;

    public PrimeCombination(Factorial factorial) {
        this.factorial = factorial;
        this.modular = factorial.getModular();
    }


    public PrimeCombination(int limit, Modular modular) {
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
