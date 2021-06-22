package template.graph;

public class DistanceOnTreeImpl implements DistanceOnTree {
    LcaOnTree lca;
    DepthOnTree dot;

    public DistanceOnTreeImpl(LcaOnTree lca, DepthOnTree dot) {
        this.lca = lca;
        this.dot = dot;
    }

    @Override
    public int distance(int u, int v) {
        return dot.depth(u) + dot.depth(v) - 2 * dot.depth(lca.lca(u, v));
    }
}
