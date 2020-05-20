import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
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
            DKarenAndCards solver = new DKarenAndCards();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DKarenAndCards {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] limits = new int[3];
            for (int i = 0; i < 3; i++) {
                limits[i] = in.readInt();
            }
            int[][] cards = new int[n + 1][3];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 3; j++) {
                    cards[i][j] = in.readInt();
                }
            }
            cards[n][2] = limits[2];
            n++;

            Arrays.sort(cards, (a, b) -> Integer.compare(a[2], b[2]));
            int[][] prefix = new int[n][2];
            int[][] suffix = new int[n][2];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 2; j++) {
                    prefix[i][j] = cards[i][j];
                    if (i > 0) {
                        prefix[i][j] = Math.max(prefix[i][j], prefix[i - 1][j]);
                    }
                }
            }
            for (int i = n - 1; i >= 0; i--) {
                for (int j = 0; j < 2; j++) {
                    suffix[i][j] = cards[i][j];
                    if (i < n - 1) {
                        suffix[i][j] = Math.max(suffix[i][j], suffix[i + 1][j]);
                    }
                }
            }

            debug.debug("cards", cards);
            debug.debug("prefix", prefix);
            debug.debug("suffix", suffix);
            long ans = 0;
            IntegerRect2 empty = new IntegerRect2(0, 0, 0, 0);
            int last = 0;
            for (int i = 0; i < n; i++) {
                int r = i;
                while (r + 1 < n && cards[i][2] == cards[r + 1][2]) {
                    r++;
                }
                int way = cards[i][2] - last;
                last = cards[i][2];
                IntegerRect2 left = i == 0 ? empty : new IntegerRect2(0, 0, prefix[i - 1][0], prefix[i - 1][1]);
                IntegerRect2 right = new IntegerRect2(suffix[i][0], suffix[i][1], limits[0], limits[1]);
                long contrib = way * (right.area() - IntegerRect2.intersect(left, right).area());
                ans += contrib;
                debug.debug("i", i);
                debug.debug("h", cards[i][2]);
                //debug.debug("cards[i]", cards[i]);
                debug.debug("contrib", contrib);
                i = r;
            }

            out.println(ans);
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

    static class IntegerRect2 {
        public long[] lb = new long[2];
        public long[] rt = new long[2];

        public IntegerRect2(long x0, long y0, long x1, long y1) {
            lb[0] = x0;
            lb[1] = y0;
            rt[0] = x1;
            rt[1] = y1;
        }

        public IntegerRect2() {
        }

        public long area() {
            long ans = 1;
            for (int i = 0; i < 2; i++) {
                ans *= Math.max(0, rt[i] - lb[i]);
            }
            return ans;
        }

        public static IntegerRect2 intersect(IntegerRect2 a, IntegerRect2 b) {
            IntegerRect2 ans = new IntegerRect2();
            for (int i = 0; i < 2; i++) {
                ans.lb[i] = Math.max(a.lb[i], b.lb[i]);
                ans.rt[i] = Math.min(a.rt[i], b.rt[i]);
            }
            return ans;
        }

        public String toString() {
            return Arrays.toString(lb) + ":" + Arrays.toString(rt);
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

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;
        static int[] empty = new int[0];

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, int x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, long x) {
            if (offline) {
                debug(name, "" + x);
            }
            return this;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
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
}

