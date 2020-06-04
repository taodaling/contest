package contest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;

public class InThePathToMosque {
    public long solve(int n, int q, int A, int B, int t) {
        int[] par = new int[n];
        int[] w = new int[n];
        int[] u = new int[q];
        int[] f = new int[q];

        par[1] = 0;
        for (int i = 2; i <= n - 1; i++) {
            par[i] = Math.max(0, i - 1 - (int) (((long) par[i - 1] * A + B) % t));
        }
        w[1] = B;
        for (int i = 2; i <= n - 1; i++) {
            w[i] = (int) (((long) w[i - 1] * A + B) % (int) 1e9);
        }
        u[0] = B % n;
        for (int i = 1; i <= q - 1; i++) {
            u[i] = (int) (((long) u[i - 1] * A + B) % n);
        }

        f[0] = B;
        for (int i = 1; i <= q - 1; i++) {
            f[i] = (int) (((long) f[i - 1] * A + B) % (int) 1e9);
        }

        HeavyLightDecompose hld = new HeavyLightDecompose(n, 0);
        for (int i = 1; i < n; i++) {
            hld.addEdge(i, par[i]);
            hld.nodes[i].b += w[i];
        }
        hld.finish();
        long sum = 0;
        for (int i = 0; i < q; i++) {
            int end = hld.processPath(u[i], f[i]);
            sum += end;
        }
        return sum;
    }
}

class HeavyLightDecompose {

    public static class SegmentQuery {
        int index;
        long sum;
        boolean end;

        public void reset(int sum) {
            index = -1;
            this.sum = sum;
            end = false;
        }
    }

    public static class Segment {
        private Segment left;
        private Segment right;
        private long a;
        private long b;
        private long min;
        private long sum;

        public void pushUp() {
            min = Math.min(left.min + right.sum, right.min);
            sum = left.sum + right.sum;
        }

        public void modify(long x) {
            this.a += x;
            reset();
        }

        public void reset() {
            this.sum = a + b;
            this.min = a - b;
        }

        public void pushDown() {
        }

        public Segment(int l, int r, IntFunction<HLDNode> function) {
            if (l < r) {
                int m = DigitUtils.floorAverage(l, r);
                left = new Segment(l, m, function);
                right = new Segment(m + 1, r, function);
                pushUp();
            } else {
                a = function.apply(l).a;
                b = function.apply(l).b;
                reset();
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

        public void query(int ll, int rr, int l, int r, SegmentQuery q) {
            if (noIntersection(ll, rr, l, r) || q.end) {
                return;
            }
            q.index = l;
            if (covered(ll, rr, l, r) && min + q.sum >= 0) {
                q.sum += sum;
                return;
            }
            if (l == r) {
                q.sum += a;
                q.end = true;
                return;
            }

            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            right.query(ll, rr, m + 1, r, q);
            left.query(ll, rr, l, m, q);
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

        long a;
        long b;

        @Override
        public String toString() {
            return "" + id;
        }
    }

    public HeavyLightDecompose(int n, int rootId) {
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
        segIndexToNode = new HLDNode[n + 1];
        for (int i = 0; i < n; i++) {
            segIndexToNode[nodes[i].dfsOrderFrom] = nodes[i];
        }
        segment = new Segment(1, n, i -> segIndexToNode[i]);
    }

    public int processPath(int uId, int f) {
        HLDNode u = nodes[uId];
        sq.reset(f);
        while (!sq.end && u != null) {
            segment.query(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n, sq);
            u = u.link.father;
        }
        segment.update(sq.index, sq.index, 1, n, sq.sum);
        return segIndexToNode[sq.index].id;
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

    SegmentQuery sq = new SegmentQuery();
    int n;
    int order = 1;
    HLDNode root;
    HLDNode[] nodes;
    HLDNode[] segIndexToNode;
    Segment segment;
}