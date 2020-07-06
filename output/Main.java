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
            DIsomorphismFreak solver = new DIsomorphismFreak();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DIsomorphismFreak {
        int[] seq;
        int depthest;
        int minDepth;
        long minLeaf;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            int[][] edges = new int[n - 1][2];
            for (int i = 0; i < n - 1; i++) {
                edges[i][0] = in.readInt() - 1;
                edges[i][1] = in.readInt() - 1;
                Node a = nodes[edges[i][0]];
                Node b = nodes[edges[i][1]];
                a.adj.add(b);
                b.adj.add(a);
            }
            seq = new int[n];


            minDepth = n;
            minLeaf = Long.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                reset();
                dfs(nodes[i], null, 0);
                update(1);
            }

            for (int[] e : edges) {
                Node a = nodes[e[0]];
                Node b = nodes[e[1]];
                reset();
                dfs(a, b, 0);
                dfs(b, a, 0);
                update(2);
            }

            out.println(minDepth + 1).println(minLeaf);
        }

        public void update(int mul) {
            int depth = depthest;
            long leaf = mul;
            for (int j = 0; j <= depth; j++) {
                leaf *= seq[j];
            }

            if (depth < minDepth) {
                minDepth = depth;
                minLeaf = Long.MAX_VALUE;
            }
            if (depth == minDepth) {
                minLeaf = Math.min(minLeaf, leaf);
            }
        }

        public void reset() {
            Arrays.fill(seq, 1);
            depthest = 0;
        }

        public void dfs(Node root, Node p, int d) {
            depthest = Math.max(depthest, d);
            int degree = root.adj.size() - (p == null ? 0 : 1);
            seq[d] = Math.max(seq[d], degree);
            for (Node node : root.adj) {
                if (node == p) {
                    continue;
                }
                dfs(node, root, d + 1);
            }
        }

    }

    static class Node {
        List<Node> adj = new ArrayList<>();
        int id;

        public String toString() {
            return "" + (id + 1);
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

        public FastOutput append(long c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
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
}

