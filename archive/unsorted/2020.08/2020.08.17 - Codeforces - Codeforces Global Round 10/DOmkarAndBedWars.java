package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DOmkarAndBedWars {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] dirs = new int[n];
        for (int i = 0; i < n; i++) {
            dirs[i] = in.readChar() == 'L' ? 0 : 1;
        }

        int ans = inf;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                dirs[0] ^= i;
                dirs[1] ^= j;

                int contrib = dp(dirs) + i + j;
                ans = Math.min(ans, contrib);

                dirs[0] ^= i;
                dirs[1] ^= j;
            }
        }

        out.println(ans);
    }


    int inf = (int) 1e8;

    public boolean same(int i, int j, int k) {
        return i == j && j == k;
    }

    public int dp(int[] dirs) {
        int n = dirs.length;
        int[][][] dp = new int[2][2][n];
        SequenceUtils.deepFill(dp, inf);
        dp[dirs[0]][dirs[1]][1] = 0;
        for (int i = 1; i < n - 1; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    for (int t = 0; t < 2; t++) {
                        int c = Math.abs(dirs[i + 1] - t);
                        if (same(j, k, t)) {
                            continue;
                        }
                        dp[k][t][i + 1] = Math.min(dp[k][t][i + 1], c + dp[j][k][i]);
                    }
                }
            }
        }

        int ans = inf;
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                if (same(j, k, dirs[0]) || same(k, dirs[0], dirs[1])) {
                    continue;
                }
                ans = Math.min(ans, dp[j][k][n - 1]);
            }
        }
        return ans;
    }
}
