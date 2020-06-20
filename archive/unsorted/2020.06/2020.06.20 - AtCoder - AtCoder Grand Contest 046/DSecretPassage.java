package contest;

import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Modular;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class DSecretPassage {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int n = s.length;
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }

        Modular mod = new Modular(998244353);
        Combination comb = new Combination(n, mod);

        int[] cnts = new int[2];
        for (int i = 0; i < n; i++) {
            cnts[s[i]]++;
        }
        char[] rev = s.clone();
        SequenceUtils.reverse(rev);
        IntegerPreSum ps = new IntegerPreSum(i -> rev[i], n);
        int a = cnts[0];
        int b = cnts[1];
        int[][][] dp = new int[a + 1][b + 1][n + 1];
        dp[0][0][0] = 1;
        for (int i = 0; i <= a; i++) {
            for (int j = 0; j <= b; j++) {
                for (int k = 0; k <= i + j; k++) {
                    int plus = dp[i][j][k];
                    if (plus == 0) {
                        continue;
                    }
                    //add 0
                    if (i + 1 <= a) {
                        int next = k;
                        if (next < n && rev[next] == 0) {
                            next++;
                        }
                        dp[i + 1][j][next] = mod.plus(dp[i + 1][j][next], plus);
                    }
                    //add 1
                    if (j + 1 <= b) {
                        int next = k;
                        if (next < n && rev[next] == 1) {
                            next++;
                        }
                        dp[i][j + 1][next] = mod.plus(dp[i][j + 1][next], plus);
                    }
                }
            }
        }

        for (int i = 0; i <= a; i++) {
            for (int j = 0; j <= b; j++) {
                for (int k = n - 1; k >= 0; k--) {
                    dp[i][j][k] = mod.plus(dp[i][j][k], dp[i][j][k + 1]);
                }
            }
        }

        //debug.debug("dp", dp);

        int ans = 0;

        boolean[][][] maybe = new boolean[n + 1][n + 1][n + 1];
        maybe[0][0][0] = true;
        for (int i = 0; i <= n; i++) {
            for (int j = n; j >= 0; j--) {
                for (int k = n; k >= 0; k--) {
                    if (!maybe[i][j][k]) {
                        continue;
                    }
                    //get first
                    if (i + 2 <= n) {
                        int head = s[i];
                        int next = s[i + 1];
                        maybe[i + 2][j + 1 - head][k + head] = true;
                        maybe[i + 2][j + 1 - next][k + next] = true;
                    }
                    //pop first
                    if (j + k > 0 && i + 1 <= n) {
                        maybe[i + 1][j][k] = true;
                    }
                    //pop 0
                    if (j > 0 && i + 1 <= n) {
                        int head = s[i];
                        maybe[i + 1][j - 1 + 1 - head][k + head] = true;
                    }
                    //pop 1
                    if (k > 0 && i + 1 <= n) {
                        int head = s[i];
                        maybe[i + 1][j + 1 - head][k - 1 + head] = true;
                    }
                    //just pop
                    if (j + k >= 2) {
                        if (j > 0) {
                            maybe[i][j - 1][k] = true;
                        }
                        if (k > 0) {
                            maybe[i][j][k - 1] = true;
                        }
                    }
                }
            }
        }


        //debug.debug("maybe", maybe);
        int[][] min = new int[n + 1][n + 1];
        SequenceUtils.deepFill(min, n + 1);
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= n; k++) {
                    if (!maybe[i][j][k]) {
                        continue;
                    }
                    int remain = n - i;
                    int c0 = remain - ps.prefix(remain - 1);
                    int c1 = ps.prefix(remain - 1);
                    c0 += j;
                    c1 += k;
                    min[c0][c1] = Math.min(min[c0][c1], remain);
                }
            }
        }

        for (int j = 0; j <= n; j++) {
            for (int k = 0; k <= n; k++) {
                if (min[j][k] >= n + 1) {
                    continue;
                }


                int remain = min[j][k];

                debug.debug("remain", remain);
                debug.debug("j", j);
                debug.debug("k", k);
                debug.debug("total", j + k + remain);
                int c0 = j;
                int c1 = k;
                int way = dp[c0][c1][remain];
                debug.debug("way", way);
                ans = mod.plus(ans, dp[c0][c1][remain]);
            }
        }

        out.println(ans);
    }
}
