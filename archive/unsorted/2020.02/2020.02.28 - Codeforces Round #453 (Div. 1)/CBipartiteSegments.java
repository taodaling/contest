package contest;

import template.datastructure.DSU;
import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;

public class CBipartiteSegments {
    int[] parents;
    List<UndirectedEdge>[] g;
    int color;
    int[] colors;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        parents = new int[n];
        colors = new int[n];
        Arrays.fill(colors, -1);
        g = Graph.createUndirectedGraph(n);
        Edge[] edges = new Edge[m];
        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = in.readInt() - 1;
            edges[i].b = in.readInt() - 1;
            if (dsu.find(edges[i].a) != dsu.find(edges[i].b)) {
                edges[i].tree = true;
                dsu.merge(edges[i].a, edges[i].b);
                Graph.addUndirectedEdge(g, edges[i].a, edges[i].b);
            }
        }
        for (int i = 1; i < n; i++) {
            if (dsu.find(i) != dsu.find(i - 1)) {
                dsu.merge(i, i - 1);
                Graph.addUndirectedEdge(g, i, i - 1);
            }
        }

        dfs(0, -1);
        LcaOnTree lca = new LcaOnTree(g, 0);
        for (Edge e : edges) {
            if (e.tree) {
                continue;
            }
            int a = e.a;
            int b = e.b;
            int c = lca.lca(a, b);
            paint(a, c, color);
            paint(b, c, color);
            color++;
        }

        int[] right = new int[n];
        int[] limit = new int[color];
        for (int c : colors) {
            if (c == -1) {
                continue;
            }
            limit[c]++;
        }
        Machine machine = new Machine(color, limit);
        for (int i = 0, r = -1, l = 0; i < n; i++) {
            while (l < i) {
                machine.remove(colors[l]);
                l++;
            }
            while (r + 1 < n && machine.total == 0) {
                machine.add(colors[r + 1]);
                r++;
            }
            if (machine.total == 0) {
                right[i] = r;
            } else {
                right[i] = r - 1;
            }
        }

        //System.err.println(Arrays.toString(right));

        int q = in.readInt();
        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            queries[i] = new Query();
            queries[i].l = in.readInt() - 1;
            queries[i].r = in.readInt() - 1;
        }

        Query[] sorted = queries.clone();
        Arrays.sort(sorted, (a, b) -> -Integer.compare(a.l, b.l));
        Segment seg = new Segment(0, n);
        SimplifiedDeque<Query> dq = new Range2DequeAdapter<>(i -> sorted[i], 0, sorted.length - 1);
        for (int i = n - 1; i >= 0; i--) {
            seg.updatePlus(i, right[i], 0, n, 1);
            while (!dq.isEmpty() && dq.peekFirst().l == i) {
                Query head = dq.removeFirst();
                head.ans = seg.queryL(head.l, head.r, 0, n);
            }
        }

        for (Query query : queries) {
            out.println(query.ans);
        }
    }

    public void paint(int root, int end, int c) {
        colors[root] = c;
        if (root == end) {
            return;
        }
        paint(parents[root], end, c);
    }

    public void dfs(int root, int p) {
        parents[root] = p;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
    }
}

class Query {
    int l;
    int r;
    long ans;
}

class Machine {
    int total;
    int[] cnts;
    int[] limit;

    public Machine(int m, int[] limit) {
        cnts = new int[m];
        this.limit = limit;
    }

    public void add(int x) {
        if (x < 0) {
            return;
        }
        cnts[x]++;
        if (cnts[x] == limit[x]) {
            total++;
        }
    }

    public void remove(int x) {
        if (x < 0) {
            return;
        }
        cnts[x]--;
        if (cnts[x] + 1 == limit[x]) {
            total--;
        }
    }
}

class Edge {
    int a;
    int b;
    boolean tree;
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long sum;
    private int size;
    private long dirty;

    public void modify(long x) {
        sum += x * size;
        dirty += x;
    }

    public void pushUp() {
        size = left.size + right.size;
        sum = left.sum + right.sum;
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
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            size = 1;
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
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.queryL(ll, rr, l, m) +
                right.queryL(ll, rr, m + 1, r);
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