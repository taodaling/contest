package template.graph;

import java.util.List;

public class DepthOnTreeImpl implements DepthOnTree {
    List<? extends DirectedEdge>[] g;
    int[] depth;

    void dfs(int root, int p) {
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
    }

    public DepthOnTreeImpl(List<? extends DirectedEdge>[] g, int root) {
        this.g = g;
        depth = new int[g.length];
        dfs(root, -1);
    }

    @Override
    public int depth(int node) {
        return depth[node];
    }
}
