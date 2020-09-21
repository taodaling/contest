package template.math;

/**
 * Composition
 */
public class Combination implements IntCombination {
    final Factorial factorial;
    int modVal;

    public Combination(Factorial factorial) {
        this.factorial = factorial;
        this.modVal = factorial.getMod().getMod();
    }


    public Combination(int limit, Power pow) {
        this(new Factorial(limit, pow));
    }

    public int combination(int m, int n) {
        if (n > m || n < 0) {
            return 0;
        }
        return (int) ((long) factorial.fact(m) * factorial.invFact(n) % modVal * factorial.invFact(m - n) % modVal);
    }

    /**
     * return 1 / composite(m, n)
     */
    public int invCombination(int m, int n) {
        return (int) ((long) factorial.invFact(m) * factorial.fact(n) % modVal * factorial.fact(m - n) % modVal);
    }
}
