package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;

public class EBearAndDestroyingSubtrees {
    double[][] dp;
    int[] p;
    int limit = 60;

    public void revoke(int node, int h) {
        int fa = p[node];
        if (fa == -1) {
            return;
        }
        if (h + 1 < limit) {
            revoke(fa, h + 1);
        }
        int i = h;
        double prob = (1 + (i == 0 ? 0 : dp[node][i - 1])) / 2;
        dp[fa][i] /= prob;
    }

    public void apply(int node, int h) {
        int fa = p[node];
        if (fa == -1) {
            return;
        }
        int i = h;
        double prob = (1 + (i == 0 ? 0 : dp[node][i - 1])) / 2;
        dp[fa][i] *= prob;
        if (h + 1 < limit) {
            apply(fa, h + 1);
        }
    }

    public double[] newVertex() {
        double[] ans = new double[limit];
        Arrays.fill(ans, 1);
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        p = new int[q + 1];
        p[0] = -1;
        dp = new double[q + 1][];
        dp[0] = newVertex();
        int wpos = 1;
        Debug debug = new Debug(true);
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                p[wpos] = in.ri() - 1;
                dp[wpos] = newVertex();
                revoke(p[wpos], 1);
                apply(wpos, 0);
                wpos++;
                debug.debugArray("dp[0]", dp[0]);
            } else {
                double exp = 0;
                int node = in.ri() - 1;
                for (int j = 0; j < limit; j++) {
                    double prob = dp[node][j] - (j == 0 ? 0 : dp[node][j - 1]);
                    exp += prob * j;
                }
                out.println(exp);
            }
        }
    }
}
