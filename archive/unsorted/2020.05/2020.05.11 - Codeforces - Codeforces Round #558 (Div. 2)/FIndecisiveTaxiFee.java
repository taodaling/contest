package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FIndecisiveTaxiFee {
    long inf = (long) 1e18;
    int curTag = 0;
    Segment seg;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();

        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
            nodes[i].distToSrc = nodes[i].distToDst = inf;
        }

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = nodes[in.readInt() - 1];
            edges[i].b = nodes[in.readInt() - 1];
            edges[i].w = in.readInt();
            edges[i].a.adj.add(edges[i]);
            edges[i].b.adj.add(edges[i]);
        }

        Node src = nodes[0];
        Node dst = nodes[n - 1];
        TreeSet<Node> pq = new TreeSet<>((a, b) -> a.distToSrc == b.distToSrc ? a.id - b.id : Long.compare(a.distToSrc, b.distToSrc));

        src.distToSrc = 0;
        pq.add(src);
        while (!pq.isEmpty()) {
            Node head = pq.pollFirst();
            for (Edge e : head.adj) {
                Node node = e.other(head);
                if (node.distToSrc > head.distToSrc + e.w) {
                    pq.remove(node);
                    node.distToSrc = head.distToSrc + e.w;
                    pq.add(node);
                }
            }
        }

        dst.distToDst = 0;
        pq = new TreeSet<>((a, b) -> a.distToDst == b.distToDst ? a.id - b.id : Long.compare(a.distToDst, b.distToDst));
        pq.add(dst);
        while (!pq.isEmpty()) {
            Node head = pq.pollFirst();
            for (Edge e : head.adj) {
                Node node = e.other(head);
                if (node.distToDst > head.distToDst + e.w) {
                    pq.remove(node);
                    node.distToDst = head.distToDst + e.w;
                    pq.add(node);
                }
            }
        }

        for (Node trace = src; trace != dst; ) {
            Edge next = null;
            for (Edge e : trace.adj) {
                Node node = e.other(trace);
                if (node.distToDst + e.w == trace.distToDst) {
                    next = e;
                    break;
                }
            }
            next.tag = ++curTag;
            trace = next.other(trace);
        }

        seg = new Segment(1, curTag);
        for (Edge e : edges) {
            if(e.tag != -1){
                continue;
            }
            update(e.a, e.b, e.w);
            update(e.b, e.a, e.w);
        }

        for (int i = 0; i < q; i++) {
            Edge e = edges[in.readInt() - 1];
            int w = in.readInt();
            long ans = Math.min(e.a.distToSrc + e.b.distToDst + w,
                    e.a.distToDst + e.b.distToSrc + w);
            if (e.tag == -1) {
                ans = Math.min(ans, dst.distToSrc);
            } else {
                long val = seg.queryL(e.tag, e.tag, 1, curTag);
                ans = Math.min(ans, val);
            }
            out.println(ans);
        }
    }

    public void update(Node a, Node b, int w) {
        long dist = a.distToSrc + b.distToDst + w;
        int l = prev(a);
        int r = post(b);
        seg.updatePlus(l + 1, r - 1, 1, curTag, dist);
    }

    public int post(Node root) {
        if (root.r == Integer.MIN_VALUE) {
            root.r = curTag + 1;
            for (Edge e : root.adj) {
                Node node = e.other(root);
                if (node.distToDst + e.w == root.distToDst) {
                    if (e.tag != -1) {
                        root.r = e.tag;
                    } else {
                        root.r = post(node);
                    }
                    break;
                }
            }
        }
        return root.r;
    }

    public int prev(Node root) {
        if (root.l == Integer.MIN_VALUE) {
            root.l = -1;
            for (Edge e : root.adj) {
                Node node = e.other(root);
                if (node.distToSrc + e.w == root.distToSrc) {
                    if (e.tag != -1) {
                        root.l = e.tag;
                    } else {
                        root.l = prev(node);
                    }
                    break;
                }
            }
        }
        return root.l;
    }
}

class Edge {
    Node a;
    Node b;
    int w;
    int tag = -1;

    public Node other(Node x) {
        return x == a ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    long distToSrc;
    long distToDst;

    int l = Integer.MIN_VALUE;
    int r = Integer.MIN_VALUE;
    int id;


    @Override
    public String toString() {
        return "" + id;
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    static long inf = (long) 1e18;

    long min = inf;

    private void modify(long x) {
        min = Math.min(min, x);
    }

    public void pushUp() {

    }

    public void pushDown() {
        left.modify(min);
        right.modify(min);
        min = inf;
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

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updatePlus(ll, rr, l, m, x);
        right.updatePlus(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryL(ll, rr, l, m),
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
