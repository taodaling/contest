package on2021_04.on2021_04_23_Codeforces___Contest_2050_and_Codeforces_Round__718__Div__1___Div__2_.D__Explorer_Space;



import template.io.FastInput;
import template.io.FastOutput;

public class DExplorerSpace {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int[][] lr = new int[n][m - 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m - 1; j++) {
                lr[i][j] = in.ri();
            }
        }
        int[][] ud = new int[n - 1][m];
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m; j++) {
                ud[i][j] = in.ri();
            }
        }

        if (k % 2 == 1) {
            for(int i = 0; i < n; i++){
                for(int j = 0; j < m; j++){
                    out.append(-1).append(' ');
                }
                out.println();
            }
            return;
        }

        k /= 2;
        int inf = (int) 1e9;
        int[][][] dp = new int[k + 1][n][m];
        for (int i = 1; i <= k; i++) {
            for (int j = 0; j < n; j++) {
                for (int t = 0; t < m; t++) {
                    dp[i][j][t] = inf;
                    //left
                    if (t > 0) {
                        dp[i][j][t] = Math.min(dp[i][j][t], dp[i - 1][j][t - 1] + lr[j][t - 1]);
                    }
                    //right
                    if (t + 1 < m) {
                        dp[i][j][t] = Math.min(dp[i][j][t], dp[i - 1][j][t + 1] + lr[j][t]);
                    }
                    //bot
                    if (j + 1 < n) {
                        dp[i][j][t] = Math.min(dp[i][j][t], dp[i - 1][j + 1][t] + ud[j][t]);
                    }
                    //top
                    if (j > 0) {
                        dp[i][j][t] = Math.min(dp[i][j][t], dp[i - 1][j - 1][t] + ud[j - 1][t]);
                    }
                }
            }
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                out.append(dp[k][i][j] * 2).append(' ');
            }
            out.println();
        }
    }
}
