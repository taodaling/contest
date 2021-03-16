package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.utils.Debug;

public class CNondivisiblePrefixSums {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e4, mod);

    public void addDelta(long[] data, int l, int r, long x) {
        if (l >= data.length) {
            int d = l - (data.length - 1);
            l -= d;
            r -= d;
        }
        data[l] += x;
        if (r + 1 < data.length) {
            data[r + 1] -= x;
        } else {
            data[data.length - 1] += (r - (data.length - 1)) * x % mod;
        }
    }

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int p = in.ri();
        long[][] dp = new long[n + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                if (dp[i][j] == 0) {
                    continue;
                }
                addDelta(dp[i + 1], j + 1, j + (p - 2), dp[i][j]);
            }
            for (int j = 0; j <= n; j++) {
                if (j > 0) {
                    dp[i + 1][j] += dp[i + 1][j - 1];
                }
                dp[i + 1][j] %= mod;
            }
        }
        debug.debug("dp", dp);
        long invalid = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (n - i <= j + p - 1 || (n - i) * 2 <= n || (j - (n - i)) % p == 0) {
                    continue;
                }
                invalid += dp[i][j] * comb.combination(n, n - i) % mod;
            }
        }
        invalid = invalid * (p - 1) % mod;
        long[][] dp2 = new long[n + 1][2];
        dp2[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            dp[i][0] = dp[i - 1][1] * (p - 1) % mod;
            dp[i][1] = (dp[i - 1][0] + dp[i - 1][1] * (p - 2)) % mod;
        }
        long total = dp[n][1] * (p - 1) % mod;

        debug.debug("invalid", invalid);
        debug.debug("total", total);

        long ans = total - invalid;
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
