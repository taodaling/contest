package template.math;

/**
 * Factorial
 */
public class Factorial {
    int[] fact;
    int[] inv;
    Modular modular;

    public Modular getModular() {
        return modular;
    }

    public Factorial(int[] fact, int[] inv, InverseNumber in, int limit, Modular modular) {
        this.modular = modular;
        this.fact = fact;
        this.inv = inv;
        fact[0] = inv[0] = 1;
        for (int i = 1; i <= limit; i++) {
            fact[i] = modular.mul(fact[i - 1], i);
            inv[i] = modular.mul(inv[i - 1], in.inv[i]);
        }
    }

    public Factorial(int limit, Modular modular) {
        this(new int[limit + 1], new int[limit + 1], new InverseNumber(limit, modular), limit, modular);
    }

    public int fact(int n) {
        return fact[n];
    }

    public int invFact(int n) {
        return inv[n];
    }
}
