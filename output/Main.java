import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.LongBinaryOperator;
import java.util.PriorityQueue;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.AbstractCollection;
import java.io.OutputStreamWriter;
import java.util.function.IntFunction;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 27);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            ETrainTracks solver = new ETrainTracks();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class ETrainTracks {
        Debug debug = new Debug(true);

        {
            LCTNode.dq.clear();
            LCTNode.beginDq.clear();
            LCTNode.time = -1;
            LCTNode.hld = null;
        }

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

    static class HeavyLightDecompose {
        private LongBinaryOperator op;
        int n;
        int order = 1;
        HeavyLightDecompose.HLDNode root;
        HeavyLightDecompose.HLDNode[] nodes;
        HeavyLightDecompose.HLDNode[] segIndexToNode;
        HeavyLightDecompose.Segment segment;

        public HeavyLightDecompose(LongBinaryOperator op, int n, int rootId) {
            this.op = op;
            this.n = n;
            nodes = new HeavyLightDecompose.HLDNode[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new HeavyLightDecompose.HLDNode();
                nodes[i].id = i;
            }
            root = nodes[rootId];
        }

        public void addEdge(int a, int b) {
            nodes[a].next.add(nodes[b]);
            nodes[b].next.add(nodes[a]);
        }

        public void finish() {
            dfs(root, null);
            dfs2(root, root);
            segIndexToNode = new HeavyLightDecompose.HLDNode[n + 1];
            for (int i = 0; i < n; i++) {
                segIndexToNode[nodes[i].dfsOrderFrom] = nodes[i];
            }
            segment = new HeavyLightDecompose.Segment(1, n, i -> segIndexToNode[i]);
        }

        public long processPath(int uId, int vId) {
            HeavyLightDecompose.HLDNode u = nodes[uId];
            HeavyLightDecompose.HLDNode v = nodes[vId];
            long sum = (long) -1e18;
            while (u != v) {
                if (u.link == v.link) {
                    if (u.size > v.size) {
                        HeavyLightDecompose.HLDNode tmp = u;
                        u = v;
                        v = tmp;
                    }
                    sum = op.applyAsLong(sum, segment.query(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n, op));
                    u = v;
                } else {
                    if (u.link.size > v.link.size) {
                        HeavyLightDecompose.HLDNode tmp = u;
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
            HeavyLightDecompose.HLDNode u = nodes[uId];
            HeavyLightDecompose.HLDNode v = nodes[vId];
            while (u != v) {
                if (u.link == v.link) {
                    if (u.size > v.size) {
                        HeavyLightDecompose.HLDNode tmp = u;
                        u = v;
                        v = tmp;
                    }
                    segment.update(v.dfsOrderFrom + 1, u.dfsOrderFrom, 1, n, dirty);
                    u = v;
                } else {
                    if (u.link.size > v.link.size) {
                        HeavyLightDecompose.HLDNode tmp = u;
                        u = v;
                        v = tmp;
                    }
                    segment.update(u.link.dfsOrderFrom, u.dfsOrderFrom, 1, n, dirty);
                    u = u.link.father;
                }
            }
            segment.update(u.dfsOrderFrom, u.dfsOrderFrom, 1, n, dirty);
        }

        private static void dfs(HeavyLightDecompose.HLDNode root, HeavyLightDecompose.HLDNode father) {
            root.size = 1;
            root.father = father;
            for (HeavyLightDecompose.HLDNode node : root.next) {
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

        private void dfs2(HeavyLightDecompose.HLDNode root, HeavyLightDecompose.HLDNode link) {
            root.dfsOrderFrom = order++;
            root.link = link;
            if (root.heavy != null) {
                dfs2(root.heavy, link);
            }
            for (HeavyLightDecompose.HLDNode node : root.next) {
                if (node == root.father || node == root.heavy) {
                    continue;
                }
                dfs2(node, node);
            }
            root.dfsOrderTo = order - 1;
        }

        public static class Segment implements Cloneable {
            private HeavyLightDecompose.Segment left;
            private HeavyLightDecompose.Segment right;
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

            public Segment(int l, int r, IntFunction<HeavyLightDecompose.HLDNode> function) {
                if (l < r) {
                    int m = (l + r) >> 1;
                    left = new HeavyLightDecompose.Segment(l, m, function);
                    right = new HeavyLightDecompose.Segment(m + 1, r, function);
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

            public String toString() {
                StringBuilder builder = new StringBuilder();
                deepClone().toString(builder);
                if (builder.length() > 0) {
                    builder.setLength(builder.length() - 1);
                }
                return builder.toString();
            }

            private HeavyLightDecompose.Segment deepClone() {
                HeavyLightDecompose.Segment seg = clone();
                if (seg.left != null) {
                    seg.left = seg.left.deepClone();
                }
                if (seg.right != null) {
                    seg.right = seg.right.deepClone();
                }
                return seg;
            }

            protected HeavyLightDecompose.Segment clone() {
                try {
                    return (HeavyLightDecompose.Segment) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        public static class HLDNode {
            List<HeavyLightDecompose.HLDNode> next = new ArrayList<>(2);
            int id;
            int dfsOrderFrom;
            int dfsOrderTo;
            int size;
            HeavyLightDecompose.HLDNode link;
            HeavyLightDecompose.HLDNode heavy;
            HeavyLightDecompose.HLDNode father;

            public String toString() {
                return "" + id;
            }

        }

    }

    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 13];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastInput(InputStream is) {
            this.is = is;
        }

        private int read() {
            while (bufLen == bufOffset) {
                bufOffset = 0;
                try {
                    bufLen = is.read(buf);
                } catch (IOException e) {
                    bufLen = -1;
                }
                if (bufLen == -1) {
                    return -1;
                }
            }
            return buf[bufOffset++];
        }

        public void skipBlank() {
            while (next >= 0 && next <= 32) {
                next = read();
            }
        }

        public int readInt() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            int val = 0;
            if (sign == 1) {
                while (next >= '0' && next <= '9') {
                    val = val * 10 + next - '0';
                    next = read();
                }
            } else {
                while (next >= '0' && next <= '9') {
                    val = val * 10 - next + '0';
                    next = read();
                }
            }

            return val;
        }

        public long readLong() {
            int sign = 1;

            skipBlank();
            if (next == '+' || next == '-') {
                sign = next == '+' ? 1 : -1;
                next = read();
            }

            long val = 0;
            if (sign == 1) {
                while (next >= '0' && next <= '9') {
                    val = val * 10 + next - '0';
                    next = read();
                }
            } else {
                while (next >= '0' && next <= '9') {
                    val = val * 10 - next + '0';
                    next = read();
                }
            }

            return val;
        }

    }

    static class LCTNode {
        public static final LCTNode NIL = new LCTNode();
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

        static {
            NIL.left = NIL;
            NIL.right = NIL;
            NIL.father = NIL;
            NIL.treeFather = NIL;
            NIL.id = -1;
        }

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

    static class Edge {
        LCTNode a;
        LCTNode b;
        long d;

        LCTNode other(LCTNode x) {
            return x == a ? b : a;
        }

    }

    static class LongMultiWayDeque {
        private long[] values;
        private int[] next;
        private int[] prev;
        private int[] heads;
        private int[] tails;
        private int alloc;
        private int queueNum;

        public LongIterator iterator(final int queue) {
            return new LongIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public long next() {
                    long ans = values[ele];
                    ele = next[ele];
                    return ans;
                }
            };
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            prev = Arrays.copyOf(prev, newSize);
            values = Arrays.copyOf(values, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
        }

        public void clear() {
            alloc = 0;
            Arrays.fill(heads, 0, queueNum, 0);
            Arrays.fill(tails, 0, queueNum, 0);
        }

        public boolean isEmpty(int qId) {
            return heads[qId] == 0;
        }

        public LongMultiWayDeque(int qNum, int totalCapacity) {
            values = new long[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            prev = new int[totalCapacity + 1];
            heads = new int[qNum];
            tails = new int[qNum];
            queueNum = qNum;
        }

        public void addLast(int qId, long x) {
            alloc();
            values[alloc] = x;

            if (heads[qId] == 0) {
                heads[qId] = tails[qId] = alloc;
                return;
            }
            next[tails[qId]] = alloc;
            prev[alloc] = tails[qId];
            tails[qId] = alloc;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < queueNum; i++) {
                if (isEmpty(i)) {
                    continue;
                }
                builder.append(i).append(": ");
                for (LongIterator iterator = iterator(i); iterator.hasNext(); ) {
                    builder.append(iterator.next()).append(",");
                }
                if (builder.charAt(builder.length() - 1) == ',') {
                    builder.setLength(builder.length() - 1);
                }
                builder.append('\n');
            }
            return builder.toString();
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (x == null || !x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(1 << 20);
        private final Writer os;

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
        }

        public FastOutput println() {
            cache.append(System.lineSeparator());
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
                os.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }

    static interface LongIterator {
        boolean hasNext();

        long next();

    }
}

