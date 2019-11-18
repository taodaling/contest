package on2019_11.on2019_11_18_AtCoder_Grand_Contest_032.D___Rotation_Sort;



import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int b = in.readInt();
        int a = in.readInt();
        int[] perm = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            perm[i] = in.readInt();
        }
        int[] rank = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            rank[i] = 0;
            for (int j = 1; j < i; j++) {
                if (perm[j] > perm[i]) {
                    rank[i]++;
                }
            }
        }

        long[][] dp = new long[n + 1][n + 1];
        SequenceUtils.deepFill(dp, (long)1e18);
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= n; j++) {
                if (dp[i][j] == (long)1e18) {
                    continue;
                }
                if (rank[i + 1] == 0) {
                    dp[i + 1][0] = Math.min(dp[i + 1][0], dp[i][j]);
                }
                dp[i + 1][Math.min(rank[i + 1], j)] = Math.min(dp[i + 1][Math.min(rank[i + 1], j)], dp[i][j] + a);
                if (j >= rank[i + 1]) {
                    dp[i + 1][rank[i + 1]] = Math.min(dp[i + 1][rank[i + 1]], dp[i][j]);
                    dp[i + 1][j + 1] = Math.min(dp[i + 1][j + 1], dp[i][j] + b);
                }
            }
        }

        long ans = (long)1e18;
        for(int i = 0; i <= n; i++){
            ans = Math.min(ans, dp[n][i]);
        }

        out.println(ans);
    }
}
