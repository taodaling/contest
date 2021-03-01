package contest;

import template.datastructure.BitSet;
import template.graph.KthAncestorOnTreeByBinaryLift;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerEntryIterator;
import template.primitve.generated.datastructure.IntegerHashMap;

import java.util.ArrayList;
import java.util.List;

public class DOddMineralResource {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int q = in.ri();
        depth = new int[n];
        fa = new int[n];
        IntegerHashMap map = new IntegerHashMap(n, false);
        IntegerArrayList list = new IntegerArrayList(n);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].a = in.ri();
            map.modify(nodes[i].a, 1);
        }
        for (Node node : nodes) {
            if (map.get(node.a) == 1) {
                node.unique = true;
            }
        }

        for (IntegerEntryIterator iterator = map.iterator(); iterator.hasNext(); ) {
            iterator.next();
            int k = iterator.getEntryKey();
            int v = iterator.getEntryValue();
            if (v > 1) {
                list.add(k);
            }
        }
        list.unique();
        m = list.size();
        for (Node node : nodes) {
            if (node.unique) {
                continue;
            }
            node.a = list.binarySearch(node.a);
            assert node.a != -1;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            a.adj.add(b);
            b.adj.add(a);
        }
        dfs(nodes[0], null);
        KthAncestorOnTreeByBinaryLift lca = new KthAncestorOnTreeByBinaryLift(n);
        lca.init(i -> fa[i], n);
        Query query = new Query();
        BitSet tmp = new BitSet(m);
        for (int i = 0; i < q; i++) {
            Node u = nodes[in.ri() - 1];
            Node v = nodes[in.ri() - 1];
            Node c = nodes[lca.lca(u.id, depth[u.id], v.id, depth[v.id])];
            int l = in.ri();
            int r = in.ri();
            int ll = list.lowerBound(l);
            int rr = list.upperBound(r) - 1;
            query.best = -1;
            query.v = -1;
            u.st.query(l, r, 1, n, query);
            if (query.v != -1 && depth[c.id] <= query.best) {
                out.println(query.v);
                continue;
            }

            query.best = -1;
            query.v = -1;
            v.st.query(l, r, 1, n, query);
            if (query.v != -1 && depth[c.id] <= query.best) {
                out.println(query.v);
                continue;
            }

            if (ll <= rr) {
                tmp.copy(u.bs);
                tmp.xor(v.bs);
                if (!c.unique) {
                    tmp.flip(c.a);
                }
                int nextSetBit = tmp.nextSetBit(ll);
                if (nextSetBit <= rr) {
                    out.println(list.get(nextSetBit));
                    continue;
                }
            }
            out.println(-1);
        }
    }

    int[] depth;
    int[] fa;
    int n;
    int m;

    void dfs(Node root, Node p) {
        depth[root.id] = p == null ? 0 : depth[p.id] + 1;
        fa[root.id] = p == null ? -1 : p.id;
        root.st = (p == null ? NoTagPersistentSegment.NIL : p.st).clone();
        if (!root.unique) {
            root.bs = new BitSet(m);
            root.bs.flip(root.a);
            if (p != null) {
                root.bs.xor(p.bs);
            }
        } else {
            if (p != null) {
                root.bs = p.bs;
            } else {
                root.bs = new BitSet(m);
            }
            root.st.update(root.a, root.a, 1, n, depth[root.id]);
        }
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
        }
    }
}

class Node {
    List<Node> adj = new ArrayList<>();
    BitSet bs;
    int a;
    boolean unique;
    int id;
    NoTagPersistentSegment st;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}

class Query {
    int best;
    int v;
}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
        NIL.deepest = -1;
    }

    private NoTagPersistentSegment left;
    private NoTagPersistentSegment right;
    int deepest = -1;

    public void pushUp() {
        deepest = Math.max(left.deepest, right.deepest);
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
            deepest = Math.max(deepest, x);
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

    public void query(int ll, int rr, int l, int r, Query q) {
        if (this == NIL || noIntersection(ll, rr, l, r) || deepest <= q.best) {
            return;
        }
        if (l == r) {
            q.best = deepest;
            q.v = l;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (left.deepest >= right.deepest) {
            left.query(ll, rr, l, m, q);
            right.query(ll, rr, m + 1, r, q);
        } else {
            right.query(ll, rr, m + 1, r, q);
            left.query(ll, rr, l, m, q);
        }
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
