package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongDinic;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;

import java.util.List;

public class PoliceChase {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<LongFlowEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            LongFlow.addFlowEdge(g, a, b, 1);
            LongFlow.addFlowEdge(g, b, a, 1);
        }
        LongDinic dinic = new LongDinic();
        long ans = dinic.apply(g, 0, n - 1, (long) 1e18);
        out.println(ans);
        boolean[] set = LongFlow.findSetST(g, 0);
        LongFlow.findMinimumCut(g, set, (node, edge) -> {
            out.append(node + 1).append(' ').append(edge.to  + 1).println();
        });
    }
}
