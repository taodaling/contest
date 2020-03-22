package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class UOJ017 {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        int[][] dp = new int[n + 1][m + 1];

        int inf = (int) 1e9;
        SequenceUtils.deepFill(dp, (int) 1e9);
        Arrays.fill(dp[0], 1, m + 1, 0);
        int[] xs = new int[n];
        int[] ys = new int[n];
        int[] top = new int[n + 1];
        int[] bot = new int[n + 1];

        for (int i = 0; i < n; i++) {
            xs[i] = in.readInt();
            ys[i] = in.readInt();
        }

        int[] cnt = new int[n];
        Arrays.fill(top, m + 1);
        Arrays.fill(bot, 0);
        for (int i = 0; i < k; i++) {
            int p = in.readInt();
            int l = in.readInt();
            int h = in.readInt();
            bot[p] = l;
            top[p] = h;
            cnt[p]++;
        }

        debug.debug("bot", bot);
        debug.debug("top", top);
        IntegerPreSum ips = new IntegerPreSum(cnt);

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                int next = Math.min(j + xs[i - 1], m);
                dp[i][next] = Math.min(dp[i][next], dp[i - 1][j] + 1);
            }

            for (int j = 0; j <= m; j++) {
                if (j >= xs[i - 1]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j - xs[i - 1]] + 1);
                }
            }

            for (int j = 0; j <= m; j++) {
                if (j >= ys[i - 1]) {
                    dp[i][j - ys[i - 1]] = Math.min(dp[i][j - ys[i - 1]], dp[i - 1][j]);
                }
            }

            for (int j = 0; j <= bot[i] && j <= m; j++) {
                dp[i][j] = inf;
            }

            for (int j = top[i]; j <= m; j++) {
                dp[i][j] = inf;
            }
        }
        debug.debug("dp", dp);

        for (int i = n; i >= 0; i--) {
            int min = inf;
            for (int j = 0; j <= m; j++) {
                min = Math.min(min, dp[i][j]);
            }
            if (min < inf && i == n) {
                out.println(1).println(min);
                return;
            }
            if (min < inf) {
                out.println(0).println(ips.prefix(i));
                return;
            }
        }
    }
}
