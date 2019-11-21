package template.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;

public class HeavyLightDecompose {
    private LongBinaryOperator op;

    public class Segment {
        private Segment left;
        private Segment right;
        private long val;

        public void pushUp() {

        }

        public void pushDown() {
        }

        public Segment(int l, int r, long[] vals) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m, vals);
                right = new Segment(m + 1, r, vals);
                pushUp();
            } else {
                val = vals[l];
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void update(int ll, int rr, int l, int r, long val) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                this.val = val;
                return;
            }
            pushDown();
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, val);
            right.update(ll, rr, m + 1, r, val);
            pushUp();
        }

        public long query(int ll, int rr, int l, int r) {
            if (noIntersection(ll, rr, l, r)) {
                return 0;
            }
            if (covered(ll, rr, l, r)) {
                return val;
            }
            pushDown();
            int m = (l + r) >> 1;
            return op.applyAsLong(left.query(ll, rr, l, m),
                    right.query(ll, rr, m + 1, r));
        }
    }

    public static class HLDNode {
        List<HLDNode> next = new ArrayList<>(2);
        int id;
        int dfsOrderFrom;
        int dfsOrderTo;
        int size;
        long val;
        HLDNode link;
        HLDNode heavy;
        HLDNode father;

        @Override
        public String toString() {
            return "" + id;
        }
    }

    public HeavyLightDecompose(LongBinaryOperator op, int n, int rootId) {
        this.op = op;
        this.n = n;
        nodes = new HLDNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new HLDNode();
            nodes[i].id = i;
        }
        root = nodes[rootId];
    }

    public void addEdge(int a, int b) {
        nodes[a].next.add(nodes[b]);
        nodes[b].next.add(nodes[a]);
    }

    public void setInitVal(int nodeId, long val) {
        nodes[nodeId].val = val;
    }

    public void updateVal(int nodeId, long val) {
        HLDNode node = nodes[nodeId];
        segment.update(node.dfsOrderFrom, node.dfsOrderFrom, 1, n, val);
    }

    public void finish() {
        dfs(root, null);
        dfs2(root, root);
        long[] vals = new long[n + 1];
        for (int i = 0; i < n; i++) {
            vals[nodes[i].dfsOrderFrom] = nodes[i].val;
        }
        segment = new Segment(1, n, vals);
    }

    public long processPath(int uId, int vId) {
        HLDNode u = nodes[uId];
        HLDNode v = nodes[vId];
        long sum = 1;
        while (u != v) {
            if (u.link == v.link) {
                if (u.size > v.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                sum = op.applyAsLong(sum, segment.query(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n));
                u = v;
            } else {
                if (u.link.size > v.link.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                sum = op.applyAsLong(sum, segment.query(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n));
                u = u.link.father;
            }
        }
        sum = op.applyAsLong(sum, segment.query(u.dfsOrderFrom, u.dfsOrderFrom, 1, n));
        return sum;
    }

    private static void dfs(HLDNode root, HLDNode father) {
        root.size = 1;
        root.father = father;
        for (HLDNode node : root.next) {
            if (node == father) {
                continue;
            }
            dfs(node, root);
            root.size += node.size;
            if (root.heavy == null || root.heavy.size < node.size) {
                root.heavy = node;
            }
        }
    }

    private void dfs2(HLDNode root, HLDNode link) {
        root.dfsOrderFrom = order++;
        root.link = link;
        if (root.heavy != null) {
            dfs2(root.heavy, link);
        }
        for (HLDNode node : root.next) {
            if (node == root.father || node == root.heavy) {
                continue;
            }
            dfs2(node, node);
        }
        root.dfsOrderTo = order - 1;
    }

    int n;
    int order = 1;
    HLDNode root;
    HLDNode[] nodes;
    Segment segment;
}