package contest;

import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;

import java.util.List;

public class P6329 {
    List<UndirectedEdge>[] g;
    int[] fa;
    int[] data;
    IntegerBIT[] bit;
    IntegerBIT[] toFather;
    int[] size;
    LcaOnTree lca;
    int[] depth;

    public void dfsForDepth(int root, int p) {
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForDepth(e.to, root);
        }
    }

    public int dist(int a, int b) {
        int l = lca.lca(a, b);
        return depth[a] + depth[b] - 2 * depth[l];
    }

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

    int dfsForCentroid(int root, int p, int total) {
        int childSize = total - size[root];
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            int ans = dfsForCentroid(e.to, root, total);
            if (ans >= 0) {
                return ans;
            }
            childSize = Math.max(childSize, size[e.to]);
        }
        if (childSize * 2 <= total) {
            return root;
        }
        return -1;
    }

    public void contrib(int root, int p, IntegerBIT a, IntegerBIT b, int d) {
        a.update(d, data[root]);
        b.update(d, data[root]);
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            contrib(e.to, root, a, b, d + 1);
        }
    }

    public void dac(int root, int p, IntegerBIT toFatherBIT) {
        int total = size[root];
        root = dfsForCentroid(root, -1, total);
        dfsForSize(root, -1);
        fa[root] = p;
        bit[root] = new IntegerBIT(total);
        toFather[root] = toFatherBIT;
        bit[root].update(1, data[root]);
        for (UndirectedEdge e : g[root]) {
            toFather[e.to] = new IntegerBIT(size[e.to] + 1);
            contrib(e.to, root, bit[root], toFather[e.to], 2);
        }
        for (UndirectedEdge e : g[root]) {
            g[e.to].remove(e.rev);
            dac(e.to, root, toFather[e.to]);
        }
    }

    public int nearSum(int root, int k) {
        int ans = bit[root].query(k + 1);
        int last = root;
        for (int i = fa[root]; i != -1; last = i, i = fa[i]) {
            int d = dist(root, i);
            int delta = k - d;
            if (delta >= 0) {
                ans += bit[i].query(delta + 1) - toFather[last].query(delta + 1);
            }
        }
        return ans;
    }

    public void modify(int root, int x) {
        int mod = x - data[root];
        data[root] = x;
        bit[root].update(1, mod);
        int last = root;
        for (int i = fa[root]; i != -1; last = i, i = fa[i]) {
            int d = dist(root, i);
            toFather[last].update(d + 1, mod);
            bit[i].update(d + 1, mod);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        data = in.ri(n);
        fa = new int[n];
        bit = new IntegerBIT[n];
        toFather = new IntegerBIT[n];
        size = new int[n];
        depth = new int[n];
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }
        lca = new LcaOnTree(n);
        lca.init(g, i -> i == 0);
        dfsForDepth(0, -1);
        dfsForSize(0, -1);
        dac(0, -1, null);

        int last = 0;
        boolean xor = true;
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int x = in.ri();
            int y = in.ri() ;
            if(xor){
                x ^= last;
                y ^= last;
            }
            if (t == 0) {
                last = nearSum(x - 1, y);
                out.println(last);
            } else {
                modify(x - 1, y);
            }
        }
    }
}


