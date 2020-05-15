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
            DRedBlackCobweb solver = new DRedBlackCobweb();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DRedBlackCobweb {
        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        int fix = 250000;
        Debug debug = new Debug(true);
        IntegerBIT add = new IntegerBIT(fix * 2);
        IntegerBIT sub = new IntegerBIT(fix * 2);
        int ans = 1;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 0; i < n - 1; i++) {
                Node u = nodes[in.readInt() - 1];
                Node v = nodes[in.readInt() - 1];
                int x = in.readInt();
                int c = in.readInt();

                Edge edge = new Edge();
                edge.a = u;
                edge.b = v;
                edge.w = x;
                if (c == 0) {
                    edge.c0 = 1;
                } else {
                    edge.c1 = 1;
                }

                u.adj.add(edge);
                v.adj.add(edge);
            }

            dac(nodes[0]);
            out.println(ans);
        }

        public void dfsForSize(Node root, Node p) {
            root.size = 1;
            for (Edge e : root.adj) {
                Node node = e.other(root);
                if (node == p) {
                    continue;
                }
                dfsForSize(node, root);
                root.size += node.size;
            }
        }

        public Node dfsForCentroid(Node root, Node p, int total) {
            int max = total - root.size;
            for (Edge e : root.adj) {
                Node node = e.other(root);
                if (node == p) {
                    continue;
                }
                Node ans = dfsForCentroid(node, root, total);
                if (ans != null) {
                    return ans;
                }
                max = Math.max(max, node.size);
            }

            if (max * 2 <= total) {
                return root;
            }
            return null;
        }

        public void dfsForAns(Node root, Node p, int a, int b, int prod) {
            int exp = mod.subtract(add.query(2 * a - b + fix), sub.query(a - 2 * b - 1 + fix));
            ans = mod.mul(ans, power.pow(prod, exp));
            for (Edge e : root.adj) {
                Node node = e.other(root);
                if (node == p) {
                    continue;
                }
                dfsForAns(node, root, a + e.c0, b + e.c1, mod.mul(prod, e.w));
            }
        }

        public void dfsForModify(Node root, Node p, int a, int b, int x) {
            add.update(b - 2 * a + fix, x);
            sub.update(b * 2 - a + fix, x);
            for (Edge e : root.adj) {
                Node node = e.other(root);
                if (node == p) {
                    continue;
                }
                dfsForModify(node, root, a + e.c0, b + e.c1, x);
            }
        }

        public void dac(Node root) {
            dfsForSize(root, null);
            root = dfsForCentroid(root, null, root.size);
            debug.debug("root", root);
            add.update(0 + fix, 1);
            sub.update(0 + fix, 1);

            for (int i = 0; i < 2; i++) {
                for (Edge e : root.adj) {
                    Node node = e.other(root);
                    dfsForAns(node, root, e.c0, e.c1, e.w);
                    dfsForModify(node, root, e.c0, e.c1, 1);
                }
                for (Edge e : root.adj) {
                    Node node = e.other(root);
                    dfsForModify(node, root, e.c0, e.c1, -1);
                }
                if (i == 0) {
                    SequenceUtils.reverse(root.adj);
                    add.update(0 + fix, -1);
                    sub.update(0 + fix, -1);
                }
            }
            for (Edge e : root.adj) {
                Node node = e.other(root);
                node.adj.remove(e);
                dac(node);
            }
        }

    }

    static class SequenceUtils {
        public static <T> void swap(List<T> data, int i, int j) {
            T tmp = data.get(i);
            data.set(i, data.get(j));
            data.set(j, tmp);
        }

        public static <T> void reverse(List<T> data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

        public static <T> void reverse(List<T> data) {
            reverse(data, 0, data.size() - 1);
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

        public int subtract(int x, int y) {
            return valueOf(x - y);
        }

        public String toString() {
            return "mod " + m;
        }

    }

    static class IntegerBIT {
        private int[] data;
        private int n;

        public IntegerBIT(int n) {
            this.n = n;
            data = new int[n + 1];
        }

        public int query(int i) {
            int sum = 0;
            for (; i > 0; i -= i & -i) {
                sum += data[i];
            }
            return sum;
        }

        public void update(int i, int mod) {
            if (i <= 0) {
                return;
            }
            for (; i <= n; i += i & -i) {
                data[i] += mod;
            }
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                builder.append(query(i) - query(i - 1)).append(' ');
            }
            return builder.toString();
        }

    }

    static class Power {
        final Modular modular;

        public Power(Modular modular) {
            this.modular = modular;
        }

        public int pow(int x, int n) {
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
        List<Edge> adj = new ArrayList<>();
        int id;
        int size;

        public String toString() {
            return "" + id;
        }

    }

    static class Edge {
        Node a;
        Node b;
        int c0;
        int c1;
        int w;

        Node other(Node x) {
            return a == x ? b : a;
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
}

