package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DRealityShow {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] l = new long[n];
        for (int i = 0; i < n; i++) {
            l[i] = in.readInt();
        }
        long[] s = new long[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.readInt();
        }
        SequenceUtils.reverse(l, 0, l.length - 1);
        SequenceUtils.reverse(s, 0, s.length - 1);
        debug.debug("l", l);
        debug.debug("s", s);


        long[] c = new long[n + m + 2];
        for (int i = 1; i <= n + m; i++) {
            c[i] = in.readInt();
        }

        int[] remain = new int[n];
        long[][] dp = new long[n][n + 2];
        SequenceUtils.deepFill(dp, -(long) 1e18);
        for (int i = 0; i < n; i++) {
            dp[i][0] = 0;
        }
        long[] best = new long[n + 2];
        for (int i = 1; i <= n + m; i++) {
            Arrays.fill(best, (long) -1e18);
            best[0] = 0;
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= remain[j] / 2; k++) {
                    dp[j][k] = Math.max(dp[j][k * 2], dp[j][k * 2 + 1]) + c[i] * k;
                }
                for (int k = remain[j] / 2 + 1; k <= remain[j]; k++) {
                    dp[j][k] = -(long) 1e18;
                }
                remain[j] /= 2;
                if (l[j] == i) {
                    for (int k = 0; k <= j + 1; k++) {
                        dp[j][k] = best[k];
                        if (k > 0) {
                            dp[j][k] = Math.max(dp[j][k], best[k - 1] - s[j] + c[i]);
                        }
                    }
                    remain[j] = j + 1;
                }

                for (int k = 0; k <= remain[j]; k++) {
                    best[k] = Math.max(best[k], dp[j][k]);
                }
            }

            debug.debug("i", i);
            debug.debug("dp", dp);
            debug.debug("best", best);
        }

        long ans = 0;
        for (int i = 0; i <= n; i++) {
            ans = Math.max(ans, best[i]);
        }

        out.println(ans);
    }
}
