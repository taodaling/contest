package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;

import java.util.List;

public class P1345USACO54Telecowmunication {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int src = in.ri() - 1;
        int dst = in.ri() - 1;
        List<IntegerFlowEdge>[] g = Graph.createGraph(n * 2);
        int inf = (int) 1e8;
        for (int i = 0; i < n; i++) {
            int cap = i == src || i == dst ? inf : 1;
            IntegerFlow.addFlowEdge(g, i * 2, i * 2 + 1, cap);
        }
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            IntegerFlow.addFlowEdge(g, a * 2 + 1, b * 2, inf);
            IntegerFlow.addFlowEdge(g, b * 2 + 1, a * 2, inf);
        }
        IntegerMaximumFlow mf = new IntegerISAP();
        int flow = mf.apply(g, src * 2, dst * 2 + 1, inf);
        out.println(flow);
    }
}
