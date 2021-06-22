package template.graph;

import template.primitve.generated.datastructure.LongBIT;

import java.util.List;

/**
 * 点分树
 */
public class VertexDacTree {
    List<UndirectedEdge>[] g;
    LongBIT[] rootBIT;
    LongBIT[] childBIT;
    int[] p;
    int[] size;
    long[] weight;
    DistanceOnTree dot;

    void dfsForSize(int root, int p) {
        size[root] = 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForSize(e.to, root);
            size[root] += size[e.to];
        }
    }

    int findCentroid(int root, int p, int total) {
        int maxChildSize = 0;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                maxChildSize = Math.max(maxChildSize, total - size[root]);
                continue;
            }
            int ans = findCentroid(e.to, root, total);
            if (ans != -1) {
                return ans;
            }
            maxChildSize = Math.max(maxChildSize, size[e.to]);
        }
        if (maxChildSize * 2 <= total) {
            return root;
        }
        return -1;
    }

    private long query(LongBIT bit, int l, int r) {
        if (r < l) {
            return 0;
        }
        return bit.query(r + 1) - bit.query(l);
    }

    private void update(LongBIT bit, int i, long v) {
        bit.update(i + 1, v);
    }

    private void dfsForUpdate(int root, int p, LongBIT bit, int depth) {
        update(bit, depth, weight[root]);
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForUpdate(e.to, root, bit, depth + 1);
        }
    }

    void dac(int root, int p, LongBIT bit) {
        root = findCentroid(root, -1, size[root]);
        dfsForSize(root, -1);
        this.p[root] = p;
        this.childBIT[root] = bit;
        rootBIT[root] = new LongBIT(size[root]);
        dfsForUpdate(root, -1, rootBIT[root], 0);
        for (UndirectedEdge e : g[root]) {
            LongBIT child = new LongBIT(size[e.to] + 1);
            dfsForUpdate(e.to, root, child, 1);
            int index = g[e.to].indexOf(e.rev);
            g[e.to].remove(index);
            dac(e.to, root, child);
            g[e.to].add(index, e.rev);
        }
    }

    /**
     * <p>
     * modify the weight of root, increase it by v
     * </p>
     * O((\log_2V)^2)
     */
    public void update(int root, long v) {
        update(rootBIT[root], 0, v);
        int last = root;
        for (int u = p[root]; u != -1; last = u, u = p[u]) {
            int d = dot.distance(u, root);
            update(rootBIT[u], d, v);
            update(childBIT[last], d, v);
        }
    }

    /**
     * <p>
     * query the sum of weights of nodes that has distance to root no more than radius
     * </p>
     * O((\log_2V)^2)
     */
    public long query(int root, int radius) {
        long ans = query(rootBIT[root], 0, radius);
        int last = root;
        for (int u = p[root]; u != -1; last = u, u = p[u]) {
            int d = dot.distance(u, root);
            ans += query(rootBIT[u], 0, radius - d) - query(childBIT[last], 0, radius - d);
        }
        return ans;
    }

    /**
     * O(V\log_2 V) memory and time for construction
     */
    public VertexDacTree(List<UndirectedEdge>[] g, long[] weight, DistanceOnTree dot) {
        int n = g.length;
        this.g = g;
        this.weight = weight;
        this.dot = dot;
        rootBIT = new LongBIT[n];
        childBIT = new LongBIT[n];
        p = new int[n];
        size = new int[n];
        dfsForSize(0, -1);
        dac(0, -1, null);
    }
}
