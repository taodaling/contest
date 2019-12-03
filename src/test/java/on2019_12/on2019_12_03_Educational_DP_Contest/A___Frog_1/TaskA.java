package on2019_12.on2019_12_03_Educational_DP_Contest.A___Frog_1;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] h = new int[n];
        for (int i = 0; i < n; i++) {
            h[i] = in.readInt();
        }
        int[] dp = new int[n];
        dp[0] = 0;
        for (int i = 1; i < n; i++) {
            dp[i] = (int) 1e9;
            if (i > 0) {
                dp[i] = Math.min(dp[i], dp[i - 1] + Math.abs(h[i - 1] - h[i]));
            }
            if (i > 1) {
                dp[i] = Math.min(dp[i], dp[i - 2] + Math.abs(h[i - 2] - h[i]));
            }
        }

        out.println(dp[n - 1]);
    }
}
