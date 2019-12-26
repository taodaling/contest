package contest;

import template.graph.LongMinimumCloseSubGraph;
import template.io.FastInput;
import template.io.FastOutput;

public class GPetyaAndGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long[] weights = new long[n + m];
        for (int i = 0; i < n; i++) {
            weights[i] = -in.readInt();
        }
        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = in.readInt() - 1;
            edges[i][1] = in.readInt() - 1;
            weights[i + n] = in.readInt();
        }
        LongMinimumCloseSubGraph graph = new LongMinimumCloseSubGraph(weights);
        for (int i = 0; i < m; i++) {
            graph.addDependency(i + n, edges[i][0]);
            graph.addDependency(i + n, edges[i][1]);
        }

        long ans = graph.solve();
        out.println(ans);
    }
}
