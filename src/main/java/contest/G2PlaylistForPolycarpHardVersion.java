package contest;

import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

public class G2PlaylistForPolycarpHardVersion {
    Modular mod = new Modular(1e9 + 7);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        IntList[] musics = new IntList[4];
        for (int i = 1; i <= 3; i++) {
            musics[i] = new IntList(n);
        }
        for (int i = 0; i < n; i++) {
            int t = in.readInt();
            int g = in.readInt();
            musics[g].add(t);
        }

        int[] dp1 = dp(musics[1], m);
        int[] dp2 = dp(musics[2], m);
        int[] dp3 = dp(musics[3], m);
    }

    public int[] merge(int[][] a, int[][] b) {

    }

    public int[][] dp(IntList list, int m) {
        int[] lens = list.toArray();
        int n = lens.length;
        int[][][] dp = new int[n + 1][n + 1][m + 1];
        dp[0][0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int k = 0; k <= n; k++) {
                for (int j = 0; j <= m; j++) {
                    dp[i][k][j] = dp[i - 1][k][j];
                    if (j - lens[i] >= 0 && k > 0) {
                        dp[i][k][j] = mod.plus(dp[i][k][j], dp[i - 1][k - 1][j - lens[i]]);
                    }
                }
            }
        }
        return dp[n];
    }
}
