package on2019_10.on2019_10_13_Atcode_AGC005.TaskD;



import template.FastInput;
import template.NumberTheory;

import java.io.PrintWriter;
import java.util.List;

public class TaskD {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        NumberTheory.Modular mod = new NumberTheory.Modular(924844033);
        NumberTheory.Factorial fact = new NumberTheory.Factorial(2000, mod);

        int n = in.readInt();
        int k = in.readInt();

        //is last selected
        //which row
        //how many elements selected
        int[][][][] f = new int[2][2][n + 1][n + 1];
        f[1][0][0][1] = 0;
        f[1][0][0][0] = 1;
        f[0][0][0][1] = 0;
        f[0][0][0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                f[1][0][i][j] = mod.plus(f[1][1][i - 1][j], f[1][0][i - 1][j]);
                if (j > 0) {
                    f[1][1][i][j] = mod.plus(f[1][1][i - 1][j - 1], f[1][0][i - 1][j - 1]);
                }
                f[0][0][i][j] = mod.plus(f[0][1][i - 1][j], f[0][0][i - 1][j]);
                if (j > 0) {
                    f[0][1][i][j] = mod.plus(f[0][1][i - 1][j - 1], f[0][0][i - 1][j - 1]);
                }
                if (j > 0) {
                    f[1][0][i][j] = mod.plus(f[1][0][i][j], f[1][0][i - 1][j - 1]);
                    if (i > 1) {
                        f[0][0][i][j] = mod.plus(f[0][0][i][j], f[0][0][i - 1][j - 1]);
                    }
                }
            }
        }

        int limit = Math.min(2 * k, n);
        int[][] dp = new int[limit + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= limit; i++) {
            int m = (n - i) / (2 * k) + 1;
            int last = i + 2 * k * (m - 1);
            int type = 0;
            if (i - k >= 1) {
                type = 1;
            }
            for (int j = 0; j <= m; j++) {
                int cnt;
                if (last + k <= n) {
                    cnt = mod.plus(f[type][0][m][j], f[type][1][m][j]);
                } else {
                    cnt = f[type][0][m][j];
                }

                for (int t = 0; t + j <= n; t++) {
                    dp[i][t + j] = mod.plus(dp[i][t + j], mod.mul(dp[i - 1][t], cnt));
                }
            }
        }

        int ans = 0;
        //inclusive-exclusive
        for (int i = 0; i <= n; i++) {
            int cnt = 1;
            if (i % 2 == 1) {
                cnt = mod.valueOf(-1);
            }
            cnt = mod.mul(cnt, fact.fact(n - i));
            cnt = mod.mul(cnt, dp[limit][i]);

            ans = mod.plus(ans, cnt);
        }

        out.print(ans);
    }
}
