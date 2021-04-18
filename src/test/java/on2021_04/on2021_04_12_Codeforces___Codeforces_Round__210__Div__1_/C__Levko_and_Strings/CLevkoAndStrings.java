package on2021_04.on2021_04_12_Codeforces___Codeforces_Round__210__Div__1_.C__Levko_and_Strings;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class CLevkoAndStrings {
    int mod = (int) 1e9 + 7;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        char[] s = new char[n + 1];
        in.rs(s, 1);
        long[] ps = new long[k + 1];
        long[][] dp = new long[n + 1][k + 1];
        dp[0][0] = 1;
        ps[0] = 1;
        for (int i = 1; i <= n; i++) {
            int right = n - i + 1;
            int less = s[i] - 'a';
            int greater = 'z' - s[i];
            for (int j = 0; j <= k; j++) {
                dp[i][j] = ps[j] * less;
                int d;
                for (int t = i - 1; t >= 0 && (d = j - (i - t) * right) >= 0; t--) {
                    dp[i][j] += dp[t][d] * greater;
                }
                dp[i][j] %= mod;
                ps[j] += dp[i][j];
                if (ps[j] >= mod) {
                    ps[j] -= mod;
                }
            }
        }

        debug.debug("s", s);
        debug.debug("dp", dp);
        long ans = ps[k];
        out.println(ans);
    }
}
