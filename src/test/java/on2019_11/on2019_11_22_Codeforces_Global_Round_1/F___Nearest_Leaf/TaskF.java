package on2019_11.on2019_11_22_Codeforces_Global_Round_1.F___Nearest_Leaf;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int q = in.readInt();
        Node[] nodes = new Node[n + 1];
        for (int i = 1; i <= n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 2; i <= n; i++) {
            Node p = nodes[in.readInt()];
            Edge edge = new Edge();
            edge.a = p;
            edge.b = nodes[i];
            edge.w = in.readInt();
            edge.a.next.add(edge);
            edge.b.next.add(edge);
        }

        List<Query> qs = new ArrayList<>(q);
        for (int i = 0; i < q; i++) {
            Node node = nodes[in.readInt()];
            Query query = new Query();
            query.l = in.readInt();
            query.r = in.readInt();
            node.queries.add(query);
            qs.add(query);
        }
        seg = new Segment(1, n);
        dfs(nodes[1], null, 0);
        dfsForQuery(nodes[1], null);

        for(Query query : qs){
            out.println(query.ans);
        }
    }

    int n;
    Segment seg;

    public void dfs(Node root, Edge p, long dist) {
        root.l = root.r = root.id;
        root.next.remove(p);
        if (root.next.isEmpty()) {
            seg.update(root.id, root.id, 1, n, dist);
            return;
        }
        for (Edge e : root.next) {
            Node node = e.other(root);
            dfs(node, e, dist + e.w);
            root.l = Math.min(root.l, node.l);
            root.r = Math.max(root.r, node.r);
        }
        seg.update(root.id, root.id, 1, n, (long) 1e18);
    }

    public void dfsForQuery(Node root, Edge p) {
        if (p != null) {
            seg.update(1, n, 1, n, p.w);
            seg.update(root.l, root.r, 1, n, p.w * -2);
        }

        for (Edge e : root.next) {
            Node node = e.other(root);
            dfsForQuery(node, e);
        }

        for (Query q : root.queries) {
            q.ans = seg.query(q.l, q.r, 1, n);
        }

        if (p != null) {
            seg.update(root.l, root.r, 1, n, p.w * 2);
            seg.update(1, n, 1, n, -p.w);
        }
    }
}

class Edge {
    Node a;
    Node b;
    int w;

    Node other(Node x) {
        return x == a ? b : a;
    }
}

class Query {
    int l;
    int r;
    long ans;
}

class Node {
    List<Edge> next = new ArrayList<>();
    List<Query> queries = new ArrayList<>();
    int l;
    int r;
    int id;
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long min;
    private long mod;

    public void mod(long m) {
        min += m;
        mod += m;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
        if (mod != 0) {
            left.mod(mod);
            right.mod(mod);
            mod = 0;
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

    public void update(int ll, int rr, int l, int r, long mod) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            mod(mod);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, mod);
        right.update(ll, rr, m + 1, r, mod);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return (long) 1e18;
        }
        if (covered(ll, rr, l, r)) {
            return min;
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.min(left.query(ll, rr, l, m),
                right.query(ll, rr, m + 1, r));
    }
}
