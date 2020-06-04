package on2020_05.on2020_05_23_Codeforces___2017_Chinese_Multi_University_Training__BeihangU_Contest.G__Gear_Up;



import template.binary.Log2;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntFunction;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class GGearUp {
    double ln2 = Math.log(2);
    // Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            //debug.debug("2^30", 1 << 31);
            throw new UnknownError();
        }

        order = 0;
        out.printf("Case #%d:", testNumber).println();
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].r = Log2.floorLog(in.readInt());
        }
        for (int i = 0; i < m; i++) {
            Edge e = new Edge();
            e.linear = in.readInt();
            e.a = nodes[in.readInt() - 1];
            e.b = nodes[in.readInt() - 1];
            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        for (Node node : nodes) {
            if (node.visited) {
                continue;
            }
            dfs(node, null, node, true, 0);
        }

        Segment seg = new Segment(1, order, i -> seq[i].ps);

        for (int i = 0; i < q; i++) {
            int a = in.readInt();
            Node x = nodes[in.readInt() - 1];
            int y = Log2.floorLog(in.readInt());
            int cur = seg.query(x.levelL, x.levelL, 1, order);
            if (a == 1) {
                //replace
                if (x.special) {
                    int delta = (-y) - (-x.r);
                    seg.update(x.levelL, x.levelR, 1, order, delta);
                    seg.update(x.subL + 1, x.subR, 1, order, -delta);
                } else {
                    int delta = y - x.r;
                    seg.update(x.subL + 1, x.subR, 1, order, delta);
                }
                x.r = y;
            } else {
                //query
                Node root = x.top;
                int rootVelocity = y - cur;
                int max = seg.query(root.levelL, root.levelR, 1, order);
                max += rootVelocity;
                out.printf("%.3f", max * ln2).println();
            }
        }
    }

    int order = 0;
    Node[] seq = new Node[100000 + 1];

    public void dfs(Node root, Node p, Node top, boolean type, int ps) {
        root.visited = true;
        root.special = type;
        root.top = top;
        root.adj.sort((a, b) -> a.linear - b.linear);
        root.ps = ps;
        root.levelR = root.subR = root.subL = root.levelL = ++order;
        seq[root.subL] = root;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            if (e.linear == 1) {
                dfs(node, root, top, true, ps + root.r - node.r);
                root.subR = root.levelR = order;
            } else {
                dfs(node, root, top, false, ps);
                root.levelR = order;
            }
        }
    }
}

class Edge {
    Node a;
    Node b;
    int linear;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int r;
    Node top;
    boolean visited = false;
    int subL;
    int subR;
    int levelL;
    int levelR;
    boolean special;
    int ps;

    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    int max;
    int plusTag;

    private void modify(int x) {
        plusTag += x;
        max += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
        if (plusTag != 0) {
            left.modify(plusTag);
            right.modify(plusTag);
            plusTag = 0;
        }
    }

    public Segment(int l, int r, IntToIntFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            pushUp();
        } else {
            max = func.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return (int) -1e8;
        }
        if (covered(ll, rr, l, r)) {
            return max;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
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
            builder.append(max).append(",");
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
