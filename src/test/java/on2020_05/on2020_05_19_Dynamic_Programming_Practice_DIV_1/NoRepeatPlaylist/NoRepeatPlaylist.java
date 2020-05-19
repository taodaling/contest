package on2020_05.on2020_05_19_Dynamic_Programming_Practice_DIV_1.NoRepeatPlaylist;



import template.math.Modular;
import template.utils.ArrayIndex;

public class NoRepeatPlaylist {
    public int numPlaylists(int N, int M, int P) {
        ArrayIndex ai = new ArrayIndex(P + 1, N + 1);
        int[] dp = new int[ai.totalSize()];
        Modular mod = new Modular(1e9 + 7);
        dp[ai.indexOf(0, 0)] = 1;
        for (int i = 1; i <= P; i++) {
            for (int j = 0; j <= N; j++) {
                if (j > 0) {
                    dp[ai.indexOf(i, j)] = mod.plus(dp[ai.indexOf(i, j)],
                            mod.mul(dp[ai.indexOf(i - 1, j - 1)], N - (j - 1)));
                }
                if (j >= M) {
                    dp[ai.indexOf(i, j)] = mod.plus(dp[ai.indexOf(i, j)],
                            mod.mul(dp[ai.indexOf(i - 1, j)], (j - M)));
                }
            }
        }

        int ans = dp[ai.indexOf(P, N)];
        return ans;
    }
}
