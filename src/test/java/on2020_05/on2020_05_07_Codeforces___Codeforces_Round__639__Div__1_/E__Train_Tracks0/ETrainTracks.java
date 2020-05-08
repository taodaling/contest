package on2020_05.on2020_05_07_Codeforces___Codeforces_Round__639__Div__1_.E__Train_Tracks0;




import jdk.nashorn.internal.ir.LiteralNode;
import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;
import template.primitve.generated.datastructure.LongIterator;
import template.primitve.generated.datastructure.LongMultiWayDeque;
import template.primitve.generated.datastructure.LongMultiWayStack;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;

public class ETrainTracks {
    {
        LCTNode.dq.clear();
        LCTNode.beginDq.clear();
        LCTNode.time = -1;
        LCTNode.hld = null;
    }

    Debug debug = new Debug(true);


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        LCTNode[] nodes = new LCTNode[n];
        HeavyLightDecompose hld = new HeavyLightDecompose(Long::max, n, 0);
        LCTNode.hld = hld;

        for (int i = 0; i < n; i++) {
            nodes[i] = new LCTNode();
            nodes[i].id = i;
        }
        for (int i = 0; i < n - 1; i++) {
            LCTNode a = nodes[in.readInt() - 1];
            LCTNode b = nodes[in.readInt() - 1];
            Edge edge = new Edge();
            edge.a = a;
            edge.b = b;
            edge.d = in.readInt();
            a.next.add(edge);
            b.next.add(edge);

            hld.addEdge(a.id, b.id);
        }

        hld.finish();

        dfs(nodes[0], null, 0);
        LCTNode.time = -1;
        for (int i = 0; i < m; i++) {
            LCTNode to = nodes[in.readInt() - 1];
            long time = in.readLong();
            LCTNode.time = time;
            LCTNode.access(to);
            hld.processPathUpdate(0, to.id, time);
        }

        debug.debug("dq", LCTNode.dq);
        debug.debug("begin", LCTNode.beginDq);
        PriorityQueue<LCTNode> wait = new PriorityQueue<>(n, (a, b) -> Long.compare(a.begin, b.begin));
        PriorityQueue<LCTNode> pq = new PriorityQueue<>(n, (a, b) -> Long.compare(a.front, b.front));
        for (LCTNode node : nodes) {
            if (LCTNode.dq.isEmpty(node.id)) {
                continue;
            }

            node.iterator = LCTNode.dq.iterator(node.id);
            node.beginIterator = LCTNode.beginDq.iterator(node.id);
            node.begin = Math.max(1, node.beginIterator.next());
            node.front = node.iterator.next();
            wait.add(node);
        }

        long inf = (long) 1e18;
        long time = inf;
        for (long i = 1; pq.size() + wait.size() > 0; i++) {
            if (pq.isEmpty()) {
                i = Math.max(i, wait.peek().begin);
            }

            while (!wait.isEmpty() && wait.peek().begin <= i) {
                pq.add(wait.remove());
            }

            LCTNode head = pq.remove();
            if (head.front < i) {
                time = head.front;
                break;
            }

            if (head.iterator.hasNext()) {
                head.begin = head.beginIterator.next();
                head.front = head.iterator.next();
                wait.add(head);
            }
        }


        int req = 0;
        out.append(time == inf ? -1 : time).append(' ');
        for (int i = 0; i < n; i++) {
            for (LongIterator iterator = LCTNode.dq.iterator(i); iterator.hasNext(); ) {
                long next = iterator.next();
                if (next < time) {
                    req++;
                }
            }
        }

        out.println(req);
    }


    public void dfs(LCTNode root, LCTNode p, long depth) {
        root.depth = depth;
        for (Edge e : root.next) {
            LCTNode node = e.other(root);
            if (node == p) {
                continue;
            }
            dfs(node, root, depth + e.d);
            root.pushDown();
            root.right.father = LCTNode.NIL;
            root.right.treeFather = root;
            root.setRight(node);
            root.pushUp();
        }
    }
}


class HeavyLightDecompose {
    private LongBinaryOperator op;

    public static class Segment implements Cloneable {
        private Segment left;
        private Segment right;
        private static long inf = (long) 1e18;
        private long val = -inf;
        private long dirty = -inf;

        public void pushUp() {
        }

        public void modify(long x) {
            this.val = Math.max(x, val);
            this.dirty = Math.max(x, dirty);
        }

        public void pushDown() {
            left.modify(dirty);
            right.modify(dirty);
            dirty = -inf;
        }

