package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.MultiplicativeFunctionSieve;
import template.utils.Debug;

public class DPaths {
    int[] sqrt;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(n, true, false, false);
        long coprime = 0;
        for (int i = 1; i <= n; i++) {
            long m = n / i;
            coprime += sieve.mobius[i] * (m * m - m * (m + 1) / 2);
        }
        sqrt = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            sqrt[i] = sqrt[i - 1];
            while ((sqrt[i] + 1) * (sqrt[i] + 1) <= i) {
                sqrt[i]++;
            }
        }

        int[] preSum = new int[n + 1];
        preSum[0] = sieve.mobius[0];
        for (int i = 1; i <= n; i++) {
            preSum[i] = preSum[i - 1] + sieve.mobius[i];
        }

        long okCoprime = 0;
        for (int i = 1, r; i * i <= n; i = r + 1) {
            r = Math.min(n / (n / i), n / (n / (i * i)));
            long val = sum(n / i, n / (i * i));
            okCoprime += val * (r - i + 1);
        }
        okCoprime -= n - 1;

        long bfCoprime = 0;
        long bfOkCoprime = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (GCDs.gcd(i, j) == 1) {
                    bfCoprime++;
                    if ((long) i * j <= n && i != 1) {
                        bfOkCoprime++;
                    }
                }
            }
        }
        if (bfCoprime != coprime) {
            throw new RuntimeException();
        }
        if (bfOkCoprime != okCoprime) {
            throw new RuntimeException();
        }


        debug.debug("coprime", coprime);
        debug.debug("okCoprime", okCoprime);
        debug.debug("sum", n * (n - 1) / 2);
        long total = (long)n * (n - 1) / 2 - coprime + okCoprime * 2;
        out.println(total);
    }


    public long sum(int n, int m) {
        n = Math.min(n, sqrt[m]);

        long a = 0;
        for (int i = 1, r; i <= n; i = r + 1) {
            r = m / (m / i);
            a += (long) (m / i) * (r - i + 1);
        }

        long b = (long)n * (n + 1) / 2;
        return a - b;
    }
}
