package template.graph;

import java.util.List;
import java.util.function.IntPredicate;

public class ParentOnTreeByDfs implements ParentOnTree {
    List<? extends DirectedEdge>[] g;
    int[] p;

    public ParentOnTreeByDfs(List<? extends DirectedEdge>[] g, IntPredicate isRoot) {
        p = new int[g.length];
        for (int i = 0; i < g.length; i++) {
            if (isRoot.test(i)) {
                dfs(g, i, -1);
            }
        }
    }

    private void dfs(List<? extends DirectedEdge>[] g, int root, int p) {
        this.p[root] = p;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(g, e.to, root);
        }
    }

    @Override
    public int parent(int node) {
        return p[node];
    }
}
