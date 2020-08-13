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
            DBoboniuAndJianghu solver = new DBoboniuAndJianghu();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DBoboniuAndJianghu {
        long inf = (long) 1e18;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 0; i < n; i++) {
                nodes[i].t = in.readInt();
            }
            for (int i = 0; i < n; i++) {
                nodes[i].h = in.readInt();
            }
            for (int i = 0; i < n - 1; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                a.adj.add(b);
                b.adj.add(a);
            }

            long total = 0;
            Arrays.sort(nodes, (a, b) -> Integer.compare(a.h, b.h));
            for (int i = 0; i < n; i++) {
                int l = i;
                int r = i;
                while (r + 1 < n && nodes[r + 1].h == nodes[r].h) {
                    r++;
                }
                i = r;

                for (int j = l; j <= r; j++) {
                    if (nodes[j].visited) {
                        continue;
                    }
                    PseudoNode ps = build(nodes[j], null);
                    dp(ps);
                    long req = Math.min(ps.dp[1], ps.dp[2]);
                    total += req;
                }
            }

            out.println(total);
        }

        public long extraCost(PseudoNode root) {
            return root.dp[2] - (Math.min(root.dp[0], root.dp[1]) + root.cost);
        }

        public void contrib(PseudoNode root, int in, int out, long sum) {
            in += root.in;
            out += root.out;
            root.dp[0] = Math.min(root.dp[0], Math.max(in - 1, out) * root.cost + sum);
            root.dp[1] = Math.min(root.dp[1], Math.max(in, out) * root.cost + sum);
            if (in >= out) {
                root.dp[2] = Math.min(root.dp[2], (in + 1) * root.cost + sum);
            } else {
                root.dp[2] = Math.min(root.dp[2], out * root.cost + sum);
            }
        }

        public void dp(PseudoNode root) {
            long sum = 0;
            for (PseudoNode node : root.adj) {
                dp(node);
                sum += Math.min(node.dp[0], node.dp[1]) + node.cost;
            }
            int n = root.adj.size();
            root.adj.sort((a, b) -> Long.compare(extraCost(a), extraCost(b)));
            Arrays.fill(root.dp, inf);
            contrib(root, n, 0, sum);
            for (int i = 0; i < n; i++) {
                PseudoNode node = root.adj.get(i);
                sum += extraCost(node);
                int out = i + 1;
                int in = n - i - 1;
                contrib(root, in, out, sum);
            }
        }

        public PseudoNode build(Node root, Node p) {
            root.visited = true;
            PseudoNode ans = new PseudoNode(root.t);
            for (Node node : root.adj) {
                if (node == p) {
                    continue;
                }
                if (node.h != root.h) {
                    if (node.h < root.h) {
                        ans.out++;
                    } else {
                        ans.in++;
                    }
                } else {
                    ans.adj.add(build(node, root));
                }
            }

            return ans;
        }

    }

    static class Node {
        int id;
        int h;
        long t;
        List<Node> adj = new ArrayList<>();
        boolean visited;

        public String toString() {
            return "" + (id + 1);
        }

    }

    static class PseudoNode {
        long cost;
        long[] dp = new long[3];
        List<PseudoNode> adj = new ArrayList<>();
        int in;
        int out;

        public PseudoNode(long cost) {
            this.cost = cost;
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
}

