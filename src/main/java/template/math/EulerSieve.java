package template.math;

/**
 * Euler sieve for filter primes
 */
public class EulerSieve {
    private int[] primes;
    private boolean[] isComp;
    private int primeLength;

    public int getPrimeCount() {
        return primeLength;
    }

    public int get(int k) {
        return primes[k];
    }

    public boolean isPrime(int x) {
        if (x <= 1) {
            return false;
        }
        return !isComp[x];
    }

    public boolean isComp(int x) {
        if (x <= 1) {
            return false;
        }
        return isComp[x];
    }

    public EulerSieve(int limit) {
        isComp = new boolean[limit + 1];
        primes = new int[limit + 1];
        primeLength = 0;
        for (int i = 2; i <= limit; i++) {
            if (!isComp[i]) {
                primes[primeLength++] = i;
            }
            for (int j = 0, until = limit / i; j < primeLength && primes[j] <= until; j++) {
                int pi = primes[j] * i;
                isComp[pi] = true;
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }
    }
}
