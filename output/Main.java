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
            DChattering solver = new DChattering();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DChattering {
        Debug debug = new Debug(false);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[] w = new int[n];
            in.populate(w);

            int[] left = new int[3 * n];
            int[] right = new int[3 * n];
            for (int i = 0; i < n * 3; i++) {
                left[i] = Math.max(i - w[i % n], 0);
                right[i] = Math.min(i + w[i % n], 3 * n - 1);
            }
            IntegerSparseTable leftIST = new IntegerSparseTable(i -> i, n * 3, (a, b) -> left[a] < left[b] ? a : b);
            IntegerSparseTable rightIST = new IntegerSparseTable(i -> i, n * 3, (a, b) -> right[a] > right[b] ? a : b);

            debug.debug("left", left);
            debug.debug("right", right);
            ArrayIndex ai = new ArrayIndex(3 * n, 21, 2);
            int[] jump = new int[ai.totalSize()];
            for (int i = 0; i < n * 3; i++) {
                jump[ai.indexOf(i, 0, 0)] = left[i];
                jump[ai.indexOf(i, 0, 1)] = right[i];
            }
            for (int i = 0; i + 1 <= 20; i++) {
                for (int j = 0; j < 3 * n; j++) {
                    int l = jump[ai.indexOf(j, i, 0)];
                    int r = jump[ai.indexOf(j, i, 1)];
                    jump[ai.indexOf(j, i + 1, 0)] = jump[ai.indexOf(leftIST.query(l, r), i, 0)];
                    jump[ai.indexOf(j, i + 1, 1)] = jump[ai.indexOf(rightIST.query(l, r), i, 1)];
                }
            }

            for (int i = n; i < 2 * n; i++) {
                int l = i;
                int r = i;
                int time = 0;
                for (int j = 20; j >= 0; j--) {
                    if (jump[ai.indexOf(r, j, 1)] - jump[ai.indexOf(l, j, 0)] + 1 < n) {
                        int oldL = jump[ai.indexOf(l, j, 0)];
                        int oldR = jump[ai.indexOf(r, j, 1)];
                        r = rightIST.query(oldL, oldR);
                        l = leftIST.query(oldL, oldR);
                        time += 1 << j;
                    }
                }
                if (r - l + 1 < n) {
                    time++;
                }
                out.println(time);
            }
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

        public void populate(int[] data) {
            for (int i = 0; i < data.length; i++) {
                data[i] = readInt();
            }
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

    static class IntegerSparseTable {
        private int[][] st;
        private IntegerBinaryFunction merger;

        public IntegerSparseTable(IntToIntegerFunction function, int length, IntegerBinaryFunction merger) {
            int m = Log2.floorLog(length);
            st = new int[m + 1][length];
            this.merger = merger;
            for (int i = 0; i < length; i++) {
                st[0][i] = function.apply(i);
            }
            for (int i = 0; i < m; i++) {
                int interval = 1 << i;
                for (int j = 0; j < length; j++) {
                    if (j + interval < length) {
                        st[i + 1][j] = merge(st[i][j], st[i][j + interval]);
                    } else {
                        st[i + 1][j] = st[i][j];
                    }
                }
            }
        }

        private int merge(int a, int b) {
            return merger.apply(a, b);
        }

        public int query(int left, int right) {
            int queryLen = right - left + 1;
            int bit = Log2.floorLog(queryLen);
            // x + 2^bit == right + 1
            // So x should be right + 1 - 2^bit - left=queryLen - 2^bit
            return merge(st[bit][left], st[bit][right + 1 - (1 << bit)]);
        }

        public String toString() {
            return Arrays.toString(st[0]);
        }

    }

    static interface IntToIntegerFunction {
        int apply(int x);

    }

    static class ArrayIndex {
        int[] dimensions;

        public ArrayIndex(int... dimensions) {
            this.dimensions = dimensions;
        }

        public int totalSize() {
            int ans = 1;
            for (int x : dimensions) {
                ans *= x;
            }
            return ans;
        }

        public int indexOf(int a, int b) {
            return a * dimensions[1] + b;
        }

        public int indexOf(int a, int b, int c) {
            return indexOf(a, b) * dimensions[2] + c;
        }

    }

    static interface IntegerBinaryFunction {
        int apply(int a, int b);

    }

    static class Log2 {
        public static int floorLog(int x) {
            return 31 - Integer.numberOfLeadingZeros(x);
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
}

