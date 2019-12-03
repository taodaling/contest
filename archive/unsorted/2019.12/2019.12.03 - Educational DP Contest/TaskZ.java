package contest;

import template.algo.LeqSlopeOptimizer;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskZ {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long c = in.readLong();
        long[] h = new long[n];
        for (int i = 0; i < n; i++) {
            h[i] = in.readInt();
        }
        LeqSlopeOptimizer optimizer = new LeqSlopeOptimizer(n);
        long[] dp = new long[n];
        dp[0] = 0;
        optimizer.add(dp[0] + h[0] * h[0], h[0], 0);
        for (int i = 1; i < n; i++) {
            int last = optimizer.getBestChoice(2 * h[i]);
            dp[i] = dp[last] + (h[i] - h[last]) * (h[i] - h[last]) + c;
            optimizer.add(dp[i] + h[i] * h[i], h[i], i);
        }
        out.println(dp[n - 1]);
    }
}
