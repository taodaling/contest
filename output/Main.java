import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
            FIndependentSet solver = new FIndependentSet();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FIndependentSet {
        Modular mod = new Modular(998244353);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 0; i < n - 1; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                a.next.add(b);
                b.next.add(a);
            }

            dfs(nodes[0], null);
            int ans = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if (i < j) {
                        continue;
                    }
                    ans = mod.plus(ans, nodes[0].dp[i][j]);
                }
            }
            ans = mod.subtract(ans, 1);
            out.println(ans);
        }

        public void dfs(Node root, Node p) {
            for (int j = 0; j < 2; j++) {
                root.dp[0][j] = 1;
            }
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                dfs(node, root);
                {
                    //1 0
                    int a = mod.plus(node.dp[0][0], mod.plus(node.dp[0][1], mod.plus(node.dp[1][0], node.dp[1][1])));
                    int b = mod.plus(node.dp[0][0], mod.plus(node.dp[1][0], node.dp[1][1]));
                    root.dp[1][0] = mod.plus(mod.mul(root.dp[1][0], mod.plus(a, b)), mod.mul(a, root.dp[0][0]));
                }
                {
                    //0 0
                    int a = mod.plus(node.dp[1][1], mod.plus(node.dp[0][0], node.dp[1][0]));
                    root.dp[0][0] = mod.mul(root.dp[0][0], a);
                }
                {
                    //1 1
                    int a = mod.plus(node.dp[0][0], node.dp[1][0]);
                    int b = mod.plus(node.dp[0][0], mod.plus(node.dp[1][0], node.dp[1][1]));
                    root.dp[1][1] = mod.plus(mod.mul(root.dp[1][1], mod.plus(a, b)), mod.mul(a, root.dp[0][1]));
                }
                {
                    //0 1
                    int a = mod.plus(node.dp[1][1], mod.plus(node.dp[0][0], node.dp[1][0]));
                    root.dp[0][1] = mod.mul(root.dp[0][1], a);
                }

            }
        }

    }

    static class Node {
        int[][] dp = new int[2][2];
        List<Node> next = new ArrayList<>();
        int id;

        public String toString() {
            return "" + id;
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
}

