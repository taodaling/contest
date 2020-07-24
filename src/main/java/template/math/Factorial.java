package template.math;

/**
 * Factorial
 */
public class Factorial {
    int[] fact;
    int[] inv;
    Modular mod;

    public Modular getMod() {
        return mod;
    }

    public Factorial(int[] fact, int[] inv, Power pow) {
        this.mod = pow.getModular();
        this.fact = fact;
        this.inv = inv;
        fact[0] = inv[0] = 1;
        int n = Math.min(fact.length, pow.getModular().getMod());
        for (int i = 1; i < n; i++) {
            fact[i] = i;
            fact[i] = mod.mul(fact[i], fact[i - 1]);
        }
        inv[n - 1] = pow.inverse(fact[n - 1]);
        for (int i = n - 2; i >= 1; i--) {
            inv[i] = mod.mul(inv[i + 1], i + 1);
        }
    }

    public Factorial(int limit, Power pow) {
        this(new int[limit + 1], new int[limit + 1], pow);
    }

    public int fact(int n) {
        return fact[n];
    }

    public int invFact(int n) {
        return inv[n];
    }
}
