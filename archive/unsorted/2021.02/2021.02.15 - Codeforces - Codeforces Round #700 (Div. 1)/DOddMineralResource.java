package contest;

import graphs.lca.Lca;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;
import template.rand.RandomWrapper;
import template.rand.Randomized;

import java.util.List;

public class DOddMineralResource {
    int[] a;
    long[] hash;
    List<UndirectedEdge>[] g;
    int lo;
    int hi;
    NoTagPersistentSegment[] st;
    int[] fa;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        lo = 1;
        hi = n;
        a = new int[n];
        hash = new long[n + 1];
        fa = new int[n];
        st = new NoTagPersistentSegment[n];
        for (int i = 0; i <= n; i++) {
            hash[i] = RandomWrapper.INSTANCE.nextLong();
        }
        g = Graph.createGraph(n);
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
        }
        for (int i = 0; i < n - 1; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        dfs(0, -1);
        LcaOnTree lca = new LcaOnTree(g, 0);
        for (int i = 0; i < q; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            int l = lca.lca(u, v);
            int L = in.ri();
            int R = in.ri();
            int ans = search(L, R, lo, hi, st[u], st[v], st[l], fa[l] == -1 ? NoTagPersistentSegment.NIL : st[fa[l]]);
            out.println(ans);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    int search(int ll, int rr, int l, int r, NoTagPersistentSegment a, NoTagPersistentSegment b, NoTagPersistentSegment c, NoTagPersistentSegment d) {
        if (noIntersection(ll, rr, l, r) || (a.xor ^ b.xor ^ c.xor ^ d.xor) == 0) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        int ans = search(ll, rr, l, m, a.left, b.left, c.left, d.left);
        if (ans == -1) {
            ans = search(ll, rr, m + 1, r, a.right, b.right, c.right, d.right);
        }
        return ans;
    }

    void dfs(int root, int p) {
        fa[root] = p;
        st[root] = (p == -1 ? NoTagPersistentSegment.NIL : st[p]).clone();
        st[root].update(a[root], a[root], lo, hi, hash[a[root]]);
        for (DirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
    }


}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    NoTagPersistentSegment left;
    NoTagPersistentSegment right;
    long xor;

    public void pushUp() {
        xor = left.xor ^ right.xor;
    }

    public NoTagPersistentSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (covered(ll, rr, l, r)) {
            xor ^= x;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (!noIntersection(ll, rr, l, m)) {
            left = left.clone();
            left.update(ll, rr, l, m, x);
        }
        if (!noIntersection(ll, rr, m + 1, r)) {
            right = right.clone();
            right.update(ll, rr, m + 1, r, x);
        }
        pushUp();
    }

    public void query(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        left.query(ll, rr, l, m);
        right.query(ll, rr, m + 1, r);
    }

    @Override
    public NoTagPersistentSegment clone() {
        try {
            return (NoTagPersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
