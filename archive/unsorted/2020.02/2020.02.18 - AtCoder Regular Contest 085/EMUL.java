package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.graph.LongISAP;
import template.graph.graph.LongMaximumCloseSubGraph;
import template.graph.graph.LongMaximumCloseSubGraphAdapter;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class EMUL {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] weights = new long[n];
        long sum = 0;
        for (int i = 0; i < n; i++) {
            weights[i] = -in.readInt();
            sum -= weights[i];
        }
        LongMaximumCloseSubGraph mcsg = new LongMaximumCloseSubGraphAdapter(
                new LongISAP(n + 2));
        boolean[] picked = new boolean[n];
        List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
        for (int i = 0; i < n; i++) {
            for (int j = (i + 1) * 2; j <= n; j += i + 1) {
                Graph.addEdge(g, i, j - 1);
            }
        }
        long ans = mcsg.maximumCloseSubGraph(g, weights, picked);
        out.println(sum + ans);
    }
}
