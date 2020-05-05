package contest;

import template.math.GCDs;
import template.math.Modular;

public class EraseToGCD {
    public int countWays(int[] S, int goal) {
        int limit = 1000;
        int n = S.length;
        int[][] dp = new int[n + 1][limit + 1];
        dp[0][0] = 1;
        Modular mod = new Modular(1e9 + 7);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= limit; j++) {
                int g = GCDs.gcd(S[i], j);
                dp[i + 1][g] = mod.plus(dp[i + 1][g], dp[i][j]);
                dp[i + 1][j] = mod.plus(dp[i + 1][j], dp[i][j]);
            }
        }
        return dp[n][goal];
    }
}
