package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        int mx = n * n * 4;
        int[][][] dp = new int[n + 1][n + 1][mx + 1];
        dp[0][0][mx / 2] = 1;

        NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
        NumberTheory.Composite comp = new NumberTheory.Composite(n, mod);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                for (int t = 0; t <= mx; t++) {
                    if (dp[i][j][t] == 0) {
                        continue;
                    }
                    int unmatched = j;
                    dp[i + 1][j][t] = mod.plus(dp[i + 1][j][t], dp[i][j][t]);
                    dp[i + 1][j][t] = mod.plus(dp[i + 1][j][t], mod.mul(comp.composite(unmatched, 1), dp[i][j][t]));
                    dp[i + 1][j][t] = mod.plus(dp[i + 1][j][t], mod.mul(comp.composite(unmatched, 1), dp[i][j][t]));
                    dp[i + 1][j + 1][t - 2 * (i + 1)] = mod.plus(dp[i + 1][j + 1][t - 2 * (i + 1)], dp[i][j][t]);
                    if(j > 0) {
                        dp[i + 1][j - 1][t + 2 * (i + 1)] = mod.plus(dp[i + 1][j - 1][t + 2 * (i + 1)], mod.mul(comp.composite(unmatched, 1), mod.mul(comp.composite(unmatched, 1), dp[i][j][t])));
                    }
                }
            }
        }

        out.println(dp[n][0][mx / 2 + k]);
    }
}
