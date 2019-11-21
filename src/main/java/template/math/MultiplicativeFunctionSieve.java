package template.math;

/**
 * Euler sieve for multiplicative function
 */
public class MultiplicativeFunctionSieve {
    int[] primes;
    boolean[] isComp;
    int primeLength;
    int[] mobius;
    int[] euler;
    int[] factors;
    int[] smallestPrimeFactor;
    int[] numberOfSmallestPrimeFactor;

    public MultiplicativeFunctionSieve(int limit, boolean enableMobius, boolean enableEuler, boolean enableFactors) {
        isComp = new boolean[limit + 1];
        primes = new int[limit + 1];
        numberOfSmallestPrimeFactor = new int[limit + 1];
        smallestPrimeFactor = new int[limit + 1];
        primeLength = 0;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                primes[primeLength++] = i;
                numberOfSmallestPrimeFactor[i] = smallestPrimeFactor[i] = i;
            }
            for (int j = 0, until = limit / i; j < primeLength && primes[j] <= until; j++) {
                int pi = primes[j] * i;
                smallestPrimeFactor[pi] = primes[j];
                numberOfSmallestPrimeFactor[pi] = smallestPrimeFactor[i] == primes[j]
                                ? (numberOfSmallestPrimeFactor[i] * numberOfSmallestPrimeFactor[primes[j]])
                                : numberOfSmallestPrimeFactor[primes[j]];
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
                    if (numberOfSmallestPrimeFactor[i] != smallestPrimeFactor[i]) {
                        mobius[i] = 0;
                    } else {
                        mobius[i] = mobius[numberOfSmallestPrimeFactor[i]] * mobius[i / numberOfSmallestPrimeFactor[i]];
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
                    if (numberOfSmallestPrimeFactor[i] == i) {
                        euler[i] = i - i / smallestPrimeFactor[i];
                    } else {
                        euler[i] = euler[numberOfSmallestPrimeFactor[i]] * euler[i / numberOfSmallestPrimeFactor[i]];
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
                    if (numberOfSmallestPrimeFactor[i] == i) {
                        factors[i] = 1 + factors[i / smallestPrimeFactor[i]];
                    } else {
                        factors[i] = factors[numberOfSmallestPrimeFactor[i]]
                                        * factors[i / numberOfSmallestPrimeFactor[i]];
                    }
                }
            }
        }
    }
}
