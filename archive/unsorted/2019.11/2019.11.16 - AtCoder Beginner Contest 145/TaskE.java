package contest;

import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

import java.util.Arrays;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int t = in.readInt();
        int[][] dp = new int[n + 1][t + 1];
        int[] hasRemain = new int[n + 1];
        hasRemain[0] = 0;

        int[][] ab = new int[n + 1][2];
        for(int i = 1; i <= n; i++){
            ab[i][0] = in.readInt();
            ab[i][1] = in.readInt();
        }
        Arrays.sort(ab, 1, n + 1, (a, b) -> a[0] - b[0]);

        for (int i = 1; i <= n; i++) {
            int a = ab[i][0];
            int b = ab[i][1];
            dp[i][0] = 0;
            for (int j = 0; j <= t; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - a >= 0) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - a] + b);
                }
            }
            dp[i][t] = Math.max(dp[i][t], hasRemain[i - 1] + b);

            hasRemain[i] = hasRemain[i - 1];
            for(int j = 0; j < t; j++){
                hasRemain[i] = Math.max(hasRemain[i], dp[i][j]);
            }
        }

        int mx = 0;
        for(int i = 0; i <= t; i++){
            mx = Math.max(mx, dp[n][i]);
        }
        out.println(mx);
    }
}
