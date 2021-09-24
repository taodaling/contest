package on2021_09.on2021_09_17_Single_Round_Match_813.RaftingOnDunajec;



import template.math.Combination;
import template.math.DigitUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class RaftingOnDunajec {
    int mod = (int) 1e9 + 7;
    Combination comb = new Combination((int) 1e3, mod);

    public int count(int n, int m) {
        long[][] fp = new long[n + 1][m + 1];
        for (int i = 0; i <= n; i++) {
            fp[i][0] = 1;
            for (int j = 1; j <= m; j++) {
                fp[i][j] = fp[i][j - 1] * i % mod;
            }
        }
        long[][] fcomb = new long[m + 1][m + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= m; j++) {
                fcomb[i][j] = comb.combination(i, j);
            }
        }

        long[][][] prev = new long[2][n + 1][m + 1];
        long[][][] next = new long[2][n + 1][m + 1];
        prev[0][0][0] = 1;
        for (int s = 0; s < n; s++) {
            SequenceUtils.deepFill(next, 0L);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= n; j++) {
                    for (int k = 0; k <= m; k++) {
                        long way = prev[i][j][k];
                        if (way == 0) {
                            continue;
                        }
                        //empty
                        next[i ^ 1][0][k] += way;

                        if (j + 1 <= n) {
                            //j + 1
                            for (int t = 0; t + k <= m; t++) {
                                next[i][j + 1][t + k] +=
                                        way * fcomb[m - k][t] % mod
                                                * fp[j + 1][t] % mod;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= n; j++) {
                    for (int k = 0; k <= m; k++) {
                        next[i][j][k] = DigitUtils.modWithoutDivision(next[i][j][k], mod);
                    }
                }
            }
            long[][][] tmp = prev;
            prev = next;
            next = tmp;

//            debug.debug("s", s);
//            debug.debug("prev", prev);
        }

        long sum = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= n; j++) {
                int k = m;
                long way = prev[i][j][k];
                if (way == 0) {
                    continue;
                }
                long contrib = way;
                if (i == 1) {
                    contrib = -contrib;
                }
                sum += contrib;
            }
        }

        sum = DigitUtils.mod(sum, mod);
        return (int) sum;
    }

//    Debug debug = new Debug(false);
}
