package template.rand;

import template.graph.DirectedEdge;

import java.util.List;

public class HashOnTree2 {
    long[] subtree;
    int[] size;
    MultiSetHasher hasher;
    private List<? extends DirectedEdge>[] g;
    private int n;
    private long treeHash;

    public HashOnTree2(int n, MultiSetHasher hasher) {
        subtree = new long[n];
        size = new int[n];
        this.hasher = hasher;
    }

    public void init(List<? extends DirectedEdge>[] g) {
        n = g.length;
        this.g = g;
        dfs0(0, -1);
        dfs1(0, -1, 0);
        treeHash = 0;
        for (int i = 0; i < n; i++) {
            treeHash = hasher.merge(treeHash, hasher.hash(subtree[i]));
        }
    }

    public long hashByRoot(int root) {
        return hasher.hash(subtree[root]);
    }

    public long hashWithoutRoot() {
        return treeHash;
    }

    private void dfs0(int root, int p) {
        size[root] = 1;
        subtree[root] = 0;
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs0(e.to, root);
            size[root] += size[e.to];
            subtree[root] = hasher.merge(subtree[root], hasher.hash(subtree[e.to]));
        }
        subtree[root] = hasher.merge(subtree[root], size[root]);
    }

    private void dfs1(int root, int p, long top) {
        subtree[root] = hasher.merge(subtree[root], top);
        subtree[root] = hasher.remove(subtree[root], size[root]);
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            long contrib = subtree[root];
            contrib = hasher.remove(contrib, hasher.hash(subtree[e.to]));
            contrib = hasher.merge(contrib, n - size[e.to]);
            dfs1(e.to, root, hasher.hash(contrib));
        }
        subtree[root] = hasher.merge(subtree[root], n);
    }
}
