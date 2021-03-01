package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;
import template.math.MultiplicativeFunctionSieve;

import java.math.BigInteger;

public class BZOJ2401 {
    int limit = (int) 1e6;
    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(limit);
    long[] delta = new long[limit + 1];
    long[] A = new long[limit + 1];
    long[] S = new long[limit + 1];
    BigInteger[] cache = new BigInteger[limit + 1];

    {
        int[] mobius = sieve.getMobius();
        for (int i = 1; i <= limit; i++) {
            S[i] = S[i - 1] + i;
        }
        for (int i = 1; i <= limit; i++) {
            for (int j = 1; j * i <= limit; j++) {
                A[j * i] += (long) mobius[i] * i;
            }
        }
        for (int i = 1; i <= limit; i++) {
            for (int j = 1; j * i <= limit; j++) {
                delta[i * j] += S[i] * A[j];
            }
        }
        cache[1] = BigInteger.ONE;
        for (int i = 2; i <= limit; i++) {
            cache[i] = cache[i - 1].add(BigInteger.valueOf(2 * i * delta[i] - i));
        }
    }



    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        out.println(cache[n]);
    }
}
