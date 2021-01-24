package contest;

import template.algo.LeqSlopeOptimizer;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class SubarraySquares {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        long[] x = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            x[i] = in.ri();
            x[i] += x[i - 1];
        }
        long inf = (long) 1e18;
        long[] prev = new long[n + 1];
        Arrays.fill(prev, inf);
        prev[0] = 0;
        long[] next = new long[n + 1];
        LeqSlopeOptimizer optimizer = new LeqSlopeOptimizer(n);
        for (int i = 0; i < k; i++) {
            Arrays.fill(next, inf);
            optimizer.clear();
            next[0] = 0;
            for (int j = 1; j <= n; j++) {
                optimizer.add(prev[j - 1] + x[j - 1] * x[j - 1], 2 * x[j - 1], j - 1);
                int opt = optimizer.getBestChoice(x[j]);
                next[j] = prev[opt] + (x[j] - x[opt]) * (x[j] - x[opt]);
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }

        long ans = prev[n];
        out.println(ans);
    }
}