        public Segment(int l, int r, IntFunction<HLDNode> function) {
            if (l < r) {
                int m = (l + r) >> 1;
                left = new Segment(l, m, function);
                right = new Segment(m + 1, r, function);
                pushUp();
            } else {
                //val = function.apply(l).val;
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
            int m = (l + r) >> 1;
            left.update(ll, rr, l, m, x);
            right.update(ll, rr, m + 1, r, x);
            pushUp();
        }

        public long query(int ll, int rr, int l, int r, LongBinaryOperator op) {
            if (noIntersection(ll, rr, l, r)) {
                return -inf;
            }
            if (covered(ll, rr, l, r)) {
                return val;
            }
            pushDown();
            int m = (l + r) >> 1;
            return op.applyAsLong(left.query(ll, rr, l, m, op),
                    right.query(ll, rr, m + 1, r, op));
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
        segIndexToNode = new HLDNode[n + 1];
        for (int i = 0; i < n; i++) {
            segIndexToNode[nodes[i].dfsOrderFrom] = nodes[i];
        }
        segment = new Segment(1, n, i -> segIndexToNode[i]);
    }

    public long processPath(int uId, int vId) {
        HLDNode u = nodes[uId];
        HLDNode v = nodes[vId];
        long sum = (long) -1e18;
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

    public void processPathUpdate(int uId, int vId, long dirty) {
        HLDNode u = nodes[uId];
        HLDNode v = nodes[vId];
        while (u != v) {
            if (u.link == v.link) {
                if (u.size > v.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.update(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n, dirty);
                u = v;
            } else {
                if (u.link.size > v.link.size) {
                    HLDNode tmp = u;
                    u = v;
                    v = tmp;
                }
                segment.update(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n, dirty);
                u = u.link.father;
            }
        }
        segment.update(u.dfsOrderFrom, u.dfsOrderFrom, 1, n, dirty);
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

class Edge {
    LCTNode a;
    LCTNode b;
    long d;

    LCTNode other(LCTNode x) {
        return x == a ? b : a;
    }
}

/**
 * Created by dalt on 2018/5/20.
 */
class LCTNode {
    public static final LCTNode NIL = new LCTNode();

    static {
        NIL.left = NIL;
        NIL.right = NIL;
        NIL.father = NIL;
        NIL.treeFather = NIL;
        NIL.id = -1;
    }

    LCTNode left = NIL;
    LCTNode right = NIL;
    LCTNode father = NIL;
    LCTNode treeFather = NIL;
    boolean reverse;
    int id;
    long front;
    long begin;
    LongIterator iterator;
    LongIterator beginIterator;

    static long inf = (long) 1e18;
    long lastTrain = -inf;

    List<Edge> next = new ArrayList<>();

    long depth;
    static LongMultiWayDeque dq = new LongMultiWayDeque(100000, 4000000);
    static LongMultiWayDeque beginDq = new LongMultiWayDeque(100000, 4000000);
    static long time = -1;
    static HeavyLightDecompose hld;


    public static void access(LCTNode x) {
        boolean skip = true;
        LCTNode last = NIL;
        while (x != NIL) {
            splay(x);
            LCTNode r = x.right;

            if (!skip) {
                x.right.father = NIL;
                x.right.treeFather = x;
                x.setRight(last);
                x.pushUp();

                r.pushDown();
                while (r.left != NIL) {
                    r = r.left;
                    r.pushDown();
                }
                splay(r);

                if (time != -1 && last != NIL) {
                    dq.addLast(x.id, time + x.depth);
                    beginDq.addLast(x.id, hld.processPath(r.id, r.id) + r.depth);
                }
            }
            skip = false;

            last = x;
            x = x.treeFather;
        }
    }

    public static void makeRoot(LCTNode x) {
        access(x);
        splay(x);
        x.reverse();
    }

    public static void cut(LCTNode y, LCTNode x) {
        makeRoot(y);
        access(x);
        splay(y);
        y.right.treeFather = NIL;
        y.right.father = NIL;
        y.setRight(NIL);
        y.pushUp();
    }

    public static void join(LCTNode y, LCTNode x) {
        makeRoot(x);
        x.treeFather = y;
    }

    public static void findRoute(LCTNode x, LCTNode y) {
        makeRoot(y);
        access(x);
    }

    public static void splay(LCTNode x) {
        if (x == NIL) {
            return;
        }
        LCTNode y, z;
        while ((y = x.father) != NIL) {
            if ((z = y.father) == NIL) {
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    zig(x);
                } else {
                    zag(x);
                }
            } else {
                z.pushDown();
                y.pushDown();
                x.pushDown();
                if (x == y.left) {
                    if (y == z.left) {
                        zig(y);
                        zig(x);
                    } else {
                        zig(x);
                        zag(x);
                    }
                } else {
                    if (y == z.left) {
                        zag(x);
                        zig(x);
                    } else {
                        zag(y);
                        zag(x);
                    }
                }
            }
        }

        x.pushDown();
        x.pushUp();
    }

    public static void zig(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.right;

        y.setLeft(b);
        x.setRight(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static void zag(LCTNode x) {
        LCTNode y = x.father;
        LCTNode z = y.father;
        LCTNode b = x.left;

        y.setRight(b);
        x.setLeft(y);
        z.changeChild(y, x);

        y.pushUp();
    }

    public static LCTNode findRoot(LCTNode x) {
        splay(x);
        x.pushDown();
        while (x.left != NIL) {
            x = x.left;
            x.pushDown();
        }
        splay(x);
        return x;
    }

    @Override
    public String toString() {
        return "" + id;
    }

    public void pushDown() {
        if (this == NIL) {
            return;
        }
        if (reverse) {
            reverse = false;

            LCTNode tmpNode = left;
            left = right;
            right = tmpNode;

            left.reverse();
            right.reverse();
        }

        left.lastTrain = lastTrain;
        right.lastTrain = lastTrain;
        left.treeFather = treeFather;
        right.treeFather = treeFather;
    }

    public void reverse() {
        reverse = !reverse;
    }

    public void setLeft(LCTNode x) {
        left = x;
        x.father = this;
    }

    public void setRight(LCTNode x) {
        right = x;
        x.father = this;
    }

    public void changeChild(LCTNode y, LCTNode x) {
        if (left == y) {
            setLeft(x);
        } else {
            setRight(x);
        }
    }

    public void pushUp() {
        if (this == NIL) {
            return;
        }
    }
}
