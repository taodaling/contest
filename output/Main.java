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
 * Built using CHelper plug-in Actual solution is at the top
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
            TaskD solver = new TaskD();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class TaskD {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }

            for (int i = 1; i < n; i++) {
                Edge e = new Edge();
                e.a = nodes[in.readInt()];
                e.b = nodes[in.readInt()];
                e.length = in.readInt();
                e.a.next.add(e);
                e.b.next.add(e);
            }
            dfs(nodes[1], null);
            List<Node> gs = new ArrayList<>();
            findGravity(nodes[1], null, n, gs);

            long ans = 0;
            for (Node g : gs) {
                dfsForDepth(g, null, 0);

                long totalDepth = 0;
                long minDepth = (long) 1e18;
                for (int i = 1; i <= n; i++) {
                    if (nodes[i].depth == 0) {
                        continue;
                    }
                    totalDepth += nodes[i].depth;
                    minDepth = Math.min(minDepth, nodes[i].depth);
                }
                for (Node node : gs) {
                    if (g == node) {
                        continue;
                    }
                    minDepth = node.depth;
                }
                ans = Math.max(ans, totalDepth * 2 - minDepth);
            }

            out.println(ans);
        }

        public void dfsForDepth(Node root, Edge p, long depth) {
            root.depth = depth;
            for (Edge e : root.next) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                dfsForDepth(node, e, depth + e.length);
            }
        }

        public void findGravity(Node root, Edge p, int total, List<Node> list) {
            int maxSize = 0;
            if (root.p != null) {
                maxSize = Math.max(maxSize, total - root.size);
            }
            for (Edge e : root.next) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                maxSize = Math.max(maxSize, node.size);
            }
            if (maxSize <= total / 2) {
                list.add(root);
            }
            for (Edge e : root.next) {
                if (e == p) {
                    continue;
                }
                Node node = e.other(root);
                findGravity(node, e, total, list);
            }
        }

        public void dfs(Node root, Edge p) {
            if (p != null) {
                root.p = p.other(root);
            }
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
    static class Edge {
        Node a;
        Node b;
        long length;

        public Node other(Node x) {
            return a == x ? b : a;
        }

    }
    static class Node {
        List<Edge> next = new ArrayList<>();
        int size;
        Node p;
        long depth;
        int id;

        public String toString() {
            return "" + id;
        }

    }
}

