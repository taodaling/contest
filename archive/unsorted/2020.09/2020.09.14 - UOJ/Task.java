package contest;

import template.graph.CapacityScalingMinimumCostFlowBySpfa;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongMinimumCostFlow;

import java.util.List;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = 0;
        int t = n - 1;


        List<LongCostFlowEdge>[] g = LongFlow.createCostFlow(n);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            int cap = in.readInt();
            int cost = in.readInt();
            LongFlow.addCostEdge(g, u, v, cap, cost);
        }

        CapacityScalingMinimumCostFlowBySpfa mcf = new CapacityScalingMinimumCostFlowBySpfa();
        long[] ans = mcf.apply(g, s, t, true);
        out.append(ans[0]).append(' ').append(ans[1]);
    }
}
