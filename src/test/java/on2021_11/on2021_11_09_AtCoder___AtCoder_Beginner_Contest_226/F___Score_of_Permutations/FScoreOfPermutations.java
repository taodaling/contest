package on2021_11.on2021_11_09_AtCoder___AtCoder_Beginner_Contest_226.F___Score_of_Permutations;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.utils.Debug;

public class FScoreOfPermutations {
    int mod = 998244353;
    Combination combSlow = new Combination(100, mod);
    Power pow = new Power(mod);
    Debug debug = new Debug(false);
    Factorial fact = new Factorial(100, mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int L = (int) 3e5;
        int n = in.ri();
        int k = in.ri();
        long[][] dp = new long[n + 1][L];
        int[][] lcm = new int[n + 1][L];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j < L; j++) {
                lcm[i][j] = (int) LCMs.lcm(i, j);
            }
        }
        dp[0][1] = 1;
        int[][] comb = new int[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                comb[i][j] = (int) ((long)combSlow.combination(i, j) * fact.fact(j) % mod);
            }
        }
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < L; j++) {
                dp[i][j] %= mod;
                if (dp[i][j] == 0) {
                    continue;
                }
                for (int t = 1; i + t <= n; t++) {
                    dp[i + t][lcm[t][j]] += comb[n - i - 1][t - 1] * dp[i][j] % mod;
                }
            }
        }

        debug.debug("lcm", lcm);
        debug.debug("dp", dp);
        long ans = 0;
        for (int i = 0; i < L; i++) {
            ans += pow.pow(i, k) * dp[n][i] % mod;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
