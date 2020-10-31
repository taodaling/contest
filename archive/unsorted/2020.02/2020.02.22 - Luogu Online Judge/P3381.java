package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongMinimumCostFlow;

import java.util.List;

public class P3381 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        int t = in.readInt();
        List<LongCostFlowEdge>[] g = LongFlow.createCostFlow(n + 1);
        for (int i = 0; i < m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            int c = in.readInt();
            int w = in.readInt();
            LongFlow.addEdge(g, u, v, c, w);
        }

        LongMinimumCostFlow mcf = new LongMinCostFlowPolynomial();
        long[] ans = mcf.apply(g, s, t, (long) 2e18);
        out.append(ans[0]).append(' ').append(ans[1]);
    }
}
