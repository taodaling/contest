package contest;

public class NumberofFiboCalls {
    public int[] fiboCallsMade(int n) {
        int[][] dp = new int[Math.max(2, n + 1)][2];
        dp[0][0] = 1;
        dp[1][1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i - 2][j];
            }
        }
        return dp[n];
    }
}
