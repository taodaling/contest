package on2020_10.on2020_10_25_CSES___CSES_Problem_Set.Rectangle_Cutting;



import template.io.FastInput;
import template.utils.SequenceUtils;

import java.io.PrintWriter;

public class RectangleCutting {
    int[][] dp;

    int dp(int a, int b) {
        if (dp[a][b] == -1) {
            if (a == b) {
                return dp[a][b] = 0;
            }
            dp[a][b] = (int) 1e9;
            for (int k = 1; k < a; k++) {
                dp[a][b] = Math.min(dp[a][b], dp(k, b) + dp(a - k, b) + 1);
            }
            for (int k = 1; k < b; k++) {
                dp[a][b] = Math.min(dp[a][b], dp(a, k) + dp(a, b - k) + 1);
            }
        }
        return dp[a][b];
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int a = in.readInt();
        int b = in.readInt();
        dp = new int[a + 1][b + 1];
        SequenceUtils.deepFill(dp, -1);
        int ans = dp(a, b);
        out.println(ans);
    }
}
