package contest;

import template.algo.PreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskN {
    long[][] dp;
    PreSum ps;
    int[] a;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        ps = new PreSum(a);
        dp = new long[n][n];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(0, n - 1);
        out.println(ans);
    }

    public long dp(int l, int r) {
        if (dp[l][r] == -1) {
            dp[l][r] = (long) 1e18;
            if (l == r) {
                return dp[l][r] = 0;
            }
            for (int i = l; i < r; i++) {
                dp[l][r] = Math.min(dp[l][r],
                        dp(l, i) + dp(i + 1, r) +
                                ps.intervalSum(l, r));
            }
        }
        return dp[l][r];
    }
}
