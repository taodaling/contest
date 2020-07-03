package contest;


import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class AbstractThinking {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();

        long[][][][][][][] dp = new long[n + 1][3][3][3][2][2][2];
        dp[0][0][0][0][0][0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int t = 0; t < 3; t++) {
                        for (int jk = 0; jk < 2; jk++) {
                            for (int kt = 0; kt < 2; kt++) {
                                for (int occur = 0; occur < 2; occur++) {
                                    long way = dp[i][j][k][t][jk][kt][occur];
                                    if (way == 0) {
                                        continue;
                                    }
                                    for (int a = 0; a < 2 && j + a < 3; a++) {
                                        for (int b = 0; b < 2 && k + b < 3; b++) {
                                            for (int c = 0; c < 2 && t + c < 3; c++) {
                                                if (!(j + a >= k + b && k + b >= t + c)) {
                                                    continue;
                                                }
                                                if ((j + a) - (t + c) > 1) {
                                                    continue;
                                                }
                                                //can't do it together
                                                if(a == 1 && b == 1 && jk == 1){
                                                    continue;
                                                }
                                                if(b == 1 && c == 1 && kt == 1){
                                                    continue;
                                                }
                                                int njk = jk;
                                                if (a == 0 && j == 1 && b == 1) {
                                                    njk = 0;
                                                }
                                                if (a == 1 && j == 0 && b == 1) {
                                                    njk = 1;
                                                }
                                                int nkt = kt;
                                                if (b == 0 && k == 1 && c == 1) {
                                                    nkt = 0;
                                                }
                                                if (b == 1 && k == 0 && c == 1) {
                                                    nkt = 1;
                                                }

                                                int noccur = occur;
                                                if (j == 1 && a == 1 && k == 1 && b == 0 && jk == 0) {
                                                    noccur = 1;
                                                }
                                                if (k == 1 && b == 1 && t == 1 && c == 0 && kt == 0) {
                                                    noccur = 1;
                                                }
                                                if (a == 1 && j == 1 && t == 1 && c == 0 && (jk == 0 || kt == 0)) {
                                                    noccur = 1;
                                                }

                                                dp[i + 1][j + a][k + b][t + c][njk][nkt][noccur] += way;
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }

        long ans = 0;
        debug.debug("dp", dp);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                ans += dp[n][2][2][2][i][j][1];
            }
        }
        debug.debug("ans", ans);
        out.println(ans);
    }

    public int min(int a, int b, int c) {
        int ans = Math.min(a, b);
        ans = Math.min(ans, c);
        return ans;
    }

    public int max(int a, int b, int c) {
        int ans = Math.max(a, b);
        ans = Math.max(ans, c);
        return ans;
    }

    public long comb(long n, long m) {
        if (m > n || m < 0) {
            return 0;
        }
        return m == 0 ? 1 : (comb(n - 1, m - 1) * n / m);
    }

}
