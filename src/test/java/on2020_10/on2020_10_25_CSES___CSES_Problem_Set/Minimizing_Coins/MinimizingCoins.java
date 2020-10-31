package on2020_10.on2020_10_25_CSES___CSES_Problem_Set.Minimizing_Coins;



import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class MinimizingCoins {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] c = new int[n];
        in.populate(c);
        long[] dp = new long[x + 1];
        dp[0] = 1;
        int mod = (int)1e9 + 7;
        for (int i = 1; i <= x; i++) {
            for (int t : c) {
                if (i < t) {
                    continue;
                }
                dp[i] += dp[i - t];
            }
            dp[i] %= mod;
        }
        out.println(dp[x]);
    }
}
