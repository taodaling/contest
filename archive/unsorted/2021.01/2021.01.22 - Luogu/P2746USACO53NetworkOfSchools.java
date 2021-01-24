package contest;

import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class P2746USACO53NetworkOfSchools {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        List<DirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n; i++) {
            int to;
            while ((to = in.ri()) != 0) {
                to--;
                Graph.addEdge(g, i, to);
            }
        }
        int cc = 0;
        DirectedTarjanSCC scc = new DirectedTarjanSCC(n);
        scc.init(g);
        int[] indeg = new int[n];
        int[] outdeg = new int[n];
        for (int i = 0; i < n; i++) {
            if (scc.set[i] == i) {
                cc++;
            }
            int set = scc.set[i];
            for (DirectedEdge e : g[i]) {
                int to = scc.set[e.to];
                if (to == set) {
                    continue;
                }
                indeg[to]++;
                outdeg[set]++;
            }
        }
        int root = 0;
        int leaf = 0;
        for (int i = 0; i < n; i++) {
            if (scc.set[i] == i) {
                if (indeg[i] == 0) {
                    root++;
                }
                if (outdeg[i] == 0) {
                    leaf++;
                }
            }
        }
        out.println(root);
        out.println(cc == 1 ? 0 : Math.max(root, leaf));
    }
}
