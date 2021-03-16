package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

import java.math.BigInteger;

public class ANotCoprime {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        EulerSieve sieve = new EulerSieve(50);
        int[] primes = new int[sieve.getPrimeCount()];
        for (int i = 0; i < sieve.getPrimeCount(); i++) {
            primes[i] = sieve.get(i);
        }
        int m = primes.length;
        int n = in.ri();
        int[] a = in.ri(n);
        long min = Long.MAX_VALUE;
        for (int i = 0; i < 1 << m; i++) {
            long prod = 1;
            for (int j = 0; j < m; j++) {
                if (Bits.get(i, j) == 1) {
                    prod *= primes[j];
                }
            }
            boolean ok = true;
            for (int x : a) {
                boolean find = false;
                for (int j = 0; j < m; j++) {
                    if (Bits.get(i, j) == 1 && x % primes[j] == 0) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                min = Math.min(min, prod);
            }
        }
        out.println(min);
    }
}
