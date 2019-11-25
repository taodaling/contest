import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.function.BiFunction;
import java.io.IOException;
import java.util.Deque;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
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
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskE {
        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 0; i <= n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 1; i <= n; i++) {
                Node p = nodes[in.readInt()];
                p.next.add(nodes[i]);
            }
            MultiWayDeque<Node> levels = new MultiWayDeque<>(n + 1, n + 1);
            MultiWayDeque<Node> virtualEdges = new MultiWayDeque<>(n + 1, n + 1);
            List<Node> trace = new ArrayList<>(2 * (n + 1));
            dfs(nodes[0], levels, 0, trace);
            SparseTable<Node> st = new SparseTable<>(trace.toArray(), trace.size(), (a, b) -> a.dfn <= b.dfn ? a : b);
            Deque<Node> deque = new ArrayDeque<>(n + 1);
            IntVersionArray va = new IntVersionArray(n + 1);

            int ans = 0;
            for (int i = 0; i <= n; i++) {
                trace.clear();
                va.clear();
                for (Iterator<Node> iterator = levels.iterator(i); iterator.hasNext(); ) {
                    Node node = iterator.next();
                    va.set(node.id, 1);
                    trace.add(node);
                }
                int cnt = trace.size();
                if (cnt == 0) {
                    break;
                }
                for (int j = 1, until = trace.size(); j < until; j++) {
                    Node prev = trace.get(j - 1);
                    Node cur = trace.get(j);
                    Node lca = st.query(prev.dfn, cur.dfn);
                    if (va.get(lca.id) == 1) {
                        continue;
                    }
                    va.set(lca.id, 1);
                    trace.add(lca);
                }
                trace.sort((a, b) -> a.dfn - b.dfn);
                for (int j = 0, until = trace.size(); j < until; j++) {
                    trace.get(j).virtualId = j;
                }
                deque.clear();
                virtualEdges.expandQueueNum(trace.size());
                virtualEdges.clear();
                for (Node node : trace) {
                    while (!deque.isEmpty() && st.query(deque.peekLast().dfn, node.dfn) != deque.peekLast()) {
                        deque.removeLast();
                    }
                    if (!deque.isEmpty()) {
                        virtualEdges.addLast(deque.peekLast().virtualId, node);
                    }
                    deque.addLast(node);
                }

                Node root = trace.get(0);
                dpOnTree(root, virtualEdges);
                int way = root.dp[1];
                way = mod.mul(way, pow.pow(2, n + 1 - cnt));
                ans = mod.plus(ans, way);
            }
            out.println(ans);
        }

        public void dpOnTree(Node root, MultiWayDeque<Node> edges) {
            if (edges.isEmpty(root.virtualId)) {
                root.dp[0] = root.dp[1] = 1;
                return;
            }
            root.dp[0] = 1;
            root.dp[1] = 0;
            int total = 1;
            for (Iterator<Node> iterator = edges.iterator(root.virtualId); iterator.hasNext(); ) {
                Node node = iterator.next();
                dpOnTree(node, edges);
                total = mod.mul(total, mod.plus(node.dp[0], node.dp[1]));
                root.dp[1] = mod.plus(mod.mul(root.dp[1], node.dp[0]), mod.mul(root.dp[0], node.dp[1]));
                root.dp[0] = mod.mul(root.dp[0], node.dp[0]);
            }
            root.dp[0] = mod.subtract(total, root.dp[1]);
        }

        public void dfs(Node root, MultiWayDeque<Node> deque, int height, List<Node> trace) {
            deque.addLast(height, root);
            root.dfn = trace.size();
            trace.add(root);
            for (Node node : root.next) {
                dfs(node, deque, height + 1, trace);
                trace.add(root);
            }
        }

    }

    static class MultiWayDeque<V> {
        private Object[] values;
        private int[] next;
        private int[] prev;
        private int[] heads;
        private int[] tails;
        private int alloc;
        private int queueNum;

        public RevokeIterator<V> iterator(final int queue) {
            return new RevokeIterator() {
                int ele = heads[queue];


                public boolean hasNext() {
                    return ele != 0;
                }


                public Object next() {
                    Object ans = values[ele];
                    ele = next[ele];
                    return ans;
                }


                public void revoke() {
                    ele = prev[ele];
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

        public void expandQueueNum(int qNum) {
            if (qNum <= queueNum) {
            } else if (qNum <= heads.length) {
                Arrays.fill(heads, queueNum, qNum, 0);
                Arrays.fill(tails, queueNum, qNum, 0);
            } else {
                Arrays.fill(heads, queueNum, heads.length, 0);
                Arrays.fill(tails, queueNum, heads.length, 0);
                heads = Arrays.copyOf(heads, qNum);
                tails = Arrays.copyOf(tails, qNum);
            }
            queueNum = qNum;
        }

        public MultiWayDeque(int qNum, int totalCapacity) {
            values = new Object[totalCapacity + 1];
            next = new int[totalCapacity + 1];
            prev = new int[totalCapacity + 1];
            heads = new int[qNum];
            tails = new int[qNum];
            queueNum = qNum;
        }

        public void addLast(int qId, V x) {
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
                builder.append(i).append(": ");
                for (Iterator<V> iterator = iterator(i); iterator.hasNext(); ) {
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

    static class Power {
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int pow(int x, long n) {
            if (n == 0) {
                return modular.valueOf(1);
            }
            long r = pow(x, n >> 1);
            r = modular.valueOf(r * r);
            if ((n & 1) == 1) {
                r = modular.valueOf(r * x);
            }
            return (int) r;
        }

    }

    static class Node {
        List<Node> next = new ArrayList<>();
        int virtualId;
        int[] dp = new int[2];
        int dfn;
        int id;

        public String toString() {
            return "" + id;
        }

    }

    static class Log2 {
        public int floorLog(int x) {
            return 31 - Integer.numberOfLeadingZeros(x);
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

    }

    static class IntVersionArray {
        int[] data;
        int[] version;
        int now;
        int def;

        public IntVersionArray(int cap) {
            this(cap, 0);
        }

        public IntVersionArray(int cap, int def) {
            data = new int[cap];
            version = new int[cap];
            now = 0;
            this.def = def;
        }

        public void clear() {
            now++;
        }

        public void visit(int i) {
            if (version[i] < now) {
                version[i] = now;
                data[i] = def;
            }
        }

        public void set(int i, int v) {
            version[i] = now;
            data[i] = v;
        }

        public int get(int i) {
            visit(i);
            return data[i];
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

        public FastOutput println(int c) {
            cache.append(c).append('\n');
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

    static interface RevokeIterator<E> extends Iterator<E> {
    }

    static class SparseTable<T> {
        private Object[][] st;
        private BiFunction<T, T, T> merger;
        private CachedLog2 log2;

        public SparseTable(Object[] data, int length, BiFunction<T, T, T> merger) {
            log2 = new CachedLog2(length);
            int m = log2.floorLog(length);

            st = new Object[m + 1][length];
            this.merger = merger;
            for (int i = 0; i < length; i++) {
                st[0][i] = data[i];
            }
            for (int i = 0; i < m; i++) {
                int interval = 1 << i;
                for (int j = 0; j < length; j++) {
                    if (j + interval < length) {
                        st[i + 1][j] = merge((T) st[i][j], (T) st[i][j + interval]);
                    } else {
                        st[i + 1][j] = st[i][j];
                    }
                }
            }
        }

        private T merge(T a, T b) {
            return merger.apply(a, b);
        }

        public T query(int left, int right) {
            int queryLen = right - left + 1;
            int bit = log2.floorLog(queryLen);
            // x + 2^bit == right + 1
            // So x should be right + 1 - 2^bit - left=queryLen - 2^bit
            return merge((T) st[bit][left], (T) st[bit][right + 1 - (1 << bit)]);
        }

        public String toString() {
            return Arrays.toString(st[0]);
        }

    }

    static class Modular {
        int m;

        public Modular(int m) {
            this.m = m;
        }

        public Modular(long m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public Modular(double m) {
            this.m = (int) m;
            if (this.m != m) {
                throw new IllegalArgumentException();
            }
        }

        public int valueOf(int x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return x;
        }

        public int valueOf(long x) {
            x %= m;
            if (x < 0) {
                x += m;
            }
            return (int) x;
        }

        public int mul(int x, int y) {
            return valueOf((long) x * y);
        }

        public int plus(int x, int y) {
            return valueOf(x + y);
        }

        public int subtract(int x, int y) {
            return valueOf(x - y);
        }

        public String toString() {
            return "mod " + m;
        }

    }

    static class CachedLog2 {
        private int[] cache;
        private Log2 log2;

        public CachedLog2(int n) {
            cache = new int[n + 1];
            int b = 0;
            for (int i = 0; i <= n; i++) {
                while ((1 << (b + 1)) <= i) {
                    b++;
                }
                cache[i] = b;
            }
        }

        public int floorLog(int x) {
            if (x >= cache.length) {
                return log2.floorLog(x);
            }
            return cache[x];
        }

    }
}

