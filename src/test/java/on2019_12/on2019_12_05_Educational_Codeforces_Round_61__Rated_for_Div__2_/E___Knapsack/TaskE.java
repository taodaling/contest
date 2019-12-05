package on2019_12.on2019_12_05_Educational_Codeforces_Round_61__Rated_for_Div__2_.E___Knapsack;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Gcd;
import template.math.Lcm;
import template.utils.SequenceUtils;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long w = in.readLong();
        long[] cnts = new long[9];
        for (int i = 1; i <= 8; i++) {
            cnts[i] = in.readLong();
        }
        int g = 840 * 8;
        long[][] dp = new long[9][g];
        SequenceUtils.deepFill(dp, -(long) 1e18);
        dp[0][0] = 0;
        for (int i = 1; i <= 8; i++) {
            for (int j = 0; j < g; j++) {
                for (int k = 0; k * i < 840 && k * i <= j && k <= cnts[i]; k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - k * i] + (cnts[i] - k) * i / 840);
                }
            }
        }

        long ans = 0;
        for (int i = 0; i < g && i <= w; i++) {
            if (dp[8][i] < 0) {
                continue;
            }
            long local = i;
            long canUse = Math.min((w - local) / 840, dp[8][i]);
            local += 840 * canUse;
            ans = Math.max(ans, local);
        }

        out.println(ans);
    }

}
