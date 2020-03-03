package on2020_03.on2020_03_03_Codeforces_Round__495__Div__2_.E__Sonya_and_Ice_Cream;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ESonyaAndIceCream {

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 1; i < n; i++) {
            Node a = nodes[in.readInt() - 1];
            Node b = nodes[in.readInt() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.len = in.readInt();
            a.next.add(e);
            b.next.add(e);
        }

        dfs(nodes[0], null, 0);
        int end1 = 0;
        for (int i = 1; i < n; i++) {
            if (nodes[i].dist > nodes[end1].dist) {
                end1 = i;
            }
        }
        dfs(nodes[end1], null, 0);
        int end2 = 0;
        for (int i = 1; i < n; i++) {
            if (nodes[i].dist > nodes[end2].dist) {
                end2 = i;
            }
        }

        Deque<Node> dq = new ArrayDeque<>(n);
        collect(nodes[end1], null, nodes[end2], dq);
        Node[] seq = dq.toArray(new Node[0]);
        for (Node node : seq) {
            node.diameter = true;
        }

        int[] dist = new int[seq.length];
        for (int i = 0; i < seq.length - 1; i++) {
            for (Edge e : seq[i].next) {
                if (e.other(seq[i]) == seq[i + 1]) {
                    dist[i] = e.len;
                    break;
                }
            }
        }

        for (Node node : seq) {
            dfs2(node, null, 0);
        }

        debug.debug("seq", seq);
        debug.debug("dist", dist);
        Segment segment = new Segment(0, seq.length);
        for (int i = 0; i < seq.length && i < k; i++) {
            segment.update(i, i, 0, seq.length, seq[i].dist);
        }
        int total = 0;
        for (int i = k; i < seq.length; i++) {
            total += dist[i - 1];
            segment.update(i, i, 0, seq.length, seq[i].dist + total);
        }

        int ans = segment.query(0, seq.length, 0, seq.length);
        for (int i = 1; i + k - 1 < seq.length; i++) {
            segment.update(0, i - 1, 0, seq.length, dist[i - 1]);
            segment.update(i + k - 1, seq.length, 0, seq.length, -dist[i + k - 2]);
            ans = Math.min(ans, segment.query(0, seq.length, 0, seq.length));
        }
        out.println(ans);
    }

    public static boolean collect(Node root, Node p, Node target, Deque<Node> trace) {
        trace.addLast(root);
        if (root == target) {
            return true;
        }
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            if (collect(node, root, target, trace)) {
                return true;
            }
        }
        trace.removeLast();
        return false;
    }

    public static void dfs2(Node root, Node p, int len) {
        root.dist = len;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p || node.diameter) {
                continue;
            }
            dfs2(node, root, len + e.len);
            root.dist = Math.max(root.dist, node.dist);
        }
    }

    public static void dfs(Node root, Node p, int len) {
        root.dist = len;
        for (Edge e : root.next) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, len + e.len);
        }
    }
}

class Edge {
    Node a;
    Node b;
    int len;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    int dist;
    List<Edge> next = new ArrayList<>();
    boolean diameter;
    int id;

    @Override
    public String toString() {
        return "" + id;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private int max;
    private int dirty;

    public void modify(int x) {
        max += x;
        dirty += x;
    }

    public void pushUp() {
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

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
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public int query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return max;
        }
        pushDown();
        int m = (l + r) >> 1;
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
