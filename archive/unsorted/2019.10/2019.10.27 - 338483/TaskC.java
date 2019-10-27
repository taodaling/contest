package contest;

import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

import java.util.ArrayList;
import java.util.List;

public class TaskC {
    int n;
    int m;
    int[][][] dp;
    List<int[]>[] constraints;
    NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        constraints = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            constraints[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            int l = in.readInt();
            int r = in.readInt();
            int x = in.readInt();
            constraints[r].add(new int[]{l, r, x});
        }
        dp = new int[n + 2][n + 1][n + 1];
        dp[1][0][0] = 3;
        for (int i = 1; i <= n; i++) {
            for (int[] c : constraints[i]) {
                for (int j = 0; j <= n; j++) {
                    for (int k = 0; k <= n; k++) {
                        int cnt = 1;
                        if (j >= c[0]) {
                            cnt++;
                        }
                        if (k >= c[0]) {
                            cnt++;
                        }
                        if (cnt != c[2]) {
                            dp[i][j][k] = 0;
                        }
                    }
                }
            }
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= n; k++) {
                    int plus = dp[i][j][k];
                    dp[i + 1][j][k] = mod.plus(dp[i + 1][j][k], plus);
                    if (j == 0) {
                        dp[i + 1][i][k] = mod.plus(dp[i + 1][i][k], plus);
                        dp[i + 1][i][k] = mod.plus(dp[i + 1][i][k], plus);
                    } else {
                        dp[i + 1][i][k] = mod.plus(dp[i + 1][i][k], plus);
                    }
                    if(j > 0) {
                        dp[i + 1][i][j] = mod.plus(dp[i + 1][i][j], plus);
                    }
                }
            }
        }

        int ans = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (dp[n][i][j] == 0) {
                    continue;
                }
                ans = mod.plus(ans, dp[n][i][j]);
            }
        }

        out.println(ans);
    }
}
