package on2021_04.on2021_04_21_Codeforces___Codeforces_Round__201__Div__1_.B__Lucky_Common_Subsequence;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.AhoCorasick;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Locale;

public class BLuckyCommonSubsequence {

    int shift = 1000;
    int[][][] prev;
    int[][][] dp;
    int end;

    public void update(int i, int j, int k, int ni, int nj, int nk, int val) {
        if (nk == end) {
            return;
        }
        if (dp[ni][nj][nk] < val) {
            dp[ni][nj][nk] = val;
            prev[ni][nj][nk] = (i * shift + j) * shift + k;
        }
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] a = in.rs().toCharArray();
        char[] b = in.rs().toCharArray();
        char[] virus = in.rs().toCharArray();
        AhoCorasick ac = new AhoCorasick('A', 'Z', virus.length);
        ac.prepareBuild();
        for (char c : virus) {
            ac.build(c);
        }
        end = ac.buildLast;
        int[] topo = ac.endBuild();
        int[][] next = ac.next;
        int inf = (int) 1e9;
        int n = a.length;
        int m = b.length;

        prev = new int[n + 1][m + 1][topo.length];
        dp = new int[n + 1][m + 1][topo.length];
        SequenceUtils.deepFill(dp, -inf);
        dp[0][0][0] = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k < topo.length; k++) {
                    //both add
                    if (i < n && j < m && a[i] == b[j]) {
                        update(i, j, k, i + 1, j + 1, next[a[i] - 'A'][k], dp[i][j][k] + 1);
                    }
                    if (j < m) {
                        update(i, j, k, i, j + 1, k, dp[i][j][k]);
                    }
                    if (i < n) {
                        update(i, j, k, i + 1, j, k, dp[i][j][k]);
                    }
                }
            }
        }

        debug.debug("dp", dp);
        int best = 0;
        for (int i = 0; i < topo.length; i++) {
            if (dp[n][m][i] > dp[n][m][best]) {
                best = i;
            }
        }
        if (dp[n][m][best] == 0) {
            out.println(0);
            return;
        }

        //start from best
        StringBuilder res = new StringBuilder();
        int x = n;
        int y = m;
        while (x > 0 && y > 0) {
            int transfer = prev[x][y][best];
            int nk = transfer % shift;
            transfer /= shift;
            int ny = transfer % shift;
            transfer /= shift;
            int nx = transfer % shift;
            if (nx < x && ny < y) {
                res.append(a[x - 1]);
            }
            x = nx;
            y = ny;
            best = nk;
        }

        res.reverse();
        out.println(res.toString());
    }
}
