package on2021_08.on2021_08_23_CS_Academy___Virtual_Round__21.Tournament_Cycle;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.utils.Debug;

public class TournamentCycle {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e4, mod);
    Combination comb = new Combination(fact);
    FastPow2 p2 = new FastPow2(2, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long[][][] dp = new long[2][2][n + 1];

        dp[0][0][0] = 1;
        for (int i = 0; i < n; i++) {
            for(int t = 0; t < 2; t++) {
                for (int j = 0; j < 2; j++) {
                    dp[t][j][i] %= mod;
                    if (dp[t][j][i] == 0) {
                        continue;
                    }
                    //single
                    dp[t][j][i + 1] += dp[t][j][i] * p2.pow(i) % mod;
                    if (i + k <= n) {
                        dp[1][j ^ 1][i + k] += dp[t][j][i]
                                * comb.combination(n - i - 1, k - 1) % mod
                                * fact.fact(k - 1) % mod
                                * p2.pow(IntMath.sumOfInterval(i, i + k - 1) - k) % mod;
                    }
                }
            }
        }
        debug.debug("dp", dp);
        long ans = dp[1][1][n] - dp[1][0][n];
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }

    Debug debug = new Debug(true);
}
