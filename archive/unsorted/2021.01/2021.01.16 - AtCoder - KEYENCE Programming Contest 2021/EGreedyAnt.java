package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

public class EGreedyAnt {
    int[][][] dp;
    int[] a;
    int n;

    int dp(int l, int r, int n) {
        if (dp[l][r][n] == -1) {
            if (l == 0 && r == this.n + 1) {
                return dp[l][r][n] = 0;
            }
            int round = r - l - 1;
            int chance = n + 1 + n - round;
            int ans = 0;
            if (a[l] < a[r]) {
                ans = Math.max(ans, dp(l, r + 1, n + 1));
            } else {
                ans = Math.max(ans, dp(l - 1, r, n + 1));
            }
            if (chance > 0) {
                if (l >= 1) {
                    ans = Math.max(ans, dp(l - 1, r, n) + a[l]);
                }
                if (r <= this.n) {
                    ans = Math.max(ans, dp(l, r + 1, n) + a[r]);
                }
            }
            dp[l][r][n] = ans;
        }
        return dp[l][r][n];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        a = new int[n + 2];
        dp = new int[n + 2][n + 2][n + 3];
        for (int i = 1; i <= n; i++) {
            a[i] = in.ri();
        }
        SequenceUtils.deepFill(dp, -1);
        for (int i = 0; i <= n; i++) {
            int ans = dp(i, i + 1, 0);
            out.println(ans);
        }
    }
}
