package on2021_07.on2021_07_20_CodeChef___June_Cook_Off_2021_Division_1.Battle_Royale;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.utils.Debug;

public class BattleRoyale {
    int mod = (int) 1e9 + 7;
    InverseNumber inv = new ModPrimeInverseNumber(100, mod);

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int N = 100;
        long[][][] dp = new long[N + 1][N + 1][N + 1];
        for (int i = 1; i <= N; i++) {
            dp[i][i][0] = 1;
        }
        for (int i = 2; i <= N; i++) {
            long invI = inv.inverse(i - 1);
            for (int j = i; j >= 0; j--) {
                for (int k = 0; k + j <= i; k++) {
                    dp[i][j][k] %= mod;
                    long way = dp[i][j][k];
                    if (way == 0 || j == 0) {
                        continue;
                    }
                    int remain = j - 1;
                    int old = i - j - k;
                    //kill a killed
                    dp[i][j - 1][k] += invI * k % mod * way % mod;
                    //kill a old
                    dp[i][j - 1][k + 1] += invI * old % mod * way % mod;
                    //kill a new
                    if (remain > 0) {
                        dp[i][j - 2][k + 1] += invI * remain % mod * way % mod;
                    }
                }
            }
        }
        long[] exp = new long[N + 1];
        for (int i = 2; i <= N; i++) {
            exp[i] = 1;
            for (int j = 1; j <= i; j++) {
                exp[i] += exp[i - j] * dp[i][0][j] % mod;
            }
            exp[i] = DigitUtils.mod(exp[i], mod);
        }

//        debug.debug("2/3", 2L * inv.inverse(3) % mod);
        debug.debug("dp", dp);
        debug.debug("exp", exp);
        int t = in.ri();
        for (int i = 0; i < t; i++) {
            int n = in.ri();
            out.println(exp[n]);
        }
    }
}
