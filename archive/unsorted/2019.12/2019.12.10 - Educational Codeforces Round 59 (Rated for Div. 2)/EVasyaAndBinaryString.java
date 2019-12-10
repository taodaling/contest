package contest;

import template.algo.PreSum;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class EVasyaAndBinaryString {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++)
            System.out.append('0');
    }

    long[][][] dp;
    long[] profits;
    int[] flags;
    int n;
    long inf = (long) 1e18;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        flags = new int[n];
        for (int i = 0; i < n; i++) {
            flags[i] = in.readChar() - '0';
        }

        profits = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            profits[i] = in.readInt();
        }

        dp = new long[n][n][n + 1];
        SequenceUtils.deepFill(dp, -1L);

        long ans = dp(0, n - 1, 0);
        out.println(ans);
    }

    public long dp(int l, int r, int remain) {
        if (l > r) {
            return remain == 0 ? 0 : -inf;
        }
        if (dp[l][r][remain] == -1) {
            dp[l][r][remain] = -inf;
            for (int i = n; i > remain; i--) {
                dp[l][r][remain] = Math.max(dp[l][r][remain],
                        dp(l, r, i) + profits[i - remain]);
            }
            if (remain == 0) {
                return dp[l][r][remain];
            }
            if (l == r) {
                dp[l][r][remain] = remain == 1 ? 0 : -inf;
                return dp[l][r][remain];
            }
            if (remain == 1) {
                dp[l][r][remain] = Math.max(dp[l][r][remain],
                        dp(l, l, 1) + dp(l + 1, r, 0));
            }
            for (int i = l + 1; i <= r; i++) {
                if (flags[i] != flags[l]) {
                    continue;
                }
                dp[l][r][remain] = Math.max(dp[l][r][remain],
                        dp(l, i - 1, 0) +
                                dp(i, r, remain));
                dp[l][r][remain] = Math.max(dp[l][r][remain],
                        dp(l, i - 1, 1) +
                                dp(i, r, remain - 1));
            }
        }
        return dp[l][r][remain];
    }
}
