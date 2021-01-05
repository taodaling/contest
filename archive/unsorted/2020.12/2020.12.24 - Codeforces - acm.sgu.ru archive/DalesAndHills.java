package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class DalesAndHills {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] neg = new int[n];
        in.populate(a);
        for (int i = 0; i < n; i++) {
            neg[i] = -a[i];
        }
        int[] lInc = inc(a);
        int[] lDec = inc(neg);
        SequenceUtils.reverse(a);
        SequenceUtils.reverse(neg);
        int[] rDec = inc(a);
        int[] rInc = inc(neg);
        SequenceUtils.reverse(rInc);
        SequenceUtils.reverse(rDec);

        debug.debugArray("lInc", lInc);
        debug.debugArray("rInc", rInc);
        debug.debugArray("lDec", lDec);
        debug.debugArray("rDec", rDec);

        int height = 0;
        int depth = 0;
        for (int i = 1; i < n - 1; i++) {
            height = Math.max(height, Math.min(lInc[i], rDec[i]));
            depth = Math.max(depth, Math.min(lDec[i], rInc[i]));
        }
        out.append(height).append(' ').append(depth).println();
    }

    public int[] inc(int[] a) {
        int n = a.length;
        int[] dp = new int[n];
        for (int i = 1; i < n; i++) {
            dp[i] = 0;
            if (a[i - 1] < a[i]) {
                dp[i] = dp[i - 1] + 1;
            }
        }
        return dp;
    }
}
