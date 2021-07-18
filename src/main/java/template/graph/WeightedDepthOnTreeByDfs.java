package template.graph;

import template.primitve.generated.graph.LongWeightDirectedEdge;

import java.util.List;
import java.util.function.IntPredicate;

public class WeightedDepthOnTreeByDfs implements WeightedDepthOnTree {
    private List<? extends LongWeightDirectedEdge>[] g;
    private long[] depth;

    public WeightedDepthOnTreeByDfs(int n) {
        depth = new long[n];
    }

    public void init(List<? extends LongWeightDirectedEdge>[] g, IntPredicate isRoot) {
        for (int i = 0; i < g.length; i++) {
            if (isRoot.test(i)) {
                dfs(i, -1, 0);
            }
        }
    }

    private void dfs(int root, int p, long d) {
        depth[root] = d;
        for (LongWeightDirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, d + e.weight);
        }
    }

    @Override
    public long depth(int u) {
        return depth[u];
    }
}
