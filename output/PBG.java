
public class PBG {
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);
    PrimeCombination combination = new PrimeCombination(10000, mod);
    int neg2 = power.inverseByFermat(2);

    public int findEV(int P, int B, int G) {
        int n = P + B + G;
        int sum = mod.valueOf((1 + n) * n / 2);
        if (G == 0) {
            return expect(P, B);
        }
        if (B == 0) {
            int EG = expect(G, P);
            int EP = mod.mul(mod.subtract(sum, mod.mul(G, EG)), power.inverseByFermat(P));
            return EP;
        }
        int EG = expect(G, B + P);
        int EG2 = expect(G + P, B);
        int EB = mod.mul(mod.subtract(sum, mod.mul(G + P, EG2)), power.inverseByFermat(B));
        int EP = mod.subtract(sum, mod.mul(G, EG));
        EP = mod.subtract(EP, mod.mul(B, EB));
        EP = mod.mul(EP, power.inverseByFermat(P));
        return EP;
    }

    public int expect(int P, int B) {
        int[][] dp = new int[P + B + 1][P + 1];
        dp[P + B][P] = 1;
        int exp = 0;
        for (int i = P + B; i >= 2; i--) {
            for (int j = P; j >= 1; j--) {
                if (dp[i][j] == 0) {
                    continue;
                }
                //P with P
                // A die
                int prob1 = mod.mul(neg2, mod.mul(combination.combination(j - 1, 1), combination.invCombination(i, 2)));

                //P with P and A survive
                int prob2 = mod.mul(combination.combination(j, 2), combination.invCombination(i, 2));
                prob2 = mod.subtract(prob2, prob1);

                //else
                int prob3 = mod.subtract(1, prob1);
                prob3 = mod.subtract(prob3, prob2);

                exp = mod.plus(exp, mod.mul(i, mod.mul(dp[i][j], prob1)));
                dp[i - 1][j - 1] = mod.plus(dp[i - 1][j - 1], mod.mul(prob2, dp[i][j]));
                dp[i - 1][j] = mod.plus(dp[i - 1][j], mod.mul(prob3, dp[i][j]));
            }
        }

        exp = mod.plus(exp, mod.mul(dp[1][1], 1));
        return exp;
    }

}

class Factorial {
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

class PrimeCombination implements IntCombination {
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

    public int invCombination(int m, int n) {
        return modular.mul(modular.mul(factorial.invFact(m), factorial.fact(n)), factorial.fact(m - n));
    }

}

class Power {
    final Modular modular;

    public Power(Modular modular) {
        this.modular = modular;
    }

    public int pow(int x, int n) {
        if (n == 0) {
            return modular.valueOf(1);
        }
        long r = pow(x, n >> 1);
        r = modular.valueOf(r * r);
        if ((n & 1) == 1) {
            r = modular.valueOf(r * x);
        }
        return (int) r;
    }

    public int inverseByFermat(int x) {
        return pow(x, modular.m - 2);
    }

}

class InverseNumber {
    int[] inv;

    public InverseNumber(int[] inv, int limit, Modular modular) {
        this.inv = inv;
        inv[1] = 1;
        int p = modular.getMod();
        for (int i = 2; i <= limit; i++) {
            int k = p / i;
            int r = p % i;
            inv[i] = modular.mul(-k, inv[r]);
        }
    }

    public InverseNumber(int limit, Modular modular) {
        this(new int[limit + 1], limit, modular);
    }

}

class Modular {
    int m;

    public int getMod() {
        return m;
    }

    public Modular(int m) {
        this.m = m;
    }

    public Modular(long m) {
        this.m = (int) m;
        if (this.m != m) {
            throw new IllegalArgumentException();
        }
    }

    public Modular(double m) {
        this.m = (int) m;
        if (this.m != m) {
            throw new IllegalArgumentException();
        }
    }

    public int valueOf(int x) {
        x %= m;
        if (x < 0) {
            x += m;
        }
        return x;
    }

    public int valueOf(long x) {
        x %= m;
        if (x < 0) {
            x += m;
        }
        return (int) x;
    }

    public int mul(int x, int y) {
        return valueOf((long) x * y);
    }

    public int plus(int x, int y) {
        return valueOf(x + y);
    }

    public int subtract(int x, int y) {
        return valueOf(x - y);
    }

    public String toString() {
        return "mod " + m;
    }

}

interface IntCombination {
}
