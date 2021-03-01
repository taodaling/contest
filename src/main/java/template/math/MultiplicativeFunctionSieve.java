package template.math;

/**
 * Euler sieve for multiplicative function
 */
public class MultiplicativeFunctionSieve {
    static MultiplicativeFunctionSieve instance = new MultiplicativeFunctionSieve(1 << 16);

    public static MultiplicativeFunctionSieve getInstance(int n) {
        if (n <= (1 << 16)) {
            return instance;
        }
        return new MultiplicativeFunctionSieve(n);
    }

    public int[] primes;
    public boolean[] isComp;
    public int primeLength;
    public int[] smallestPrimeFactor;
    public int[] expOfSmallestPrimeFactor;
    int limit;

    public int[] pow(int k, Power pow) {
        int mod = pow.getMod();
        int[] ans = new int[limit + 1];
        if (k == 0) {
            ans[0] = 1;
        }
        if (limit >= 1) {
            ans[1] = 1;
        }
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                ans[i] = pow.pow(i, k);
            } else {
                ans[i] = (int) ((long) ans[i / smallestPrimeFactor[i]] * ans[smallestPrimeFactor[i]] % mod);
            }
        }
        return ans;
    }

    public int[] getMobius() {
        int[] mobius = new int[limit + 1];
        mobius[1] = 1;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                mobius[i] = -1;
            } else {
                if (expOfSmallestPrimeFactor[i] != smallestPrimeFactor[i]) {
                    mobius[i] = 0;
                } else {
                    mobius[i] = mobius[expOfSmallestPrimeFactor[i]] * mobius[i / expOfSmallestPrimeFactor[i]];
                }
            }
        }
        return mobius;
    }

    public int[] getEuler() {
        int[] euler = new int[limit + 1];
        euler[1] = 1;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                euler[i] = i - 1;
            } else {
                if (expOfSmallestPrimeFactor[i] == i) {
                    euler[i] = i - i / smallestPrimeFactor[i];
                } else {
                    euler[i] = euler[expOfSmallestPrimeFactor[i]] * euler[i / expOfSmallestPrimeFactor[i]];
                }
            }
        }
        return euler;
    }

    public int[] getSmallestPrimeFactor() {
        return smallestPrimeFactor;
    }

    public int[] getFactor() {
        int[] factors = new int[limit + 1];
        factors[1] = 1;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                factors[i] = 2;
            } else {
                if (expOfSmallestPrimeFactor[i] == i) {
                    factors[i] = 1 + factors[i / smallestPrimeFactor[i]];
                } else {
                    factors[i] = factors[expOfSmallestPrimeFactor[i]]
                            * factors[i / expOfSmallestPrimeFactor[i]];
                }
            }
        }
        return factors;
    }

    public MultiplicativeFunctionSieve(int limit) {
        this.limit = limit;
        isComp = new boolean[limit + 1];
        primes = new int[limit + 1];
        expOfSmallestPrimeFactor = new int[limit + 1];
        smallestPrimeFactor = new int[limit + 1];
        primeLength = 0;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                primes[primeLength++] = i;
                expOfSmallestPrimeFactor[i] = smallestPrimeFactor[i] = i;
            }
            for (int j = 0, until = limit / i; j < primeLength && primes[j] <= until; j++) {
                int pi = primes[j] * i;
                smallestPrimeFactor[pi] = primes[j];
                expOfSmallestPrimeFactor[pi] = smallestPrimeFactor[i] == primes[j]
                        ? (expOfSmallestPrimeFactor[i] * expOfSmallestPrimeFactor[primes[j]])
                        : expOfSmallestPrimeFactor[primes[j]];
                isComp[pi] = true;
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }
    }
}
