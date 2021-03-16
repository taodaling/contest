package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DRoadsAndRamen {
    int[][] edges;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }

        List<Query> allQuery = new ArrayList<>(1000000);
        edges = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            edges[i][0] = u;
            edges[i][1] = v;
            Node a = nodes[u];
            Node b = nodes[v];
            a.adj.add(b);
            b.adj.add(a);
            if (in.readInt() == 1) {
                allQuery.add(new Query(i));
            }
        }

        int m = in.readInt();
        Query[] qs = new Query[m];
        for (int i = 0; i < m; i++) {
            qs[i] = new Query(in.readInt() - 1);
            allQuery.add(qs[i]);
        }

        dfsForDepth(nodes[0], null);
        Node a = CompareUtils.maxOf(nodes, 0, n - 1, Node.sortByDepth);
        dfsForDepth(a, null);
        Node b = CompareUtils.maxOf(nodes, 0, n - 1, Node.sortByDepth);
        solve(a, nodes, allQuery);
        solve(b, nodes, allQuery);
        for (Query q : qs) {
            out.println(q.ans);
        }
    }

    public void solve(Node root, Node[] nodes, List<Query> qs) {
        dfsForLR(root, null);
        alloc = 1;
        int n = nodes.length;
        Node[] invIds = new Node[n + 1];
        for (Node node : nodes) {
            invIds[node.l] = node;
        }
        Segment seg = new Segment(1, n, i -> invIds[i].depth);
        for (Query q : qs) {
            Node u = nodes[edges[q.v][0]];
            Node v = nodes[edges[q.v][1]];
            if (u.depth < v.depth) {
                Node tmp = u;
                u = v;
                v = tmp;
            }
            seg.update(u.l, u.r, 1, n);
            q.ans = Math.max(q.ans, seg.queryL(1, n, 1, n));
        }
    }


    int alloc = 1;

    public void dfsForLR(Node root, Node p) {
        root.l = alloc++;
        root.depth = p == null ? 0 : p.depth + 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForLR(node, root);
        }
        root.r = alloc - 1;
    }

    public void dfsForDepth(Node root, Node p) {
        root.depth = p == null ? 0 : p.depth + 1;
        for (Node node : root.adj) {
            if (node == p) {
                continue;
            }
            dfsForDepth(node, root);
        }

    }
}

class Query {
    int v;
    int ans;

    public Query(int v) {
        this.v = v;
    }
}

class Node {
    int id;
    List<Node> adj = new ArrayList<>();
    int depth;

    @Override
    public String toString() {
        return "" + (id + 1);
    }

    int l;
    int r;

    static Comparator<Node> sortByDepth = (a, b) -> Integer.compare(a.depth, b.depth);
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int[] oe = new int[2];
    private boolean rev;

    private void modify() {
        rev = !rev;
        SequenceUtils.swap(oe, 0, 1);
    }

    public void pushUp() {
        for (int i = 0; i < 2; i++) {
            oe[i] = Math.max(left.oe[i], right.oe[i]);
        }
    }

    public void pushDown() {
        if (rev) {
            left.modify();
            right.modify();
            rev = false;
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            oe[0] = func.apply(l);
            oe[1] = 0;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify();
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m);
        right.update(ll, rr, m + 1, r);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return oe[0];
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryL(ll, rr, l, m),
                right.queryL(ll, rr, m + 1, r));
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append("val").append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}