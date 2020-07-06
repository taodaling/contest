import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 29);
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
            P5903K solver = new P5903K();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P5903K {
        int s;
        long UI_MASK = (1L << 32) - 1;
        Debug debug = new Debug(true);

        long get(int x) {
            x ^= x << 13;
            x ^= x >>> 17;
            x ^= x << 5;
            return (s = x) & UI_MASK;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int q = in.readInt();
            s = (int) in.readLong();
            int[] father = new int[n];
            in.populate(father);
            int root = -1;
            List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
            for (int i = 0; i < n; i++) {
                father[i]--;
                if (father[i] == -1) {
                    root = i;
                } else {
                    Graph.addEdge(g, father[i], i);
                }
            }
            debug.elapse("build graph");

            long xor = 0;
            KthAncestorOnTree tree = new KthAncestorOnTree(g, root);
            debug.elapse("build tree");

            int ans = 0;
            for (int i = 0; i < q; i++) {
                int x = (int) ((get(s) ^ ans) % n);
                int depth = tree.getDepth(x) + 1;
                int k = (int) ((get(s) ^ ans) % depth);
                ans = tree.kthAncestor(x, k) + 1;
                xor ^= ans * (long) (i + 1);
//            debug.debug("x", x);
//            debug.debug("k", k);
//            debug.debug("ans", ans);
            }
            debug.elapse("process query");

            out.println(xor);
        }

    }

    static class Log2 {
        public static int floorLog(int x) {
            return 31 - Integer.numberOfLeadingZeros(x);
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

    static class DirectedEdge {
        public int to;

        public DirectedEdge(int to) {
            this.to = to;
        }

        public String toString() {
            return "->" + to;
        }

    }

    static class SequenceUtils {
        public static void deepFill(Object array, int val) {
            if (!array.getClass().isArray()) {
                throw new IllegalArgumentException();
            }
            if (array instanceof int[]) {
                int[] intArray = (int[]) array;
                Arrays.fill(intArray, val);
            } else {
                Object[] objArray = (Object[]) array;
                for (Object obj : objArray) {
                    deepFill(obj, val);
                }
            }
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        private long time = System.currentTimeMillis();

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug elapse(String name) {
            if (offline) {
                debug(name, System.currentTimeMillis() - time);
                time = System.currentTimeMillis();
            }
            return this;
        }

        public Debug debug(String name, long x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(long c) {
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

    static class KthAncestorOnTree {
        int[] depths;
        int[] longest;
        int[] longestChild;
        int[] top;
        int[] up;
        int[] down;
        int[] mem;
        int memIndicator;
        int[][] jump;
        List<? extends DirectedEdge>[] g;

        public int alloc(int n) {
            int ans = memIndicator;
            memIndicator += n;
            if (memIndicator > mem.length) {
                throw new ArrayIndexOutOfBoundsException();
            }
            return ans;
        }

        public KthAncestorOnTree(List<? extends DirectedEdge>[] g, int root) {
            this.g = g;
            int n = g.length;
            depths = new int[n];
            longest = new int[n];
            top = new int[n];
            up = new int[n];
            down = new int[n];
            mem = new int[n * 2];
            longestChild = new int[n];
            jump = new int[Log2.floorLog(n) + 2][n];
            SequenceUtils.deepFill(jump, -1);
            dfs(root, -1);
            dfsForUpAndDown(root, -1, false);
        }

        public int kthAncestor(int root, int k) {
            if (k == 0) {
                return root;
            }
            int targetDepth = depths[root] - k;
            int log = Log2.floorLog(k);
            root = top[jump[log][root]];
            if (targetDepth <= depths[root]) {
                return mem[up[root] + depths[root] - targetDepth];
            } else {
                return mem[down[root] + targetDepth - depths[root]];
            }
        }

        public int getDepth(int root) {
            return depths[root];
        }

        private void dfs(int root, int p) {
            depths[root] = p == -1 ? 0 : (depths[p] + 1);
            jump[0][root] = p;
            for (int i = 0; jump[i][root] != -1; i++) {
                jump[i + 1][root] = jump[i][jump[i][root]];
            }
            longestChild[root] = -1;
            for (DirectedEdge e : g[root]) {
                if (e.to == p) {
                    continue;
                }
                dfs(e.to, root);
                if (longest[root] < longest[e.to] + 1) {
                    longest[root] = longest[e.to] + 1;
                    longestChild[root] = e.to;
                }
            }
        }

        private void upRecord(int root, int i, int until) {
            if (root == -1 || i == until) {
                return;
            }
            mem[i] = root;
            upRecord(jump[0][root], i + 1, until);
        }

        private void downRecord(int root, int i, int until) {
            if (root == -1 || i == until) {
                return;
            }
            mem[i] = root;
            downRecord(longestChild[root], i + 1, until);
        }

        private void dfsForUpAndDown(int root, int p, boolean connect) {
            if (connect) {
                top[root] = top[p];
            } else {
                top[root] = root;
                int len = longest[root];
                up[root] = alloc(len + 1);
                down[root] = alloc(len + 1);
                upRecord(root, up[root], up[root] + len + 1);
                downRecord(root, down[root], down[root] + len + 1);
            }

            for (DirectedEdge e : g[root]) {
                if (e.to == p) {
                    continue;
                }
                dfsForUpAndDown(e.to, root, e.to == longestChild[root]);
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

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
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
}

