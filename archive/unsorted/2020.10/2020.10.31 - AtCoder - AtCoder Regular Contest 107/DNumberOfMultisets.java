package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DNumberOfMultisets {
    int mod = 998244353;
    long[][] dp;
    int n;
    int k;

    public long dp(int i, int j) {
        if (i == 0) {
            return j == 0 ? 1 : 0;
        }
        if (j > i) {
            return 0;
        }
        if (dp[i][j] == -1) {
            if (j == 0) {
                return dp[i][j] = 0;
            }
            dp[i][j] = dp(i, j * 2) + dp(i - 1, j - 1);
            dp[i][j] %= mod;
        }
        return dp[i][j];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        k = in.readInt();
        dp = new long[n + 1][n + 1];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(n, k);
        out.println(ans);
    }
}
