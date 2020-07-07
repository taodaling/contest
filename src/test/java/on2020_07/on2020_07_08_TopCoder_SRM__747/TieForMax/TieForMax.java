package on2020_07.on2020_07_08_TopCoder_SRM__747.TieForMax;



import template.utils.ArrayIndex;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class TieForMax {

    private double[][] comb = new double[100][100];

    public double comb(int n, int m) {
        if (comb[n][m] == -1) {
            if (n < m) {
                return comb[n][m] = 0;
            }
            if (m == 0) {
                return comb[n][m] = 1;
            }
            comb[n][m] = comb(n - 1, m - 1) + comb(n - 1, m);
        }
        return comb[n][m];
    }

    //Debug debug = new Debug(true);

    public double getProbability(int t, int p) {
        SequenceUtils.deepFill(comb, -1D);
        ArrayIndex ai = new ArrayIndex(p + 1, t + 1, t + 1, 2);
        double[] dp = new double[ai.totalSize()];
        dp[ai.indexOf(0, 0, 0, 0)] = 1;
        for (int i = 0; i < p; i++) {
            for (int j = 0; j <= t; j++) {
                for (int k = 0; k <= t; k++) {
                    for (int z = 0; z < 2; z++) {
                        double way = dp[ai.indexOf(i, j, k, z)];
                        if (way == 0) {
                            continue;
                        }
                        for (int x = 0; x + j <= t; x++) {
                            int nextK = Math.max(k, x);
                            int nextZ = k > x ? z : k == x ? 1 : 0;
                            dp[ai.indexOf(i + 1, j + x, nextK, nextZ)] += way * comb(t - j, x);
                        }
                    }
                }
            }
        }

        double ans = 0;
        for (int i = 0; i <= t; i++) {
            ans += dp[ai.indexOf(p, t, i, 1)];
        }

        double total = Math.pow(p, t);
//        debug.debug("ans", ans);
//        debug.debug("total", total);
        return ans / total;
    }
}
