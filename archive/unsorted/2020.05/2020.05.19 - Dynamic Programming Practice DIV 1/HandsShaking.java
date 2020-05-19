package contest;

import java.util.Arrays;

public class HandsShaking {
    long[] dp;

    public long dp(int n) {
        if (dp[n] == -1) {
            if (n <= 1) {
                return dp[n] = n == 0 ? 1 : 0;
            }
            dp[n] = 0;
            for (int left = 0; left <= n - 2; left++) {
                dp[n] += dp(left) * dp(n - 2 - left);
            }
        }
        return dp[n];
    }

    public long countPerfect(int n) {
        dp = new long[n + 1];
        Arrays.fill(dp, -1);
        long ans = dp(n);
        return ans;
    }
}
