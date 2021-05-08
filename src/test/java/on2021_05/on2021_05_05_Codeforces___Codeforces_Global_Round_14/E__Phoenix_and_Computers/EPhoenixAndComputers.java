package on2021_05.on2021_05_05_Codeforces___Codeforces_Global_Round_14.E__Phoenix_and_Computers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow2;
import template.math.Combination;

public class EPhoenixAndComputers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int mod = in.ri();
        CachedPow2 cp = new CachedPow2(2, mod, n + 1, mod - 1);
        Combination comb = new Combination(n + 1, mod);
        long[][][] dp = new long[n + 1][n + 1][2];
        dp[0][0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j][0] = dp[i - 1][j][1];
                for (int k = 0; k < i; k++) {
                    int len = i - k;
                    int last = j - len;
                    if (last >= 0) {
                        dp[i][j][1] += dp[k][last][0] * cp.pow(len - 1) % mod * comb.combination(j, len) % mod;
                    }
                }
                dp[i][j][1] %= mod;
            }
        }

        long ans = 0;
        for(int i = 0; i <= n; i++){
            ans += dp[n][i][1];
        }

        ans %= mod;
        out.println(ans);
    }
}
