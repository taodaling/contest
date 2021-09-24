package on2021_08.on2021_08_15_CS_Academy___Virtual_Beta_Round__8.Consecutive_Subsequence;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ConsecutiveSubsequence {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int L = (int) 1e6;
        int[][] dp = new int[2][n];
        int[][] reg = new int[2][L + 1];
        for (int i = 0; i < n; i++) {
            dp[0][i] = reg[0][a[i] - 1] + 1;
            dp[1][i] = reg[1][a[i] - 1] + 1;
            if (a[i] >= 2) {
                dp[1][i] = Math.max(dp[1][i], reg[0][a[i] - 2] + 2);
            }
            for (int j = 0; j < 2; j++) {
                reg[j][a[i]] = Math.max(reg[j][a[i]], dp[j][i]);
            }
        }
        int ans0 = Arrays.stream(dp[0]).max().orElse(-1) + 1;
        int ans1 = Arrays.stream(dp[1]).max().orElse(-1);
        out.println(Math.max(ans0, ans1));
    }
}
