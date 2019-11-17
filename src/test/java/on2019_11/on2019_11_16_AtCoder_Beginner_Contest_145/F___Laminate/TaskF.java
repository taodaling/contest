package on2019_11.on2019_11_16_AtCoder_Beginner_Contest_145.F___Laminate;



import template.DiscreteMap;
import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] hs = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            hs[i] = in.readInt();
        }

        DiscreteMap dm = new DiscreteMap(hs.clone());
        for (int i = 0; i <= n; i++) {
            hs[i] = dm.rankOf(hs[i]);
        }

        int m = dm.maxRank();
        long[][][] dp = new long[n + 1][k + 1][m + 1];
        SequenceUtils.deepFill(dp, (long) 1e13);
        dp[0][0][0] = 0;
        long[][][] prefix = new long[n + 1][k + 1][m + 1];
        long[][][] suffix = new long[n + 1][k + 1][m + 1];
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= m; j++) {
                if (j == 0) {
                    prefix[0][i][j] = dp[0][i][j];
                } else {
                    prefix[0][i][j] = Math.min(prefix[0][i][j - 1] + dm.iThElement(j) - dm.iThElement(j - 1), dp[0][i][j]);
                }
            }

            for (int j = m; j >= 0; j--) {
                if (j == m) {
                    suffix[0][i][j] = dp[0][i][j];
                } else {
                    suffix[0][i][j] = Math.min(suffix[0][i][j + 1], dp[0][i][j]);
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            int h = hs[i];
            for (int j = 0; j <= k; j++) {
                for (int t = 0; t <= m; t++) {
                    if (t != h) {
                        if (j > 0) {
                            dp[i][j][t] = Math.min(prefix[i - 1][j - 1][t], dp[i][j][t]);
                            dp[i][j][t] = Math.min(suffix[i - 1][j - 1][t], dp[i][j][t]);
                        } else {

                        }
                    } else {
                        dp[i][j][t] = Math.min(prefix[i - 1][j][t], dp[i][j][t]);
                        dp[i][j][t] = Math.min(suffix[i - 1][j][t], dp[i][j][t]);
                    }
                }
            }

            for (int ii = 0; ii <= k; ii++) {
                for (int jj = 0; jj <= m; jj++) {
                    if (jj == 0) {
                        prefix[i][ii][jj] = dp[i][ii][jj];
                    } else {
                        prefix[i][ii][jj] = Math.min(prefix[i][ii][jj - 1] + dm.iThElement(jj) - dm.iThElement(jj - 1), dp[i][ii][jj]);
                    }
                }
                for (int jj = m; jj >= 0; jj--) {
                    if (jj == m) {
                        suffix[i][ii][jj] = dp[i][ii][jj];
                    } else {
                        suffix[i][ii][jj] = Math.min(suffix[i][ii][jj + 1], dp[i][ii][jj]);
                    }
                }


            }
        }

        long ans = (long) 1e18;
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= m; j++) {
                ans = Math.min(ans, dp[n][i][j]);
            }
        }

        out.println(ans);
    }
}
