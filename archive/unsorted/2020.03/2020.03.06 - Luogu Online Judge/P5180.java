package contest;



import template.graph.DirectedEdge;
import template.graph.DominatorTree;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.List;

public class P5180 {
    int[] size;
    List<DirectedEdge>[] tree;
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        size = new int[n];
        List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
        tree = Graph.createDirectedGraph(n);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addEdge(g, u, v);
        }
        DominatorTree dt = new DominatorTree(g, 0);
        for (int i = 1; i < n; i++) {
            int p = dt.parent(i);
            if(p == -1){
                continue;
            }
            Graph.addEdge(tree, p, i);
        }
        dfs(0);
        debug.debug("dt", dt);
        for (int i = 0; i < n; i++) {
            out.append(size[i]).append(' ');
        }
    }

    public void dfs(int root) {
        size[root] = 1;
        for (DirectedEdge e : tree[root]) {
            dfs(e.to);
            size[root] += size[e.to];
        }
    }
}
