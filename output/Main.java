import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
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
            FEdgeColoringOfBipartiteGraph solver = new FEdgeColoringOfBipartiteGraph();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class FEdgeColoringOfBipartiteGraph {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int a = in.readInt();
            int b = in.readInt();
            int m = in.readInt();
            boolean[][] g = new boolean[a][b];
            int[][] colors = new int[a][b];
            int[][] edges = new int[2][m];
            for (int i = 0; i < m; i++) {
                int u = in.readInt() - 1;
                int v = in.readInt() - 1;
                g[u][v] = true;
                edges[0][i] = u;
                edges[1][i] = v;
            }

            BipartiteGraphEdgeColoring bgec = new BipartiteGraphEdgeColoring(a, b);
            int ans = bgec.solve(g, colors);
            out.println(ans);
            for (int i = 0; i < m; i++) {
                int u = edges[0][i];
                int v = edges[1][i];
                out.append(colors[u][v] + 1).append(' ');
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

    static class BipartiteGraphEdgeColoring {
        int[][] left;
        int[][] right;
        int[][] colors;

        public BipartiteGraphEdgeColoring(int n, int m) {
            left = new int[n][Math.max(n, m)];
            right = new int[m][Math.max(n, m)];
        }

        public void paint(int a, int b, int c) {
            int old = colors[a][b];
            if (old != -1) {
                if (left[a][old] == b) {
                    left[a][old] = -1;
                }
                if (right[b][old] == a) {
                    right[b][old] = -1;
                }
            }
            colors[a][b] = c;
            left[a][c] = b;
            right[b][c] = a;
        }

        public void dfs(int b, int c1, int c2) {
            if (right[b][c2] == -1) {
                return;
            }
            int na = right[b][c2];
            int nb = left[na][c1];
            if (nb != -1) {
                dfs(nb, c1, c2);
                paint(na, nb, c2);
            }
            paint(na, b, c1);
        }

        public int solve(boolean[][] g, int[][] colors) {
            if (g.length == 0 || g[0].length == 0) {
                return 0;
            }
            int n = g.length;
            int m = g[0].length;
            this.colors = colors;
            int deg = 0;
            for (int i = 0; i < n; i++) {
                int local = 0;
                for (int j = 0; j < m; j++) {
                    local += g[i][j] ? 1 : 0;
                }
                deg = Math.max(deg, local);
            }
            for (int j = 0; j < m; j++) {
                int local = 0;
                for (int i = 0; i < n; i++) {
                    local += g[i][j] ? 1 : 0;
                }
                deg = Math.max(deg, local);
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    colors[i][j] = -1;
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < deg; j++) {
                    left[i][j] = -1;
                }
            }
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < deg; j++) {
                    right[i][j] = -1;
                }
            }

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (!g[i][j]) {
                        continue;
                    }
                    int c = -1;
                    int ca = -1;
                    int cb = -1;
                    for (int k = 0; k < deg; k++) {
                        if (left[i][k] == -1 && right[j][k] == -1) {
                            c = k;
                            break;
                        }
                        if (left[i][k] == -1) {
                            cb = k;
                        }
                        if (right[j][k] == -1) {
                            ca = k;
                        }
                    }
                    if (c != -1) {
                        paint(i, j, c);
                        continue;
                    }
                    dfs(j, ca, cb);
                    paint(i, j, cb);
                }
            }

            return deg;
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
}

