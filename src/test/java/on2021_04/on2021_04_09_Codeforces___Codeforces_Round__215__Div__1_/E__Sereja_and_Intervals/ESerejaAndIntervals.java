package on2021_04.on2021_04_09_Codeforces___Codeforces_Round__215__Div__1_.E__Sereja_and_Intervals;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import javax.swing.*;

public class ESerejaAndIntervals {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int x = in.ri();

        long ans;
        assert solveN(n, m, x) == solveM(n, m, x);
        if (n >= m) {
            ans = solveN(n, m, x);
        } else {
            ans = solveM(n, m, x);
        }

        for (int i = 1; i <= n; i++) {
            ans = ans * i % mod;
        }
        out.println(ans);
    }

    public long solveN(int n, int m, int x) {
        long[][][] prev = new long[2][m + 1][m + 1];
        long[][][] next = new long[2][m + 1][m + 1];
        prev[0][0][0] = 1;
        for (int i = 1; i <= n; i++) {
            presum(prev[0]);
            presum(prev[1]);
            SequenceUtils.deepFill(next, 0L);
            for (int t = 0; t <= 1; t++) {
                for (int r = 1; r <= m; r++) {
                    for (int c = r; c <= m; c++) {
                        next[t][r][c] = prev[t][r - 1][c - 1];
                        if (r == x && t == 1) {
                            next[t][r][c] += prev[0][r - 1][c - 1];
                            if (next[t][r][c] >= mod) {
                                next[t][r][c] -= mod;
                            }
                        }
                    }
                }
            }
            long[][][] tmp = prev;
            prev = next;
            next = tmp;
        }

        presum(prev[1]);
        return prev[1][m][m];
    }

    Debug debug = new Debug(true);

    public long solveM(int n, int m, int x) {
        long[][] prev = new long[n + 1][n + 1];
        long[][] next = new long[n + 1][n + 1];
        prev[0][0] = 1;
        for (int i = 1; i <= m; i++) {
            debug.debug("i", i);
            debug.debug("prev", prev);
            SequenceUtils.deepFill(next, 0L);
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= j; k++) {
                    //j++
                    if (j + 1 <= n) {
                        next[j + 1][k] += prev[j][k];
                    }
                    if (i != x) {
                        if (k + 1 <= j) {
                            next[j][k + 1] += prev[j][k];
                        }
                        next[j][k] += prev[j][k];
                    }
                    if (j + 1 <= n) {
                        next[j + 1][k + 1] += prev[j][k];
                    }
                }
            }
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= n; k++) {
                    next[j][k] = DigitUtils.modWithoutDivision(next[j][k], mod);
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        debug.debug("i", m);
        debug.debug("prev", prev);

        return prev[n][n];
    }

    void presum(long[][] data) {
        int n = data.length;
        int m = data[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < m; j++) {
                data[i][j] += data[i][j - 1];
                if (data[i][j] >= mod) {
                    data[i][j] -= mod;
                }
            }
            if (i > 0) {
                for (int j = 0; j < m; j++) {
                    data[i][j] += data[i - 1][j];
                    if (data[i][j] >= mod) {
                        data[i][j] -= mod;
                    }
                }
            }
        }
    }
}

