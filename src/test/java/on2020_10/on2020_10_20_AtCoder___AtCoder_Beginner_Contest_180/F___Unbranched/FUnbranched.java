package on2020_10.on2020_10_20_AtCoder___AtCoder_Beginner_Contest_180.F___Unbranched;



import template.io.FastInput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.io.PrintWriter;

public class FUnbranched {
    int mod = (int) 1e9 + 7;
    int limit = 500;
    int[] path = new int[limit];
    int[] circle = new int[limit];
    Factorial fact = new Factorial(limit, mod);
    int inv2 = (mod + 1) / 2;
    Combination comb = new Combination(fact);

    public int path(int n) {
        if (n == 1) {
            return 1;
        }
        long ans = (long) fact.fact(n - 1) * (n / 2 + (n % 2 * inv2)) % mod;
        return (int) ans;
    }

    public int circle(int n) {
        if (n == 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        long ans = (long) (n - 1) * (n - 2) / 2 % mod;
        for (int i = 4; i + 1 <= n; i += 2) {
            ans = ans * (n - i + 1) % mod * (n - i) % mod;
        }
        return (int) ans;
    }


    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        for (int i = 1; i < limit; i++) {
            path[i] = path(i);
            circle[i] = circle(i);
        }
        debug.debug("path", path);
        debug.debug("circle", circle);

        int n = in.readInt();
        int m = in.readInt();
        int l = in.readInt();
        long[][][] dp = new long[n + 1][m + 1][2];
        dp[0][0][0] = 1;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int t = 0; t < 2; t++) {
                    dp[i][j][t] %= mod;
                    if (dp[i][j][t] == 0) {
                        continue;
                    }
                    for (int k = i + 1; k <= n && k - i <= l; k++) {
                        int next = t | (k - i == l ? 1 : 0);
                        if (j + k - i - 1 <= m) {
                            dp[k][j + k - i - 1][next] += dp[i][j][t] * path[k - i] % mod * comb.combination(n - i - 1, k - i - 1) % mod;
                        }
                        if (j + k - i <= m) {
                            dp[k][j + k - i][next] += dp[i][j][t] * circle[k - i] % mod * comb.combination(n - i - 1, k - i - 1) % mod;
                        }
                    }
                }
            }
        }

        debug.debug("dp", dp);

        long ans = dp[n][m][1];
        out.println(ans);
    }
}
