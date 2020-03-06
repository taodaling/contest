package template.graph;

import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;
import template.utils.Debug;

import java.util.Arrays;
import java.util.List;

public class DominatorTree {
    List<? extends DirectedEdge>[] g;
    IntegerMultiWayStack in;
    List<DirectedEdge>[] span;
    int[] depth;
    int[] dfn;
    int[] sdom;
    int[] idom;
    LcaOnTree lcaOnTree;
    int root;
    int[] stk;
    Segment segment;
    SegmentQuery sq = new SegmentQuery();

    Debug debug = new Debug(true);
    public DominatorTree(List<? extends DirectedEdge>[] g, int root) {
        this.root = root;
        this.g = g;
        this.span = Graph.createDirectedGraph(g.length);
        sdom = new int[g.length];
        idom = new int[g.length];
        Arrays.fill(idom, -1);
        depth = new int[g.length];
        stk = new int[g.length];
        segment = new Segment(0, g.length);
        int len = 0;
        for (List<?> list : g) {
            len += list.size();
        }
        in = new IntegerMultiWayStack(g.length, len);
        dfn = new int[g.length];
        generateTree1(root, -1);
        for (int i = 0; i < g.length; i++) {
            if (dfn[i] == 0) {
                continue;
            }
            for (DirectedEdge e : g[i]) {
                in.addLast(e.to, i);
            }
        }
        lcaOnTree = new LcaOnTree(span, root);
        dfsForSdom(root);
        dfsForIdom(root);
        idom[root] = -1;

        debug.debug("idom", idom);
        debug.debug("sdom", sdom);
        debug.debug("span", span);
    }

    public int parent(int i) {
        return idom[i];
    }

    private void dfsForIdom(int root) {
        stk[depth[root]] = root;
        segment.update(depth[root], depth[root], 0, span.length, dfn[sdom[root]]);
        sq.reset();
        segment.query(depth[sdom[root]] + 1, depth[root],
                0, span.length, sq);
        int u = stk[sq.index];
        if (sdom[root] == sdom[u]) {
            idom[root] = sdom[root];
        } else {
            idom[root] = idom[u];
        }
        for (DirectedEdge e : span[root]) {
            dfsForIdom(e.to);
        }
    }

    private void dfsForSdom(int root) {
        for (int i = span[root].size() - 1; i >= 0; i--) {
            DirectedEdge e = span[root].get(i);
            dfsForSdom(e.to);
        }
        int lca = root;
        for (IntegerIterator iterator = in.iterator(root);
             iterator.hasNext(); ) {
            int node = iterator.next();
            int s = dfn[node] < dfn[root] ? node : sdom[node];
            lca = lcaOnTree.lca(lca, s);
        }
        sdom[root] = lca;
    }

    private int order = 0;

    private void generateTree1(int root, int p) {
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        dfn[root] = ++order;
        for (DirectedEdge e : g[root]) {
            if (e.to == p || dfn[e.to] != 0) {
                continue;
            }
            span[root].add(e);
            generateTree1(e.to, root);
        }
    }

    private static class SegmentQuery {
        int s;
        int index;

        public void reset() {
            s = Integer.MAX_VALUE;
            index = 0;
        }

        public void update(int s, int index) {
            if (this.s > s) {
                this.s = s;
                this.index = index;
            }
        }
    }

    private static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private int s;

        public void pushUp() {
            s = Math.min(left.s, right.s);
        }

        public void pushDown() {
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
                s = x;
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public void query(int ll, int rr, int l, int r, SegmentQuery q) {
            if (noIntersection(ll, rr, l, r) || s >= q.s) {
                return;
            }
            if (l == r) {
                q.update(s, l);
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            if (left.s < right.s) {
                left.query(ll, rr, l, m, q);
                right.query(ll, rr, m + 1, r, q);
            } else {
                right.query(ll, rr, m + 1, r, q);
                left.query(ll, rr, l, m, q);
            }
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

    @Override
    public String toString() {
        return Arrays.toString(idom);
    }
}
