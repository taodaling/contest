package contest;

import template.datastructure.SegTree;
import template.graph.KthAncestorOnTreeByBinaryLift;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.CloneSupportObject;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

import java.util.ArrayList;
import java.util.List;

public class DBirthday {
    int mod = (int) 1e9 + 7;
    int order = 0;
    int n;
    SegTree<SumImpl, UpdateImpl> st;
    Node[] dfnToNode;
    SumImpl sumBuf = new SumImpl();
    UpdateImpl updBuf = new UpdateImpl();
    Debug debug = new Debug(false);

    void dfs(Node root, Node p, int t) {
        if (t >= mod) {
            t -= mod;
        }
        root.open = ++order;
        dfnToNode[root.open] = root;
        root.depth = t;
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, t + e.w);
        }
        root.close = order;
    }

    public void updateInterval(int l, int r, long x) {
        updBuf.tl = x;
        st.update(l, r, 1, n, updBuf);
    }

    public long sumOfSquareDist(int l, int r, Node node) {
        sumBuf.clear();
        st.query(l, r, 1, n, sumBuf);
        long tu = node.depth;
        long ans = tu * tu % mod * sumBuf.size % mod
                + 4 * sumBuf.sqTl
                + sumBuf.sqTx
                + 2 * tu * sumBuf.tx % mod
                - 4 * tu * sumBuf.tl % mod
                - 4 * sumBuf.tlTx;
        ans = DigitUtils.mod(ans, mod);
        return ans;
    }

    public void dfsForQuery(Node root, Node p) {
        updateInterval(root.open, root.close, root.depth);
        debug.debug("root", root);
        debug.debug("st", st);
        for (Query q : root.qs) {
            q.ans = 2 * sumOfSquareDist(q.v.open, q.v.close, root) - sumOfSquareDist(1, n, root);
            q.ans = DigitUtils.mod(q.ans, mod);
        }
        for (Edge e : root.adj) {
            Node node = e.other(root);
            if (node == p) {
                continue;
            }
            dfsForQuery(node, root);
        }
        if (p != null) {
            updateInterval(root.open, root.close, p.depth);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        Node[] nodes = new Node[n];
        dfnToNode = new Node[n + 1];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            Node a = nodes[in.ri() - 1];
            Node b = nodes[in.ri() - 1];
            Edge e = new Edge();
            e.a = a;
            e.b = b;
            e.w = in.ri();
            e.a.adj.add(e);
            e.b.adj.add(e);
        }
        dfs(nodes[0], null, 0);
        st = new SegTree<>(1, n, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl sum = new SumImpl();
                    sum.size = 1;
                    sum.tx = dfnToNode[i].depth;
                    sum.sqTx = dfnToNode[i].depth * dfnToNode[i].depth % mod;
                    return sum;
                });
        int q = in.ri();
        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            queries[i] = new Query(nodes[in.ri() - 1], nodes[in.ri() - 1]);
            queries[i].u.qs.add(queries[i]);
        }
        dfsForQuery(nodes[0], null);
        for (Query query : queries) {
            out.println(query.ans);
        }
    }
}

class Edge {
    Node a;
    Node b;
    int w;

    public Node other(Node x) {
        return a == x ? b : a;
    }
}

class Node {
    List<Edge> adj = new ArrayList<>();
    int id;
    int open;
    int close;
    long depth;

    @Override
    public String toString() {
        return "" + (id + 1);
    }

    List<Query> qs = new ArrayList<>();

}

class Query {
    Node u;
    Node v;
    long ans;

    public Query(Node u, Node v) {
        this.u = u;
        this.v = v;
    }
}

class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    long tl;

    @Override
    public void update(UpdateImpl update) {
        tl = update.tl;
    }

    @Override
    public void clear() {
        tl = -1;
    }

    @Override
    public boolean ofBoolean() {
        return tl >= 0;
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    static int mod = (int) 1e9 + 7;

    int size;
    long sqTl;
    long sqTx;
    long tx;
    long tl;
    long tlTx;

    public void clear() {
        sqTx = sqTl = tx = tl = tlTx = size = 0;
    }

    @Override
    public void add(SumImpl sum) {
        size += sum.size;
        sqTl += sum.sqTl;
        if (sqTl >= mod) {
            sqTl -= mod;
        }
        sqTx += sum.sqTx;
        if (sqTx >= mod) {
            sqTx -= mod;
        }
        tx += sum.tx;
        if (tx >= mod) {
            tx -= mod;
        }
        tl += sum.tl;
        if (tl >= mod) {
            tl -= mod;
        }
        tlTx += sum.tlTx;
        if (tlTx >= mod) {
            tlTx -= mod;
        }

    }

    @Override
    public void update(UpdateImpl update) {
        tl = update.tl * size % mod;
        sqTl = update.tl * update.tl % mod * size % mod;
        tlTx = update.tl * tx % mod;
    }

    @Override
    public void copy(SumImpl sum) {
        size = sum.size;
        tl = sum.tl;
        tx = sum.tx;
        sqTl = sum.sqTl;
        sqTx = sum.sqTx;
        tlTx = sum.tlTx;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "(" + tx + "," + tl + ")";
    }
}