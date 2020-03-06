import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            P5180 solver = new P5180();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P5180 {
        int[] size;
        List<DirectedEdge>[] tree;
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            size = new int[n];
            List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
            tree = Graph.createDirectedGraph(n);
            for (int i = 0; i < m; i++) {
                int u = in.readInt() - 1;
                int v = in.readInt() - 1;
                Graph.addEdge(g, u, v);
            }
            DominatorTree dt = new DominatorTree(g, 0);
            for (int i = 1; i < n; i++) {
                int p = dt.parent(i);
                if (p == -1) {
                    continue;
                }
                Graph.addEdge(tree, p, i);
            }
            dfs(0);
            debug.debug("dt", dt);
            for (int i = 0; i < n; i++) {
                out.append(size[i]).append(' ');
            }
        }

        public void dfs(int root) {
            size[root] = 1;
            for (DirectedEdge e : tree[root]) {
                dfs(e.to);
                size[root] += size[e.to];
            }
        }

    }

    static class CachedLog2 {
        private static final int BITS = 16;
        private static final int LIMIT = 1 << BITS;
        private static final byte[] CACHE = new byte[LIMIT];

        static {
            int b = 0;
            for (int i = 0; i < LIMIT; i++) {
                while ((1 << (b + 1)) <= i) {
                    b++;
                }
                CACHE[i] = (byte) b;
            }
        }

        public static int floorLog(int x) {
            return x < LIMIT ? CACHE[x] : (BITS + CACHE[x >>> BITS]);
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(10 << 20);
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

    static class LcaOnTree {
        int[] parent;
        int[] preOrder;
        int[] i;
        int[] head;
        int[] a;
        int time;

        void dfs1(List<? extends DirectedEdge>[] tree, int u, int p) {
            parent[u] = p;
            i[u] = preOrder[u] = time++;
            for (DirectedEdge e : tree[u]) {
                int v = e.to;
                if (v == p) continue;
                dfs1(tree, v, u);
                if (Integer.lowestOneBit(i[u]) < Integer.lowestOneBit(i[v])) {
                    i[u] = i[v];
                }
            }
            head[i[u]] = u;
        }

        void dfs2(List<? extends DirectedEdge>[] tree, int u, int p, int up) {
            a[u] = up | Integer.lowestOneBit(i[u]);
            for (DirectedEdge e : tree[u]) {
                int v = e.to;
                if (v == p) continue;
                dfs2(tree, v, u, a[u]);
            }
        }

        public LcaOnTree(List<? extends DirectedEdge>[] tree, int root) {
            int n = tree.length;
            preOrder = new int[n];
            i = new int[n];
            head = new int[n];
            a = new int[n];
            parent = new int[n];

            dfs1(tree, root, -1);
            dfs2(tree, root, -1, 0);
        }

        private int enterIntoStrip(int x, int hz) {
            if (Integer.lowestOneBit(i[x]) == hz)
                return x;
            int hw = 1 << CachedLog2.floorLog(a[x] & (hz - 1));
            return parent[head[i[x] & -hw | hw]];
        }

        public int lca(int x, int y) {
            int hb = i[x] == i[y] ? Integer.lowestOneBit(i[x]) : (1 << CachedLog2.floorLog(i[x] ^ i[y]));
            int hz = Integer.lowestOneBit(a[x] & a[y] & -hb);
            int ex = enterIntoStrip(x, hz);
            int ey = enterIntoStrip(y, hz);
            return preOrder[ex] < preOrder[ey] ? ex : ey;
        }

    }

    static class DirectedEdge {
        public int to;

        public DirectedEdge(int to) {
            this.to = to;
        }

        public String toString() {
            return "->" + to;
        }

    }

    static class IntegerMultiWayStack {
        private int[] values;
        private int[] next;
        private int[] heads;
        private int alloc;
        private int stackNum;

        public IntegerIterator iterator(final int queue) {
            return new IntegerIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public int next() {
                    int ans = values[ele];
                    ele = next[ele];
                    return ans;
                }
            };
        }

        private void doubleCapacity() {
            int newSize = Math.max(next.length + 10, next.length * 2);
            next = Arrays.copyOf(next, newSize);
            values = Arrays.copyOf(values, newSize);
        }

        public void alloc() {
            alloc++;
            if (alloc >= next.length) {
                doubleCapacity();
            }
            next[alloc] = 0;
        }

        public IntegerMultiWayStack(int qNum, int totalCapacity) {
            values = new int[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            heads = new int[qNum];
            stackNum = qNum;
        }

        public void addLast(int qId, int x) {
            alloc();
            values[alloc] = x;
            next[alloc] = heads[qId];
            heads[qId] = alloc;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stackNum; i++) {
                builder.append(i).append(": ");
                for (IntegerIterator iterator = iterator(i); iterator.hasNext(); ) {
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

    static class Graph {
        public static void addEdge(List<DirectedEdge>[] g, int s, int t) {
            g[s].add(new DirectedEdge(t));
        }

        public static List<DirectedEdge>[] createDirectedGraph(int n) {
            List<DirectedEdge>[] ans = new List[n];
            for (int i = 0; i < n; i++) {
                ans[i] = new ArrayList<>();
            }
            return ans;
        }

    }

    static class DominatorTree {
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
        DominatorTree.Segment segment;
        DominatorTree.SegmentQuery sq = new DominatorTree.SegmentQuery();
        Debug debug = new Debug(true);
        private int order = 0;

        public DominatorTree(List<? extends DirectedEdge>[] g, int root) {
            this.root = root;
            this.g = g;
            this.span = Graph.createDirectedGraph(g.length);
            sdom = new int[g.length];
            idom = new int[g.length];
            Arrays.fill(idom, -1);
            depth = new int[g.length];
            stk = new int[g.length];
            segment = new DominatorTree.Segment(0, g.length);
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

        public String toString() {
            return Arrays.toString(idom);
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
            private DominatorTree.Segment left;
            private DominatorTree.Segment right;
            private int s;

            public void pushUp() {
                s = Math.min(left.s, right.s);
            }

            public void pushDown() {
            }

            public Segment(int l, int r) {
                if (l < r) {
                    int m = (l + r) >> 1;
                    left = new DominatorTree.Segment(l, m);
                    right = new DominatorTree.Segment(m + 1, r);
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

            public void query(int ll, int rr, int l, int r, DominatorTree.SegmentQuery q) {
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

            private DominatorTree.Segment deepClone() {
                DominatorTree.Segment seg = clone();
                if (seg.left != null) {
                    seg.left = seg.left.deepClone();
                }
                if (seg.right != null) {
                    seg.right = seg.right.deepClone();
                }
                return seg;
            }

            protected DominatorTree.Segment clone() {
                try {
                    return (DominatorTree.Segment) super.clone();
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

            public String toString() {
                StringBuilder builder = new StringBuilder();
                deepClone().toString(builder);
                if (builder.length() > 0) {
                    builder.setLength(builder.length() - 1);
                }
                return builder.toString();
            }

        }

    }

    static class FastInput {
        private final InputStream is;
        private byte[] buf = new byte[1 << 20];
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

    }

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }
}

