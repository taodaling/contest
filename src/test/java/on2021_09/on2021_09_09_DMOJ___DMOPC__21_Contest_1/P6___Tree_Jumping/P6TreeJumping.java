package on2021_09.on2021_09_09_DMOJ___DMOPC__21_Contest_1.P6___Tree_Jumping;



import template.datastructure.ConvexHullTrickExt;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class P6TreeJumping {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n; i++) {
            nodes[i].r = in.ri();
        }
        for (int i = 0; i < n; i++) {
            nodes[i].c = in.ri();
        }
        for (int i = 0; i < n - 1; i++) {
            Edge e = new Edge();
            e.a = nodes[in.ri() - 1];
            e.b = nodes[in.ri() - 1];
            e.w = in.ri();
            e.a.adj.add(e);
            e.b.adj.add(e);
        }

        dfs(nodes[0], null, 0);
        Segment st = new Segment(0, n - 1);
        Node[] nodeSortByR = nodes.clone();
        Node[] nodeSortByC = nodes.clone();
        Arrays.sort(nodeSortByR, Comparator.comparingInt(x -> x.r));
        Arrays.sort(nodeSortByC, Comparator.comparingInt(x -> x.c));
        int nodeSortByRIter = n - 1;
        for (int i = n - 1; i >= 0; i--) {
            while (nodeSortByRIter >= 0 && nodeSortByR[nodeSortByRIter].r > nodeSortByC[i].c) {
                Node head = nodeSortByR[nodeSortByRIter--];
                st.update(head.begin, head.end, 0, n - 1, head.r, -head.depth);
                debug.debug("r", head);
            }
            Node head = nodeSortByC[i];

            debug.debug("c", head);
            head.ans = st.query(head.begin, head.begin, 0, n - 1, -head.c, head.depth);
        }
        for (int i = 1; i < n; i++) {
            if (nodes[i].ans < 0) {
                out.println("Unreachable");
                continue;
            }
            out.println(1 / nodes[i].ans);
        }
    }

    Debug debug = new Debug(false);
    int time = 0;

    public void dfs(Node root, Node p, long d) {
        root.depth = d;
        root.begin = time++;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, root.depth + e.w);
        }
        root.end = time - 1;
    }
}

class Edge {
    Node a;
    Node b;
    int w;

    Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    long depth;
    int r;
    int c;
    int begin;
    int end;
    double ans;
    int id;

    @Override
    public String toString() {
        return "" + (id + 1);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    ConvexHullTrickExt cht = new ConvexHullTrickExt();

    private void modify(double b, double c) {
        cht.add(b, c);
    }

    public void pushUp() {
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {

        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r, double b, double c) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify(b, c);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m, b, c);
        right.update(L, R, m + 1, r, b, c);
        pushUp();
    }

    public double query(int L, int R, int l, int r, double x, double z) {
        if (leave(L, R, l, r)) {
            return -1;
        }
        double cand = cht.empty() ? -1 : cht.query(x, z);
        if (l == r) {
            return cand;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        double ans1 = left.query(L, R, l, m, x, z);
        double ans2 = right.query(L, R, m + 1, r, x, z);
        return Math.max(cand, Math.max(ans1, ans2));
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


