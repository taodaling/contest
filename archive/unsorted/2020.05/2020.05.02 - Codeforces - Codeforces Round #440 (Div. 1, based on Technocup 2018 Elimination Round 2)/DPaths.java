package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.Debug;

public class DPaths {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(n, false, true, false);

        long twoCnt = 0;
        long threeCnt = 0;
        int[] ps = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            ps[sieve.smallestPrimeFactor[i]]++;
        }
        for (int i = 1; i <= n; i++) {
            ps[i] += ps[i - 1];
        }
        for (int i = 1; i * 2 <= n; i++) {
            int num = (ps[i] - ps[i - 1]);

            int j = Math.min(i - 1, n / i);
            twoCnt += (long) ps[j] * num;

            int k = Math.min(i - 1, n / 2);
            threeCnt += (long) ps[k] * num;
        }
        for (int i = 1; i <= n; i++) {
            int num = ps[i] - ps[i - 1];
            if (i * i <= n) {
                twoCnt += (long)num * (num - 1) / 2;
            }
            if (i * 2 <= n) {
                threeCnt += (long)num * (num - 1) / 2;
            }
        }


        threeCnt -= twoCnt;
        long coprime = 0;
        for (int i = 2; i <= n; i++) {
            coprime += sieve.euler[i];
        }

        long notCoprime = (long) n * (n - 1) / 2 - coprime;
        long ans = twoCnt * 2 - notCoprime + threeCnt * 3;

        debug.debug("coprime", coprime);
        debug.debug("twoCnt", twoCnt);
        debug.debug("notCporime", notCoprime);
        debug.debug("threeCnt", threeCnt);
        out.println(ans);
    }
}
