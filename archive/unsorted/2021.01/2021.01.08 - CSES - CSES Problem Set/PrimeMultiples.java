package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class PrimeMultiples {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        int k = in.ri();
        long[] a = new long[k];
        in.populate(a);
        long sum = 0;
        for (int i = 1; i < 1 << k; i++) {
            long contrib = n;
            for (int j = 0; j < k; j++) {
                if (Bits.get(i, j) == 0) {
                    continue;
                }
                contrib /= a[j];
            }
            if (Integer.bitCount(i) % 2 == 0) {
                contrib = -contrib;
            }
            debug.debug("i", i);
            debug.debug("contrib", contrib);
            sum += contrib;
        }
        out.println(sum);
    }

    long[] a;
}
