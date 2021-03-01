package contest;

import graphs.lca.Lca;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.List;

public class CountOnATree {
    int[] a;
    int[] p;
    List<UndirectedEdge>[] g;
    NoTagPersistentSegment[] st;
    int lo = 0;
    int hi;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        a = in.ri(n);
        g = Graph.createGraph(n);
        st = new NoTagPersistentSegment[n];
        p = new int[n];
        IntegerArrayList list = new IntegerArrayList(n);
        list.addAll(a);
        list.unique();
        for (int i = 0; i < n; i++) {
            a[i] = list.binarySearch(a[i]);
        }
        hi = list.size() - 1;
        for (int i = 0; i < n - 1; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        LcaOnTree lca = new LcaOnTree(g, 0);
        dfs(0, -1);
        for (int i = 0; i < m; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            int k = in.ri();
            int l = lca.lca(u, v);
            int ans = kthElement(lo, hi, k, st[u], st[v], st[l],
                    p[l] == -1 ? NoTagPersistentSegment.NIL : st[p[l]]);
            out.println(list.get(ans));
        }
    }

    int kthElement(int l, int r, int k, NoTagPersistentSegment a, NoTagPersistentSegment b, NoTagPersistentSegment c, NoTagPersistentSegment d) {
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        int lsize = a.left.count + b.left.count - c.left.count - d.left.count;
        if (lsize >= k) {
            return kthElement(l, m, k, a.left, b.left, c.left, d.left);
        } else {
            return kthElement(m + 1, r, k - lsize, a.right, b.right, c.right, d.right);
        }
    }

    void dfs(int root, int p) {
        st[root] = (p == -1 ? NoTagPersistentSegment.NIL : st[p]).clone();
        st[root].update(a[root], a[root], lo, hi, 1);
        this.p[root] = p;
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
    int count;

    public void pushUp() {
        count = left.count + right.count;
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

    public void update(int ll, int rr, int l, int r, int x) {
        if (covered(ll, rr, l, r)) {
            count += x;
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

