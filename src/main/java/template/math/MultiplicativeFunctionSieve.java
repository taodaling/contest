package template.math;

/**
 * Euler sieve for multiplicative function
 */
public class MultiplicativeFunctionSieve {
    public int[] primes;
    public boolean[] isComp;
    public int primeLength;
    public int[] mobius;
    public int[] euler;
    public int[] factors;
    public int[] smallestPrimeFactor;
    public int[] expOfSmallestPrimeFactor;

    public MultiplicativeFunctionSieve(int limit, boolean enableMobius, boolean enableEuler, boolean enableFactors) {
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

        if (enableMobius) {
            mobius = new int[limit + 1];
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
        }

        if (enableEuler) {
            euler = new int[limit + 1];
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
        }

        if (enableFactors) {
            factors = new int[limit + 1];
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
        }
    }
}
