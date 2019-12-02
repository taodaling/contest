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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            TaskE solver = new TaskE();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TaskE {
        int[] choose2;
        Modular mod = new Modular(1e9 + 7);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            Node[] nodes = new Node[n + 1];
            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node();
                nodes[i].id = i;
            }
            for (int i = 1; i < n; i++) {
                Node a = nodes[in.readInt()];
                Node b = nodes[in.readInt()];
                a.next.add(b);
                b.next.add(a);
            }

            choose2 = new int[10000];
            choose2[0] = 1;
            Factorial fact = new Factorial(10000, mod);
            Composite comp = new Composite(fact);
            for (int i = 2; i < 10000; i += 2) {
                choose2[i] = mod.mul(choose2[i - 2], comp.composite(i, 2));
            }
            for (int i = 0; i < 10000; i += 2) {
                choose2[i] = mod.mul(choose2[i], fact.invFact(i / 2));
            }

            dfsForSize(nodes[1], null);
            dfs(nodes[1], null);
            int[][] dp = nodes[1].dp;

            int ans = 0;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j <= n; j++) {
                    int local = mod.mul(dp[i][j], choose2[j]);
                    if (i % 2 == 1) {
                        local = mod.valueOf(-local);
                    }
                    ans = mod.plus(ans, local);
                }
            }

            out.println(ans);
        }

        public void dfsForSize(Node root, Node p) {
            root.next.remove(p);
            root.size = 1;
            for (Node node : root.next) {
                dfsForSize(node, root);
                root.size += node.size;
                if (root.heavy == null || root.heavy.size < node.size) {
                    root.heavy = node;
                }
            }
        }

        public void dfs(Node root, int[][] dp) {
            if (dp == null) {
                dp = new int[2][root.size + 1];
            }
            root.dp = dp;
            if (root.heavy == null) {
                root.dp[0][1] = 1;
                return;
            }

            dfs(root.heavy, dp);
            int[][] last = dp;
            int[][] next = new int[2][root.size + 1];
            for (int i = 0; i < 2; i++) {
                for (int j = 1; j <= root.heavy.size; j++) {
                    //reserve
                    next[i][j + 1] = mod.plus(next[i][j + 1], last[i][j]);
                    //cut
                    next[1 - i][1] = mod.valueOf(next[1 - i][1] + (long) choose2[j] * last[i][j]);
                }
            }

            {
                int[][] tmp = last;
                last = next;
                next = tmp;
            }

            int size = root.heavy.size + 1;
            for (Node node : root.next) {
                if (root.heavy == node) {
                    continue;
                }
                dfs(node, null);
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j <= size; j++) {
                        next[i][j] = 0;
                    }
                }
                for (int i = 0; i < 2; i++) {
                    for (int j = 1; j <= size; j++) {
                        for (int k = 1; k <= node.size; k++) {
                            //reserve
                            next[i][j + k] = mod.valueOf(next[i][j + k] + (long) last[i][j] * node.dp[0][k]);
                            next[i][j + k] = mod.valueOf(next[i][j + k] + (long) last[1 - i][j] * node.dp[1][k]);

                            //cut
                            next[i][j] = mod.valueOf(next[i][j] + (long) last[i][j] * mod.mul(node.dp[1][k], choose2[k]));
                            next[i][j] = mod.valueOf(next[i][j] + (long) last[1 - i][j] * mod.mul(node.dp[0][k], choose2[k]));
                        }
                    }
                }

                size += node.size;
                {
                    int[][] tmp = last;
                    last = next;
                    next = tmp;
                }
            }

            if (last != dp) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j <= root.size; j++) {
                        dp[i][j] = last[i][j];
                    }
                }
            }
        }

    }

    static class InverseNumber {
        int[] inv;

        public InverseNumber(int[] inv, int limit, Modular modular) {
            this.inv = inv;
            inv[1] = 1;
            int p = modular.getMod();
            for (int i = 2; i <= limit; i++) {
                int k = p / i;
                int r = p % i;
                inv[i] = modular.mul(-k, inv[r]);
            }
        }

        public InverseNumber(int limit, Modular modular) {
            this(new int[limit + 1], limit, modular);
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

    static class Factorial {
        int[] fact;
        int[] inv;
        Modular modular;

        public Modular getModular() {
            return modular;
        }

        public Factorial(int[] fact, int[] inv, InverseNumber in, int limit, Modular modular) {
            this.modular = modular;
            this.fact = fact;
            this.inv = inv;
            fact[0] = inv[0] = 1;
            for (int i = 1; i <= limit; i++) {
                fact[i] = modular.mul(fact[i - 1], i);
                inv[i] = modular.mul(inv[i - 1], in.inv[i]);
            }
        }

        public Factorial(int limit, Modular modular) {
            this(new int[limit + 1], new int[limit + 1], new InverseNumber(limit, modular), limit, modular);
        }

        public int fact(int n) {
            return fact[n];
        }

        public int invFact(int n) {
            return inv[n];
        }

    }

    static class Node {
        List<Node> next = new ArrayList<>();
        int[][] dp;
        int size;
        int id;
        Node heavy;

        public String toString() {
            return "" + id;
        }

    }

    static class Composite {
        final Factorial factorial;
        final Modular modular;

        public Composite(Factorial factorial) {
            this.factorial = factorial;
            this.modular = factorial.getModular();
        }

        public Composite(int limit, Modular modular) {
            this(new Factorial(limit, modular));
        }

        public int composite(int m, int n) {
            if (n > m) {
                return 0;
            }
            return modular.mul(modular.mul(factorial.fact(m), factorial.invFact(n)), factorial.invFact(m - n));
        }

    }

    static class Modular {
        int m;

        public int getMod() {
            return m;
        }

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

        public String toString() {
            return "mod " + m;
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

