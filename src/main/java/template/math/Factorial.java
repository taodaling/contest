package template.math;

import java.math.BigInteger;

/**
 * Factorial
 */
public class Factorial {
    int[] fact;
    int[] inv;
    int mod;

    public int getMod() {
        return mod;
    }


    public Factorial(int[] fact, int[] inv, int mod) {
        this.mod = mod;
        this.fact = fact;
        this.inv = inv;
        fact[0] = inv[0] = 1;
        int n = Math.min(fact.length, mod);
        for (int i = 1; i < n; i++) {
            fact[i] = i;
            fact[i] = (int) ((long) fact[i] * fact[i - 1] % mod);
        }
        if (n - 1 >= 0) {
            inv[n - 1] = BigInteger.valueOf(fact[n - 1]).modInverse(BigInteger.valueOf(mod)).intValue();
        }
        for (int i = n - 2; i >= 1; i--) {
            inv[i] = (int) ((long) inv[i + 1] * (i + 1) % mod);
        }
    }

    public Factorial(int limit, int mod) {
        this(new int[Math.min(limit + 1, mod)], new int[Math.min(limit + 1, mod)], mod);
    }

    public int fact(int n) {
        if (n >= mod) {
            return 0;
        }
        return fact[n];
    }

    public int invFact(int n) {
        if (n >= mod) {
            throw new IllegalArgumentException();
        }
        return inv[n];
    }
}
