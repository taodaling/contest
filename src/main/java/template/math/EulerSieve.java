package template.math;

import template.datastructure.BitSet;

/**
 * Euler sieve for filter primes
 */
public class EulerSieve {
    private int[] primes;
    private BitSet isComp;
    private int primeLength;

    public int getPrimeCount() {
        return primeLength;
    }

    public int get(int k) {
        return primes[k];
    }

    public int prevPrime(int x) {
        int ans = isComp.prevClearBit(x);
        return ans <= 1 ? -1 : ans;
    }

    public int nextPrime(int x){
        int ans = isComp.nextClearBit(x);
        return ans >= isComp.capacity() ? -1 : ans;
    }

    public boolean isPrime(int x) {
        if (x <= 1) {
            return false;
        }
        return !isComp.get(x);
    }

    public boolean isComp(int x) {
        if (x <= 1) {
            return false;
        }
        return isComp.get(x);
    }

    private static int[] phi = new int[]{
            4, 25, 168, 1229, 9592, 78498, 664579, 5761455, 50847534, 455052511
    };

    private int estimatePhi(int n) {
        long x = 10;
        int i = 0;
        while (x < n) {
            x *= 10;
            i++;
        }
        return phi[i];
    }

    public EulerSieve(int limit) {
        isComp = new BitSet(limit + 1);
        primes = new int[estimatePhi(limit)];
        primeLength = 0;
        for (int i = 2; i <= limit; i++) {
            if (!isComp.get(i)) {
                primes[primeLength++] = i;
            }
            for (int j = 0, until = limit / i; j < primeLength && primes[j] <= until; j++) {
                int pi = primes[j] * i;
                isComp.set(pi);
                if (i % primes[j] == 0) {
                    break;
                }
            }
        }
    }
}
