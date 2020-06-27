package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class MaximumIndependentSet {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        boolean[][] edges = new boolean[n][n];
        for (int i = 0; i < m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            edges[u][v] = true;
        }
        long[] weights = new long[n];
        Arrays.fill(weights, 1);
        boolean[] selections = new boolean[n];

        long ans = template.problem.MaximumIndependentSet.solve(edges, weights, selections);
        out.println(ans);
        for (int i = 0; i < n; i++) {
            if (selections[i]) {
                out.append(i).append(' ');
            }
        }
    }
}
