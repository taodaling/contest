package on2020_10.on2020_10_25_CSES___CSES_Problem_Set.Removal_Game;



import template.io.FastInput;
import template.utils.SequenceUtils;

import java.io.PrintWriter;

public class RemovalGame {
    long[][] dp;
    int[] a;

    public long dp(int l, int r) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r] == Long.MIN_VALUE) {
            dp[l][r] = Math.max(dp[l][r], a[l] - dp(l + 1, r));
            dp[l][r] = Math.max(dp[l][r], a[r] - dp(l, r - 1));
        }
        return dp[l][r];
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        a = new int[n];
        in.populate(a);
        dp = new long[n][n];
        SequenceUtils.deepFill(dp, Long.MIN_VALUE);
        long ans = dp(0, n - 1);
        //x - y = ans
        //x + y = sum
        //x = (ans + sum) / 2
        for (int i = 0; i < n; i++) {
            ans += a[i];
        }
        ans /= 2;
        out.println(ans);
    }
}
