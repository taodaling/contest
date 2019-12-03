package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskL {
    int[] a;
    long[][] dp;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        dp = new long[n][n];
        SequenceUtils.deepFill(dp, Long.MIN_VALUE);
        long ans = dp(0, n - 1);
        out.println(ans);
    }

    public long dp(int l, int r) {
        if (dp[l][r] == Long.MIN_VALUE) {
            if (l == r) {
                return dp[l][r] = a[l];
            }
            dp[l][r] = Math.max(a[l] - dp(l + 1, r),
                    a[r] - dp(l, r - 1));
        }
        return dp[l][r];
    }
}
