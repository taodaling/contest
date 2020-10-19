package contest;

import template.io.FastInput;
import template.utils.Debug;

import java.io.PrintWriter;

public class DSquirrelMerchant {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[3];
        int[] b = new int[3];
        in.populate(a);
        in.populate(b);
        long ans = dp(n, a, b) + n;
        debug.debug("ans", ans);
        ans = dp((int) ans, b, a) + ans;
        debug.debug("ans", ans);
        out.println(ans);
    }

    public long dp(int n, int[] prev, int[] next) {
        long[] dp = new long[n + 1];
        long max = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 3; j++) {
                if (i >= prev[j]) {
                    dp[i] = Math.max(dp[i], dp[i - prev[j]] + next[j] - prev[j]);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }
}
