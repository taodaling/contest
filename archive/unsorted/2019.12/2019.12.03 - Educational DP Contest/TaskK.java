package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskK {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        boolean[] dp = new boolean[k + 1];
        dp[0] = false;
        for (int i = 1; i <= k; i++) {
            dp[i] = false;
            for (int x : a) {
                if (x <= i) {
                    dp[i] = dp[i] || !dp[i - x];
                }
            }
        }

        out.println(dp[k] ? "First" : "Second");
    }
}
