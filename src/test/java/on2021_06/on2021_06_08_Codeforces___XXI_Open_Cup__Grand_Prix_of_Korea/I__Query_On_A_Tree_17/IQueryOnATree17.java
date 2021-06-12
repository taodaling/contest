package on2021_06.on2021_06_08_Codeforces___XXI_Open_Cup__Grand_Prix_of_Korea.I__Query_On_A_Tree_17;



import template.graph.KthAncestorOnTreeByBinaryLift;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;

public class IQueryOnATree17 {
    HeavyLightDecompose hld;
    long sum;
    KthAncestorOnTreeByBinaryLift ka;
    int centroid = 0;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        hld = new HeavyLightDecompose(Long::sum, n, 0);
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            hld.addEdge(a, b);
        }
        hld.finish();
        ka = new KthAncestorOnTreeByBinaryLift(n);
        ka.init(i -> hld.nodes[i].father == null ? -1 : hld.nodes[i].father.id, n);

        int q = in.ri();

        for (int i = 0; i < q; i++) {
            debug.debug("i", i);
            debug.debug("centroid", centroid);
            debug.debug("hld", hld.segment);
            best = -1;
            int t = in.ri();
            int u = in.ri() - 1;
            if (t == 1) {
                hld.updateSubtree(u, 1);
                sum = hld.querySubtree(0);

                considerPath(centroid, 0);
                considerPath(hld.nodes[u].getEnd().id, 0);
            } else {
                int v = in.ri() - 1;
                hld.updatePath(u, v, 1);
                sum = hld.querySubtree(0);

                int lca = ka.lca(u, v);
                if (u == lca || hld.querySubtree(ka.kthAncestor(u, ka.depth(u) - ka.depth(lca) - 1)) * 2 <= sum) {
                    considerPath(v, 0);
                } else {
                    considerPath(u, 0);
                }
                considerPath(centroid, 0);
            }

            centroid = best;
            out.println(centroid + 1);
        }
    }

    int best;

    void updateBest(int x) {
        if (best == -1 || x != -1 && ka.depth(best) < ka.depth(x)) {
            best = x;
        }
    }

    public void considerPath(int a, int b) {
        assert ka.inSubtree(a, b);
        int v = a;
        if(hld.querySubtree(v) * 2 > sum){
            updateBest(v);
            return;
        }
        for(int i = ka.log; i >= 0; i--){
            if(ka.jump[i][v] == -1){
                continue;
            }
            int u = ka.jump[i][v];
            if(hld.querySubtree(u) * 2 <= sum){
                v = u;
            }
        }
        v = ka.jump[0][v];
        updateBest(v);
    }

    Debug debug = new Debug(false);
}

class HeavyLightDecompose {
    private LongBinaryOperator op;

    public static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private long val;
        private long dirty;
        private int size;

        public void pushUp() {
            val = left.val + right.val;
        }

        public void modify(long x) {
            val += x * size;
            dirty += x;
        }

        public void pushDown() {
            if (dirty != 0) {
                left.modify(dirty);
                right.modify(dirty);
                dirty = 0;
            }
        }

        public Segment(int l, int r, IntFunction<HLDNode> function) {
            if (l < r) {
                int m = DigitUtils.floorAverage(l, r);
                left = new Segment(l, m, function);
                right = new Segment(m + 1, r, function);
                pushUp();
                size = left.size + right.size;
            } else {
                val = function.apply(l).val;
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
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public long query(int ll, int rr, int l, int r, LongBinaryOperator op) {
            if (noIntersection(ll, rr, l, r)) {
                return 0;
            }
            if (covered(ll, rr, l, r)) {
                return val;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            return op.applyAsLong(left.query(ll, rr, l, m, op),
                    right.query(ll, rr, m + 1, r, op));
        }

        private void toString(StringBuilder builder) {
            if (left == null && right == null) {
                builder.append(val).append(",");
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
    }

    public static class HLDNode {
        List<HLDNode> next = new ArrayList<>();
        int id;
        int dfsOrderFrom;
        int dfsOrderTo;
        int size;
        long val;
        HLDNode link;
        HLDNode heavy;
        HLDNode father;

        HLDNode end;

        public HLDNode getEnd() {
            if (heavy == null) {
                return this;
            }
            if (end == null) {
                end = heavy.getEnd();
            }
            return end;
        }

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

    public void updateSubtree(int nodeId, long val) {
        HLDNode node = nodes[nodeId];
        segment.update(node.dfsOrderFrom, node.dfsOrderTo, 1, n, val);
    }

    public void finish() {
        dfs(root, null);
        dfs2(root, root);
        segIndexToNode = new HLDNode[n + 1];
        for (int i = 0; i < n; i++) {
            segIndexToNode[nodes[i].dfsOrderFrom] = nodes[i];
        }
        segment = new Segment(1, n, i -> segIndexToNode[i]);
    }

    public long querySubtree(int uId) {
        return segment.query(nodes[uId].dfsOrderFrom, nodes[uId].dfsOrderTo, 1, n, op);
    }

    public long processPath(int uId, int vId) {
        HLDNode u = nodes[uId];
        HLDNode v = nodes[vId];
        long sum = 0;
        while (u != v) {
            if (u.link == v.link) {
                if (u.size > v.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                sum = op.applyAsLong(sum, segment.query(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n, op));
                u = v;
            } else {
                if (u.link.size > v.link.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                sum = op.applyAsLong(sum, segment.query(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n, op));
                u = u.link.father;
            }
        }
        sum = op.applyAsLong(sum, segment.query(u.dfsOrderFrom, u.dfsOrderFrom, 1, n, op));
        return sum;
    }

    public void updatePath(int uId, int vId, long x) {
        HLDNode u = nodes[uId];
        HLDNode v = nodes[vId];
        while (u != v) {
            if (u.link == v.link) {
                if (u.size > v.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.update(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n, x);
                u = v;
            } else {
                if (u.link.size > v.link.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.update(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n, x);
                u = u.link.father;
            }
        }
        segment.update(u.dfsOrderFrom, u.dfsOrderFrom, 1, n, x);
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
    HLDNode[] segIndexToNode;
    Segment segment;
}