package template.graph;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DfsTree {
    public int[] parents;
    public int[] depth;
    public List<? extends DirectedEdge>[] g;
    public EdgeConsumer consumer;

    public static interface EdgeConsumer {
        void accept(int root, DirectedEdge e);
    }

    public DfsTree(int n) {
        parents = new int[n];
        depth = new int[n];
    }


    public void init(List<? extends DirectedEdge>[] g) {
        this.g = g;
        Arrays.fill(parents, 0, g.length, -2);
        for (int i = 0; i < g.length; i++) {
            if (parents[i] == -2) {
                dfs(i, -1);
            }
        }
    }

    private void dfs(int root, int p) {
        parents[root] = p;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (DirectedEdge e : g[root]) {
            if (parents[e.to] != -2) {
                continue;
            }
            if (consumer != null) {
                consumer.accept(root, e);
            }
            dfs(e.to, root);
        }
    }
}
