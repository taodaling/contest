package on2020_11.on2020_11_05_CSES___CSES_Problem_Set.Distinct_Routes;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.LongDinic;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;

import java.util.List;

public class DistinctRoutes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        n = in.readInt();
        int m = in.readInt();
        g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            LongFlow.addFlowEdge(g, in.readInt() - 1, in.readInt() - 1, 1);
        }
        LongDinic dinic = new LongDinic();
        long flow = dinic.apply(g, 0, n - 1, (int) 1e9);
        out.println(flow);
        for (int i = 0; i < flow; i++) {
            seq.clear();
            dfs(0);
            out.println(seq.size());
            for (int j = 0; j < seq.size(); j++) {
                out.append(seq.get(j) + 1).append(' ');
            }
            out.println();
        }
        
    }

    FastOutput out;
    List<LongFlowEdge>[] g;
    IntegerArrayList seq = new IntegerArrayList(1000);
    int n;

    public void dfs(int root) {
        seq.add(root);
        if (root == n - 1) {
            return;
        }
        for (LongFlowEdge e : g[root]) {
            if (e.real && e.flow > 0) {
                e.flow = 0;
                dfs(e.to);
                return;
            }
        }
    }
}
