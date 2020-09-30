package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;

public class EWaitForIt {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int q = in.readInt();

        HeavyLightDecompose dec = new HeavyLightDecompose(Math::max, n, 0);
        for (int i = 0; i < n - 1; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            dec.addEdge(u, v);
        }

        long inf = (long) 1e18;

        int[] belong = new int[m];
        QueryObject qo = new QueryObject();
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            belong[i] = u;
            dec.setInitVal(u, i + 1);
        }
        dec.finish();

        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 0; i < q; i++) {
            int t = in.readInt();
            if (t == 1) {
                int u = in.readInt() - 1;
                int v = in.readInt() - 1;
                int k = in.readInt();

                list.clear();
                while (k > 0) {
                    qo.reset();
                    dec.processPath(u, v, qo);
                    if (qo.val >= inf) {
                        break;
                    }
                    k--;
                    list.add(qo.index + 1);
                    dec.pop(belong[qo.index]);
                }
                out.println(list.size());
                for (int j = 0; j < list.size(); j++) {
                    out.append(list.get(j)).append(' ');
                }
                out.println();
            } else {
                int v = in.readInt() - 1;
                int k = in.readInt();
                dec.updateSubtree(v, k);
            }
        }


    }
}

class QueryObject {
    long val;
    int index;

    public void reset() {
        val = (long) 1e18;
    }

    public void update(long val, int index) {
        if (this.val > val) {
            this.val = val;
            this.index = (int) 1e9;
        }
        if (this.val == val) {
            this.index = Math.min(index, this.index);
        }
    }
}


class HeavyLightDecompose {
    private LongBinaryOperator op;

    public static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private long val;
        private long minVal;
        private int minIndex;
        private static long inf = (long) 1e18;

        public void pushUp() {
            minVal = Math.min(left.minVal, right.minVal);
            minIndex = minVal == left.minVal ? left.minIndex : right.minIndex;
        }

        public void modify(long x) {
            this.val += x;
            this.minVal += x;
        }

        public void pushDown() {
            if (val != 0) {
                left.modify(val);
                right.modify(val);
                val = 0;
            }
        }

        public void replace(HLDNode node) {
            if (node.pq.isEmpty()) {
                minVal = inf;
            } else {
                int top = node.pq.remove();
                minVal = top + val;
                minIndex = top - 1;
            }
        }

        public Segment(int l, int r, IntFunction<HLDNode> function) {
            if (l < r) {
                int m = DigitUtils.floorAverage(l, r);
                left = new Segment(l, m, function);
                right = new Segment(m + 1, r, function);
                pushUp();
            } else {
                HLDNode node = function.apply(l);
                replace(node);
            }
        }

        private boolean covered(int ll, int rr, int l, int r) {
            return ll <= l && rr >= r;
        }

        private boolean noIntersection(int ll, int rr, int l, int r) {
            return ll > r || rr < l;
        }

        public void updateRep(int ll, int rr, int l, int r, HLDNode x) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                replace(x);
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.updateRep(ll, rr, l, m, x);
            right.updateRep(ll, rr, m + 1, r, x);
            pushUp();
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

        public void query(int ll, int rr, int l, int r, QueryObject qo) {
            if (noIntersection(ll, rr, l, r)) {
                return;
            }
            if (covered(ll, rr, l, r)) {
                qo.update(minVal, minIndex);
                return;
            }
            pushDown();
            int m = DigitUtils.floorAverage(l, r);
            left.query(ll, rr, l, m, qo);
            right.query(ll, rr, m + 1, r, qo);
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
        List<HLDNode> next = new ArrayList<>(2);
        int id;
        int dfsOrderFrom;
        int dfsOrderTo;
        int size;
        HLDNode link;
        HLDNode heavy;
        HLDNode father;
        PriorityQueue<Integer> pq = new PriorityQueue<>();

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

    public void updateSubtree(int u, long k) {
        segment.update(nodes[u].dfsOrderFrom, nodes[u].dfsOrderTo, 1, n, k);
    }

    public void addEdge(int a, int b) {
        nodes[a].next.add(nodes[b]);
        nodes[b].next.add(nodes[a]);
    }

    public void setInitVal(int nodeId, int val) {
        nodes[nodeId].pq.add(val);
    }

    public void pop(int nodeId) {
        HLDNode node = nodes[nodeId];
        segment.updateRep(node.dfsOrderFrom, node.dfsOrderFrom, 1, n, node);
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

    public void processPath(int uId, int vId, QueryObject qo) {
        HLDNode u = nodes[uId];
        HLDNode v = nodes[vId];
        while (u != v) {
            if (u.link == v.link) {
                if (u.size > v.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.query(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n, qo);
                u = v;
            } else {
                if (u.link.size > v.link.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.query(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n, qo);
                u = u.link.father;
            }
        }
        segment.query(u.dfsOrderFrom, u.dfsOrderFrom, 1, n, qo);
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