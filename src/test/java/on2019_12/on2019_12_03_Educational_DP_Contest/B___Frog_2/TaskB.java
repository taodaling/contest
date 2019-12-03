package on2019_12.on2019_12_03_Educational_DP_Contest.B___Frog_2;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = in.readInt();
        }
        int[] dp = new int[n];
        dp[0] = 0;
        for (int i = 1; i < n; i++) {
            dp[i] = (int) 1e9;
            for (int j = i - 1; j >= 0 && i - j <= k; j--) {
                dp[i] = Math.min(dp[i], dp[j] + Math.abs(h[i] - h[j]));
            }
        }
        out.println(dp[n - 1]);
    }
}
