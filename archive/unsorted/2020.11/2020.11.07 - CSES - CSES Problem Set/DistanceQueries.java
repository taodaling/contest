package contest;

import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class DistanceQueries {
    List<UndirectedEdge>[] g ;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1);
        }
        depth = new int[n];
        dfs(0, 0);
        LcaOnTree lca = new LcaOnTree(g, 0);
        for (int i = 0; i < q; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            int l = lca.lca(u, v);
            int dist = depth[u] + depth[v] - 2 * depth[l];
            out.println(dist);
        }
    }

    int[] depth;
    public void dfs(int root, int p){
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for(UndirectedEdge e : g[root]){
            if(e.to == p){
                continue;
            }
            dfs(e.to, root);
        }
    }
}
