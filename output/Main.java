import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
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
            CXenonsAttackOnTheGangs solver = new CXenonsAttackOnTheGangs();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CXenonsAttackOnTheGangs {
        Node[] nodes;
        LcaOnTree lcaOnTree;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            List<Edge> edges = new ArrayList<>(n);
            MultiWayIntegerStack stack = new MultiWayIntegerStack(n, n * 2);
            for (int i = 0; i < n - 1; i++) {
                Edge e = new Edge();
                e.a = nodes[in.readInt() - 1];
                e.b = nodes[in.readInt() - 1];
                e.a.next.add(e);
                e.b.next.add(e);
                edges.add(e);
                stack.addLast(e.a.id, e.b.id);
                stack.addLast(e.b.id, e.a.id);
                if (e.a.id > e.b.id) {
                    Node tmp = e.a;
                    e.a = e.b;
                    e.b = tmp;
                }
            }

            lcaOnTree = new LcaOnTree(stack, 0);
            dfsForDepth(nodes[0], null, 0);
            int[][] dist = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    dist[i][j] = dist[j][i] = dist(nodes[i], nodes[j]);
                }
            }

            for (Edge e : edges) {
                dfs(e.a, e);
                dfs(e.b, e);
                e.sizeA = e.a.size;
                e.sizeB = e.b.size;
            }

            long[][] dp = new long[n][n];
            LongDequeImpl deque = new LongDequeImpl(n * n);
            for (Edge e : edges) {
                dp[e.a.id][e.b.id] = e.sizeOf(e.a) * e.sizeOf(e.b);
                deque.addLast(dpId(e.a.id, e.b.id));
            }
            long ans = 0;
            while (!deque.isEmpty()) {
                long state = deque.removeFirst();
                int a = DigitUtils.highBit(state);
                int b = DigitUtils.lowBit(state);
                ans = Math.max(ans, dp[a][b]);
                long nextDepth = dist(nodes[a], nodes[b]) + 1;

                Edge pa = null;
                Edge pb = null;
                for (Edge e : nodes[a].next) {
                    Node node = e.other(nodes[a]);
                    if (dist(node, nodes[b]) != nextDepth) {
                        pa = e;
                        break;
                    }
                }
                for (Edge e : nodes[b].next) {
                    Node node = e.other(nodes[b]);
                    if (dist(node, nodes[a]) != nextDepth) {
                        pb = e;
                        break;
                    }
                }
                for (Edge e : nodes[a].next) {
                    Node node = e.other(nodes[a]);
                    if (e == pa) {
                        continue;
                    }
                    int na = node.id;
                    int nb = b;
                    if (na > nb) {
                        int tmp = na;
                        na = nb;
                        nb = tmp;
                    }
                    boolean add = dp[na][nb] == 0;
                    dp[na][nb] = Math.max(dp[na][nb], dp[a][b] + e.sizeOf(node) * pb.sizeOf(nodes[b]));
                    if (add) {
                        deque.addLast(dpId(na, nb));
                    }
                }
                for (Edge e : nodes[b].next) {
                    Node node = e.other(nodes[b]);
                    if (e == pb) {
                        continue;
                    }
                    int na = a;
                    int nb = node.id;
                    if (na > nb) {
                        int tmp = na;
                        na = nb;
                        nb = tmp;
                    }
                    boolean add = dp[na][nb] == 0;
                    dp[na][nb] = Math.max(dp[na][nb], dp[a][b] + pa.sizeOf(nodes[a]) * e.sizeOf(node));
                    if (add) {
                        deque.addLast(dpId(na, nb));
                    }
                }
            }

            //System.err.println(Arrays.deepToString(dp));

            out.println(ans);
        }

        public long dpId(int a, int b) {
            if (a > b) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            return DigitUtils.asLong(a, b);
        }

        public int dist(Node a, Node b) {
            int lca = lcaOnTree.lca(a.id, b.id);
            return a.depth + b.depth - 2 * nodes[lca].depth;
        }

        public void dfs(Node root, Edge p) {
            root.size = 1;
            for (Edge e : root.next) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                dfs(node, e);
                root.size += node.size;
            }
        }

        public void dfsForDepth(Node root, Edge p, int depth) {
            root.depth = depth;
            for (Edge e : root.next) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                dfsForDepth(node, e, depth + 1);
            }
        }

    }

    static interface LongIterator {
        boolean hasNext();

        long next();

    }

    static interface LongDeque extends LongStack {
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

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput println(long c) {
            cache.append(c);
            println();
            return this;
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

    static class LongDequeImpl implements LongDeque {
        private long[] data;
        private int bpos;
        private int epos;
        private static final long[] EMPTY = new long[0];
        private int n;

        public LongDequeImpl(int cap) {
            if (cap == 0) {
                data = EMPTY;
            } else {
                data = new long[cap];
            }
            bpos = 0;
            epos = 0;
            n = cap;
        }

        private void expandSpace(int len) {
            while (n < len) {
                n = Math.max(n + 10, n * 2);
            }
            long[] newData = new long[n];
            if (bpos <= epos) {
                if (bpos < epos) {
                    System.arraycopy(data, bpos, newData, 0, epos - bpos);
                }
            } else {
                System.arraycopy(data, bpos, newData, 0, data.length - bpos);
                System.arraycopy(data, 0, newData, data.length - bpos, epos);
            }
            epos = size();
            bpos = 0;
            data = newData;
        }

        public LongIterator iterator() {
            return new LongIterator() {
                int index = bpos;


                public boolean hasNext() {
                    return index != epos;
                }


                public long next() {
                    long ans = data[index];
                    index = LongDequeImpl.this.next(index);
                    return ans;
                }
            };
        }

        public long removeFirst() {
            long ans = data[bpos];
            bpos = next(bpos);
            return ans;
        }

        public void addLast(long x) {
            ensureMore();
            data[epos] = x;
            epos = next(epos);
        }

        private int next(int x) {
            return x + 1 >= n ? 0 : x + 1;
        }

        private void ensureMore() {
            if (next(epos) == bpos) {
                expandSpace(n + 1);
            }
        }

        public int size() {
            int ans = epos - bpos;
            if (ans < 0) {
                ans += data.length;
            }
            return ans;
        }

        public boolean isEmpty() {
            return bpos == epos;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (LongIterator iterator = iterator(); iterator.hasNext(); ) {
                builder.append(iterator.next()).append(' ');
            }
            return builder.toString();
        }

    }

    static class Edge {
        Node a;
        Node b;
        int sizeA;
        int sizeB;

        long sizeOf(Node x) {
            return a == x ? sizeA : sizeB;
        }

        Node other(Node x) {
            return a == x ? b : a;
        }

    }

    static interface LongStack {
    }

    static class MultiWayIntegerStack {
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

        public int stackNumber() {
            return stackNum;
        }

        public MultiWayIntegerStack(int qNum, int totalCapacity) {
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

    static class Node {
        List<Edge> next = new ArrayList<>();
        int size;
        int id;
        int depth;

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

    }

    static class LcaOnTree {
        int[] parent;
        int[] preOrder;
        int[] i;
        int[] head;
        int[] a;
        int time;

        void dfs1(MultiWayIntegerStack tree, int u, int p) {
            parent[u] = p;
            i[u] = preOrder[u] = time++;
            for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
                int v = iterator.next();
                if (v == p) continue;
                dfs1(tree, v, u);
                if (Integer.lowestOneBit(i[u]) < Integer.lowestOneBit(i[v])) {
                    i[u] = i[v];
                }
            }
            head[i[u]] = u;
        }

        void dfs2(MultiWayIntegerStack tree, int u, int p, int up) {
            a[u] = up | Integer.lowestOneBit(i[u]);
            for (IntegerIterator iterator = tree.iterator(u); iterator.hasNext(); ) {
                int v = iterator.next();
                if (v == p) continue;
                dfs2(tree, v, u, a[u]);
            }
        }

        public LcaOnTree(MultiWayIntegerStack tree, int root) {
            int n = tree.stackNumber();
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

    static interface IntegerIterator {
        boolean hasNext();

        int next();

    }

    static class DigitUtils {
        private static long mask32 = (1L << 32) - 1;

        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return ((((long) high)) << 32) | (((long) low) & mask32);
        }

        public static int highBit(long x) {
            return (int) (x >> 32);
        }

        public static int lowBit(long x) {
            return (int) x;
        }

    }
}

