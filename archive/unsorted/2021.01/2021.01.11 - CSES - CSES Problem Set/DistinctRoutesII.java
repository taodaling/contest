package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;

import java.util.List;

public class DistinctRoutesII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        List<LongCostFlowEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            LongFlow.addCostFlowEdge(g, a, b, 1, 1);
        }
        LongDijkstraMinimumCostFlow mcf = new LongDijkstraMinimumCostFlow();
        long[] res = mcf.apply(g, 0, n - 1, k);
        if (res[0] < k) {
            out.println(-1);
            return;
        }
        out.println(res[1]);
        IntegerArrayList seq = new IntegerArrayList(n);
        for (int time = 0; time < k; time++) {
            seq.clear();
            int root = 0;
            while (root != n - 1) {
                seq.add(root);
                for (LongCostFlowEdge e : g[root]) {
                    if (e.real && e.flow == 1) {
                        root = e.to;
                        e.flow = 0;
                        break;
                    }
                }
            }
            seq.add(root);
            out.println(seq.size());
            for (int x : seq.toArray()) {
                out.append(x + 1).append(' ');
            }
            out.println();
        }
    }
}
