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
            CJohnnySolving solver = new CJohnnySolving();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CJohnnySolving {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int k = in.readInt();

            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 0; i < m; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                a.next.add(b);
                b.next.add(a);
            }

            dfs(nodes[0], null, 0);
            Node maxDepth = null;
            for (Node node : nodes) {
                if (maxDepth == null || maxDepth.depth < node.depth) {
                    maxDepth = node;
                }
            }

            List<Node> trace = new ArrayList<>(n);
            if (maxDepth.depth >= DigitUtils.ceilDiv(n, k)) {
                up(maxDepth, nodes[0], trace);
                out.println("PATH");
                out.println(trace.size());
                for (Node node : trace) {
                    out.append(node.id + 1).append(' ');
                }
                return;
            }

            out.println("CYCLES");
            int cnt = 0;
            for (int i = 0; i < n; i++) {
                if (!nodes[i].leaf) {
                    continue;
                }
                cnt++;
                if (cnt > k) {
                    continue;
                }
                Node a = nodes[i].next.get(0);
                Node b = nodes[i].next.get(1);
                trace.clear();
                if ((nodes[i].depth - a.depth + 1) % 3 != 0) {
                    up(nodes[i], a, trace);
                } else if ((nodes[i].depth - b.depth + 1) % 3 != 0) {
                    up(nodes[i], b, trace);
                } else {
                    trace.add(nodes[i]);
                    if (a.depth < b.depth) {
                        Node tmp = a;
                        a = b;
                        b = tmp;
                    }
                    up(a, b, trace);
                }
                out.println(trace.size());
                for (Node node : trace) {
                    out.append(node.id + 1).append(' ');
                }
                out.println();
            }
        }

        public void up(Node root, Node target, List<Node> trace) {
            trace.add(root);
            if (target != root) {
                up(root.p, target, trace);
            }
        }

        public void dfs(Node root, Node p, int d) {
            root.depth = d;
            root.p = p;
            root.next.remove(p);

            for (Node node : root.next) {
                if (node.depth == -1) {
                    dfs(node, root, d + 1);
                    root.leaf = false;
                }
            }
        }

    }

    static class Node {
        Node p;
        List<Node> next = new ArrayList<>();
        boolean leaf = true;
        int depth = -1;
        int id;

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

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
            return append(c).println();
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

    static class DigitUtils {
        private DigitUtils() {
        }

        public static int floorDiv(int a, int b) {
            return a < 0 ? -ceilDiv(-a, b) : a / b;
        }

        public static int ceilDiv(int a, int b) {
            if (a < 0) {
                return -floorDiv(-a, b);
            }
            int c = a / b;
            if (c * b < a) {
                return c + 1;
            }
            return c;
        }

    }
}

