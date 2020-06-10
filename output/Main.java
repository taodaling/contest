import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
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
            DExpectedDiameterOfATree solver = new DExpectedDiameterOfATree();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DExpectedDiameterOfATree {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int q = in.readInt();

            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }

            for (int i = 0; i < m; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                a.adj.add(b);
                b.adj.add(a);
            }

            for (Node node : nodes) {
                if (node.top == null) {
                    dfsForDiameter(node, null, 0, node);
                    node.depthCnt = new int[node.size];
                    node.suffix = new int[node.size];
                    node.suffixSum = new long[node.size];
                    findDepth(node, null, 0);
                    for (int i = node.size - 1; i >= 0; i--) {
                        node.suffix[i] = node.depthCnt[i];
                        node.suffixSum[i] = (long) node.depthCnt[i] * i;
                        if (i + 1 < node.size) {
                            node.suffix[i] += node.suffix[i + 1];
                            node.suffixSum[i] += node.suffixSum[i + 1];
                        }
                    }
                }
            }

            Map<Long, Double> cache = new HashMap<>(q);
            for (int i = 0; i < q; i++) {
                Node a = nodes[in.readInt() - 1].top;
                Node b = nodes[in.readInt() - 1].top;
                if (a == b) {
                    out.println(-1);
                    continue;
                }
                if (a.size > b.size || (a.size == b.size && a.id > b.id)) {
                    Node tmp = a;
                    a = b;
                    b = tmp;
                }
                long key = DigitUtils.asLong(a.id, b.id);
                if (cache.containsKey(key)) {
                    out.println(cache.get(key));
                    continue;
                }
                long total = (long) a.size * b.size;
                long way = 0;
                long sum = 0;
                int diameter = Math.max(a.diameter, b.diameter);
                for (int j = 0; j < a.size; j++) {
                    int k = Math.max(0, diameter + 1 - 1 - j);
                    if (k >= b.size) {
                        continue;
                    }
                    way += b.suffix[k] * a.depthCnt[j];
                    sum += a.depthCnt[j] * (b.suffixSum[k] + b.suffix[k] * (long) (j + 1));
                }

                sum += (total - way) * diameter;
                double ans = (double) sum / total;
                out.println(ans);
                cache.put(key, ans);
            }
        }

        public void dfsForDiameter(Node root, Node p, int depth, Node top) {
            root.depth = depth;
            root.farthest = root.depth;
            root.top = top;
            root.size = 1;
            for (Node node : root.adj) {
                if (node == p) {
                    continue;
                }
                dfsForDiameter(node, root, depth + 1, top);
                root.size += node.size;
                root.diameter = Math.max(root.diameter, node.diameter);
                root.diameter = Math.max(root.diameter, root.farthest + node.farthest - 2 * root.depth);
                root.farthest = Math.max(root.farthest, node.farthest);
            }
        }

        public void findDepth(Node root, Node p, int height) {
            root.height = Math.max(height, root.farthest - root.depth);
            root.top.depthCnt[root.height]++;

            int[] max = new int[2];
            Arrays.fill(max, root.depth);
            for (Node node : root.adj) {
                if (node == p) {
                    continue;
                }
                if (max[0] >= node.farthest) {
                    continue;
                }
                max[0] = node.farthest;
                if (max[0] > max[1]) {
                    SequenceUtils.swap(max, 0, 1);
                }
            }
            for (Node node : root.adj) {
                if (node == p) {
                    continue;
                }
                int h = height;
                int cand = max[1] == node.farthest ? max[0] : max[1];
                h = Math.max(h, cand - root.depth) + 1;
                findDepth(node, root, h);
            }
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

        public FastOutput append(double c) {
            cache.append(new BigDecimal(c).toPlainString());
            return this;
        }

        public FastOutput append(Object c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
        }

        public FastOutput println(double c) {
            return append(c).println();
        }

        public FastOutput println(Object c) {
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

    static class DigitUtils {
        private static long LONG_TO_INT_MASK = (1L << 32) - 1;

        private DigitUtils() {
        }

        public static long asLong(int high, int low) {
            return (((long) high) << 32) | (((long) low) & LONG_TO_INT_MASK);
        }

    }

    static class Node {
        List<Node> adj = new ArrayList<>();
        Node top;
        int depth;
        int id;
        int height;
        int size;
        int[] depthCnt;
        int[] suffix;
        long[] suffixSum;
        int diameter;
        int farthest;

    }

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }
}

