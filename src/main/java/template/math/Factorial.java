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
        for (int i = 1; i < fact.length; i++) {
            fact[i] = i;
            fact[i] = mod.mul(fact[i], fact[i - 1]);
        }
        inv[inv.length - 1] = pow.inverse(fact[inv.length - 1]);
        for (int i = inv.length - 2; i >= 1; i--) {
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
