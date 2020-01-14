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
            DYouAreGivenATree solver = new DYouAreGivenATree();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DYouAreGivenATree {
        Node[] nodes;
        int count = 0;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
            }
            for (int i = 1; i < n; i++) {
                Node a = nodes[in.readInt()];
                Node b = nodes[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }

            dfs(nodes[1], null);

            int sqrt = (int) Math.ceil(Math.sqrt(n));
            int[] ans = new int[n + 1];
            for (int i = 1; i < sqrt; i++) {
                out.println(solve(i));
            }

            int r = sqrt - 1;
            int l;
            while (r < n) {
                int since = r + 1;
                l = since;
                int val = solve(l);
                r = n;
                while (l < r) {
                    int mid = (l + r + 1) >> 1;
                    if (solve(mid) == val) {
                        l = mid;
                    } else {
                        r = mid - 1;
                    }
                }
                for (int i = since; i <= r; i++) {
                    out.println(val);
                }
            }
        }

        public int solve(int k) {
            prepare();
            dp(nodes[1], k);
            return count;
        }

        public void dfs(Node root, Node p) {
            root.next.remove(p);
            root.size = 1;
            for (Node node : root.next) {
                dfs(node, root);
                root.size += node.size;
            }
        }

        private void prepare() {
            count = 0;
        }

        public int dp(Node root, int k) {
            int max1 = 0;
            int max2 = 0;

            for (Node node : root.next) {
                int ans = dp(node, k);
                if (ans > max2) {
                    max2 = ans;
                }
                if (max2 > max1) {
                    int tmp = max1;
                    max1 = max2;
                    max2 = tmp;
                }
            }

            if (max1 + max2 + 1 >= k) {
                count++;
                return 0;
            }
            return max1 + 1;
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

    static class Node {
        List<Node> next = new ArrayList<>();
        int size;

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
}

