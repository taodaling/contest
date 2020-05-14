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
            EInATrap solver = new EInATrap();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class EInATrap {
        int k = 8;
        int height = 15;
        BNode btree = BNode.build(height);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int q = in.readInt();

            Node[] nodes = new Node[n];
            for (int i = 0; i < n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
                nodes[i].a = in.readInt();
            }

            for (int i = 0; i < n - 1; i++) {
                Node a = nodes[in.readInt() - 1];
                Node b = nodes[in.readInt() - 1];
                a.next.add(b);
                b.next.add(a);
            }

            dfs(nodes[0], null, 0);
            for (int i = 0; i < n; i++) {
                prepare(nodes[i]);
            }

            for (int i = 0; i < q; i++) {
                Node u = nodes[in.readInt() - 1];
                Node v = nodes[in.readInt() - 1];
                int ans = solve(v, 0, 0, u.depth);
                out.println(ans);
            }
        }

        public int solve(Node root, int step, int dist, int targetHeight) {
            if (root == null || root.depth < targetHeight) {
                return 0;
            }

            int ans;
            if (root.depth - (1 << k) + 1 >= targetHeight) {
                ans = Math.max(root.dp[step], solve(root.prev, step + 1, dist + (1 << k), targetHeight));
            } else {
                ans = 0;
                while (root != null && root.depth >= targetHeight) {
                    ans = Math.max(root.a ^ dist, ans);
                    dist++;
                    root = root.parent;
                }
            }

            return ans;
        }

        public void dfs(Node root, Node p, int depth) {
            root.depth = depth;
            root.parent = p;
            for (Node node : root.next) {
                if (node == p) {
                    continue;
                }
                dfs(node, root, depth + 1);
            }
        }

        public void up(Node root, int dist, int x) {
            if (root == null || dist >= (1 << k)) {
                return;
            }
            btree.add(height, dist ^ root.a, x);
            up(root.parent, dist + 1, x);
        }

        public Node findPrev(Node root, int dist) {
            if (root == null || dist >= (1 << k)) {
                return root;
            }
            return findPrev(root.parent, dist + 1);
        }

        public void prepare(Node root) {
            root.prev = findPrev(root, 0);
            up(root, 0, 1);
            for (int i = 0; i < root.dp.length; i++) {
                root.dp[i] = btree.find(height, i << k, 0);
            }
            up(root, 0, -1);
        }

    }

    static class Bits {
        private Bits() {
        }

        public static int bitAt(int x, int i) {
            return (x >>> i) & 1;
        }

    }

    static class Node {
        int depth;
        List<Node> next = new ArrayList<>();
        int[] dp = new int[256];
        int id;
        int a;
        Node parent;
        Node prev;

        public String toString() {
            return "" + id;
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

    static class BNode {
        BNode[] next = new BNode[2];
        int cnt;

        static BNode build(int bit) {
            if (bit < 0) {
                return new BNode();
            }
            BNode ans = new BNode();
            ans.next[0] = build(bit - 1);
            ans.next[1] = build(bit - 1);
            return ans;
        }

        public void add(int bit, int val, int x) {
            cnt += x;
            if (bit < 0) {
                return;
            }
            next[Bits.bitAt(val, bit)].add(bit - 1, val, x);
        }

        public int find(int bit, int xor, int built) {
            if (cnt == 0) {
                return -1;
            }
            if (bit < 0) {
                return built;
            }
            int val = Bits.bitAt(xor, bit);
            int ans = next[val ^ 1].find(bit - 1, xor, built | ((1 ^ val) << bit));
            if (ans == -1) {
                ans = next[val].find(bit - 1, xor, built | (val << bit));
            }
            return ans;
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

