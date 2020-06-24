import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
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
            P3232HNOI2013 solver = new P3232HNOI2013();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class P3232HNOI2013 {
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();

            double[][] mat = new double[n][n];
            for (int i = 0; i < n; i++) {
                mat[i][i] = 1;
            }
            int[][] edges = new int[m][2];
            int[] deg = new int[n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < 2; j++) {
                    edges[i][j] = in.readInt() - 1;
                    deg[edges[i][j]]++;
                }
            }
            for (int i = 0; i < m; i++) {
                int u = edges[i][0];
                int v = edges[i][1];
                if (u != n - 1) {
                    mat[v][u] -= 1.0 / deg[u];
                }
                if (v != n - 1) {
                    mat[u][v] -= 1.0 / deg[v];
                }
            }


            GuassianElimination ge = new GuassianElimination(n, n, 1e-10);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    ge.setLeft(i, j, mat[i][j]);
                }
            }
            ge.setRight(0, 1);
            ge.solve();
            double[] x = ge.getSolutions();

            double[] weight = new double[m];
            for (int i = 0; i < m; i++) {
                int u = edges[i][0];
                int v = edges[i][1];
                if (u != n - 1) {
                    weight[i] += x[u] / deg[u];
                }
                if (v != n - 1) {
                    weight[i] += x[v] / deg[v];
                }
            }


            debug.debug("x", x);
            debug.debug("weight", weight);
            Randomized.shuffle(weight);
            Arrays.sort(weight);
            KahanSummation sum = new KahanSummation();
            for (int i = 0; i < m; i++) {
                sum.add((m - i) * weight[i]);
            }

            double ans = sum.sum();
            out.printf("%.3f", ans);
        }

    }

    static class Randomized {
        public static void shuffle(double[] data) {
            shuffle(data, 0, data.length - 1);
        }

        public static void shuffle(double[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                double tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return RandomWrapper.INSTANCE.nextInt(l, r);
        }

    }

    static class GuassianElimination {
        double[][] mat;
        int rank;
        final double prec;
        int n;
        int m;
        boolean[] independent;
        double[] solutions;

        public GuassianElimination(int n, int m, double prec) {
            this.prec = prec;
            this.n = n;
            this.m = m;
            mat = new double[n + 1][m + 1];
            solutions = mat[n];
            independent = new boolean[m];
        }

        public void setRight(int row, double val) {
            mat[row][mat[row].length - 1] = val;
        }

        public void setLeft(int row, int col, double val) {
            mat[row][col] = val;
        }

        public boolean solve() {
            int n = mat.length - 1;
            int m = mat[0].length - 1;

            int now = 0;
            for (int i = 0; i < m; i++) {
                int maxRow = now;
                for (int j = now; j < n; j++) {
                    if (Math.abs(mat[j][i]) > Math.abs(mat[maxRow][i])) {
                        maxRow = j;
                    }
                }

                if (Math.abs(mat[maxRow][i]) <= prec) {
                    continue;
                }
                swapRow(now, maxRow);
                divideRow(now, mat[now][i]);
                for (int j = now + 1; j < n; j++) {
                    if (mat[j][i] == 0) {
                        continue;
                    }
                    double f = mat[j][i];
                    subtractRow(j, now, f);
                }

                now++;
            }

            for (int i = now; i < n; i++) {
                if (Math.abs(mat[i][m]) > prec) {
                    return false;
                }
            }

            rank = now;
            for (int i = now - 1; i >= 0; i--) {
                int x = -1;
                for (int j = 0; j < m; j++) {
                    if (Math.abs(mat[i][j]) > prec) {
                        x = j;
                        break;
                    }
                }
                mat[n][x] = mat[i][m] / mat[i][x];
                independent[x] = true;
                for (int j = i - 1; j >= 0; j--) {
                    if (mat[j][x] == 0) {
                        continue;
                    }
                    mat[j][m] -= mat[j][x] * mat[n][x];
                    mat[j][x] = 0;
                }
            }
            return true;
        }

        void swapRow(int i, int j) {
            double[] row = mat[i];
            mat[i] = mat[j];
            mat[j] = row;
        }

        void subtractRow(int i, int j, double f) {
            int m = mat[0].length;
            for (int k = 0; k < m; k++) {
                mat[i][k] -= mat[j][k] * f;
            }
        }

        void divideRow(int i, double f) {
            int m = mat[0].length;
            for (int k = 0; k < m; k++) {
                mat[i][k] /= f;
            }
        }

        public double[] getSolutions() {
            return solutions;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < n; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < m; j++) {
                    if (mat[i][j] == 0) {
                        continue;
                    }
                    if (mat[i][j] != 1) {
                        row.append(mat[i][j]);
                    }
                    row.append("x").append(j).append('+');
                }
                if (row.length() > 0) {
                    row.setLength(row.length() - 1);
                } else {
                    row.append(0);
                }
                row.append("=").append(mat[i][m]);
                builder.append(row).append('\n');
            }
            return builder.toString();
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

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
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

    static class KahanSummation {
        private double error;
        private double sum;

        public double sum() {
            return sum;
        }

        public void add(double x) {
            x = x - error;
            double t = sum + x;
            error = (t - sum) - x;
            sum = t;
        }

        public String toString() {
            return new BigDecimal(sum).toString();
        }

    }

    static class RandomWrapper {
        private Random random;
        public static final RandomWrapper INSTANCE = new RandomWrapper(new Random());

        public RandomWrapper() {
            this(new Random());
        }

        public RandomWrapper(Random random) {
            this.random = random;
        }

        public int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, Object x) {
            return debug(name, x, empty);
        }

        public Debug debug(String name, Object x, int... indexes) {
            if (offline) {
                if (x == null || !x.getClass().isArray()) {
                    out.append(name);
                    for (int i : indexes) {
                        out.printf("[%d]", i);
                    }
                    out.append("=").append("" + x);
                    out.println();
                } else {
                    indexes = Arrays.copyOf(indexes, indexes.length + 1);
                    if (x instanceof byte[]) {
                        byte[] arr = (byte[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof short[]) {
                        short[] arr = (short[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof boolean[]) {
                        boolean[] arr = (boolean[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof char[]) {
                        char[] arr = (char[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof int[]) {
                        int[] arr = (int[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof float[]) {
                        float[] arr = (float[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof double[]) {
                        double[] arr = (double[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else if (x instanceof long[]) {
                        long[] arr = (long[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    } else {
                        Object[] arr = (Object[]) x;
                        for (int i = 0; i < arr.length; i++) {
                            indexes[indexes.length - 1] = i;
                            debug(name, arr[i], indexes);
                        }
                    }
                }
            }
            return this;
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

