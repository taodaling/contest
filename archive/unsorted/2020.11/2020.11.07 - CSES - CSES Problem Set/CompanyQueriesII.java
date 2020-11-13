package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class CompanyQueriesII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 1; i < n; i++) {
            Graph.addEdge(g, in.readInt() - 1, i);
        }
        LcaOnTree lca = new LcaOnTree(g, 0);
        for (int i = 0; i < q; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            out.println(1 + lca.lca(u, v));
        }
    }

}
