package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class BGlassHalfSpilled {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] ab = new int[n][2];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            ab[i][0] = in.ri();
            sum += ab[i][0];
            ab[i][1] = in.ri();
        }
        int[][] prev = new int[n + 1][sum + 1];
        int[][] next = new int[n + 1][sum + 1];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(prev, -inf);
        prev[0][0] = 0;
        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debug("prev", prev);
            int a = ab[i][0];
            int b = ab[i][1];
            SequenceUtils.deepFill(next, -inf);
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= sum; k++) {
                    //add
                    if (j + 1 <= n && k + a <= sum) {
                        next[j + 1][k + a] = Math.max(next[j + 1][k + a], prev[j][k] + b * 2);
                    }
                    //not add
                    next[j][k] = Math.max(next[j][k], prev[j][k] + b);
                }
            }
            int[][] tmp = prev;
            prev = next;
            next = tmp;
        }
        debug.debug("prev", prev);
        int[] ans = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= sum; j++) {
                int contrib = Math.min(j * 2, prev[i][j]);
                ans[i] = Math.max(ans[i], contrib);
            }
        }
        debug.debugArray("ans", ans);
        for(int i = 1; i <= n; i++){
            out.append(ans[i] / 2.0).append(' ');
        }
    }
}
