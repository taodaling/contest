package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class KnuthDivision {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = new int[n];
        in.populate(x);

        long inf = (long) 1e18;
        long[][] dp = new long[n][n];
        SequenceUtils.deepFill(dp, inf);
        int[][] opt = new int[n][n];
        for (int i = 0; i < n; i++) {
            opt[i][i] = i;
            dp[i][i] = 0;
        }
        LongPreSum lps = new LongPreSum(i -> x[i], n);
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j + i <= n; j++) {
                int l = j;
                int r = j + i - 1;
                int L = Math.max(opt[l][r - 1], l + 1);
                int R = Math.min(opt[l + 1][r], r);
                for (int k = L; k <= R; k++) {
                    long cand = dp[l][k - 1] + dp[k][r];
                    if (cand < dp[l][r]) {
                        dp[l][r] = cand;
                        opt[l][r] = k;
                    }
                }
                dp[l][r] += lps.intervalSum(l, r);
            }
        }
        debug.debug("dp", dp);
        long ans = dp[0][n - 1];
        out.println(ans);
    }
}
