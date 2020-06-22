package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.ArrayIndex;

public class AFunctionsAgain {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        ArrayIndex ai = new ArrayIndex(n, 2);
        long[] dp = new long[ai.totalSize()];
        for (int i = n - 2; i >= 0; i--) {
            long contrib = Math.abs(a[i] - a[i + 1]);
            dp[ai.indexOf(i, 0)] = Math.min(dp[ai.indexOf(i, 0)], contrib - dp[ai.indexOf(i + 1, 1)]);
            dp[ai.indexOf(i, 1)] = Math.max(dp[ai.indexOf(i, 1)], contrib - dp[ai.indexOf(i + 1, 0)]);
        }

        long ans = 0;
        for(int i = 0; i < n; i++){
            ans = Math.max(ans, dp[ai.indexOf(i, 1)]);
        }

        out.println(ans);
    }
}
