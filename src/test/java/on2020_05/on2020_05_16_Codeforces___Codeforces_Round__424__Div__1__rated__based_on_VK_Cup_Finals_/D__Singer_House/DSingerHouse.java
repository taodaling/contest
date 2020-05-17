package on2020_05.on2020_05_16_Codeforces___Codeforces_Round__424__Div__1__rated__based_on_VK_Cup_Finals_.D__Singer_House;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Modular;
import template.utils.Debug;

public class DSingerHouse {

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = n + 2;
        long[][] dp = new long[n + 1][m + 1];

        Modular mod = new Modular(1e9 + 7);

        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= m; k++) {
                    int way = mod.mul(dp[i - 1][j], dp[i - 1][k]);
                    if (j + k <= m) {
                        //not appear
                        int next = j + k;
                        dp[i][next] += way;
                    }
                    if (j + k - 1 <= m && j + k - 1 >= 0) {
                        //appear, and concatenate
                        int next = j + k - 1;
                        int local = mod.mul(way, (j + k) * (j + k - 1));
                        dp[i][next] += local;
                    }
                    if (j + k <= m) {
                        //appear, but only appear on one side
                        int next = j + k;
                        int local = mod.mul(way, 2 * (j + k));
                        dp[i][next] += local;
                    }
                    if (j + k + 1 <= m) {
                        //appear, with new in/out
                        int next = j + k + 1;
                        dp[i][next] += way;
                    }
                }
            }
            dp[i][0] = 1;
            for (int j = 0; j <= m; j++) {
                dp[i][j] = mod.valueOf(dp[i][j]);
            }
        }

        //debug.debug("dp", dp);
        int ans = (int)dp[n][1];
        out.println(ans);
    }
}
