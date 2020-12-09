package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ABouncingBall {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int p = in.ri();
        int k = in.ri();

        char[] s = new char[n];
        in.readString(s, 0);

        long x = in.ri();
        long y = in.ri();
        long[] dp = new long[n];
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = ('1' - s[i]) * x;
            if (i + k < n) {
                dp[i] += dp[i + k];
            }
        }

        long ans = (long) 1e18;

        for (int i = p - 1; i < n; i++) {
            ans = Math.min(ans, dp[i] + (i - (p - 1)) * y);
        }

        out.println(ans);
    }
}
