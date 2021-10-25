package on2021_10.on2021_10_08_AtCoder___AtCoder_Beginner_Contest_221.H___Count_Multiset;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class HCountMultiset {
    int mod = 998244353;

    public void update(long[][] dp, int l, int r, int x, long v) {
        dp[l][x] += v;
        if (r + 1 < dp.length) {
            dp[r + 1][x] -= v;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[][][] dp = new long[2][n + 2][n + 1];
        dp[1][0][0] = 1;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (i > 0) {
                    dp[0][i][j] += dp[0][i - 1][j];
                }
                dp[0][i][j] %= mod;
                //to one
                int to;
                for (int k = 1; (to = k * (i + 1) + j) <= n; k++) {
                    dp[1][i + 1][to] += dp[0][i][j];
                }
            }
            for (int j = 0; j <= n; j++) {
                dp[1][i][j] %= mod;
                int to;
                for (int k = 1; (to = k * (i + 1) + j) <= n; k++) {
                    dp[1][i + 1][to] += dp[1][i][j];
                }

                update(dp[0], i + 1, i + m - 1, j, dp[1][i][j]);
            }
        }

        for (int k = 1; k <= n; k++) {
            long ans = dp[1][k][n];
            ans = DigitUtils.mod(ans, mod);
            out.println(ans);
        }
    }

    Debug debug = new Debug(true);
}
