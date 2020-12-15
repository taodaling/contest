package contest;

import sun.security.util.BitArray;
import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.function.LongPredicate;

public class Elevator {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int c = in.ri();
        int p = in.ri();
        int t = in.ri();
        long[] a = new long[n];
        in.populate(a);
        long[] b = new long[n];
        LongPredicate predicate = new LongPredicate() {
            @Override
            public boolean test(long value) {
                long sum = 0;
                for (int i = 0; i < n; i++) {
                    long add = Math.min(value - sum, a[i]);
                    sum += add;
                    b[i] = add;
                }
                long round = 0;
                long timeNeed = 0;
                long suf = 0;
                for (int i = n - 1; i >= 0; i--) {
                    suf += b[i];
                    long roundNeed = DigitUtils.ceilDiv(suf, c);
                    timeNeed += (roundNeed - round) * (i + 1);
                    round = roundNeed;
                }
                return !(timeNeed * 2 * p <= t);
            }
        };
        long sum = Arrays.stream(a).sum();
        long ans = BinarySearch.lastFalse(predicate, 0, sum);
        out.println(ans);
    }
}
