package on2020_04.on2020_04_25_AtCoder___AtCoder_Regular_Contest_097.E___Sorted_and_Sorted;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class ESortedAndSorted {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] T = new int[n * 2 + 1];
        int[] A = new int[n * 2 + 1];
        int[][] invIndex = new int[2][n + 1];
        for (int i = 1; i <= n * 2; i++) {
            T[i] = in.readChar() == 'B' ? 0 : 1;
            A[i] = in.readInt();
            invIndex[T[i]][A[i]] = i;
        }


        int[][][] ps = new int[2][n * 2 + 1][n + 1];
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= n * 2; j++) {
                System.arraycopy(ps[i][j - 1], 0, ps[i][j], 0, n + 1);
                if (T[j] != i) {
                    continue;
                }
                ps[i][j][A[j]] = 1;
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j <= n * 2; j++) {
                for (int k = 1; k <= n; k++) {
                    ps[i][j][k] += ps[i][j][k - 1];
                }
            }
        }

        int inf = (int) 1e9;
        int[][] dp = new int[n * 2 + 1][n + 1];
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 1; i <= 2 * n; i++) {
            for (int j = 0; j <= n && j <= i; j++) {
                int b = j;
                int w = i - j;

                if (w < 0 || w > n) {
                    continue;
                }

                if (b > 0) {
                    //put B
                    int index = invIndex[0][j];
                    int cross = index - 1 - (ps[0][index - 1][j] + ps[1][index - 1][w]);
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1] + cross);
                }
                if (w > 0) {
                    int index = invIndex[1][w];
                    int cross = index - 1 - (ps[0][index - 1][b] + ps[1][index - 1][w]);
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + cross);
                }
            }
        }

        debug.debug("ps", ps);
        debug.debug("dp", dp);
        int ans = dp[n * 2][n];
        out.println(ans);
    }
}
