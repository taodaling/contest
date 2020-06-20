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
            BExtension solver = new BExtension();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class BExtension {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int a = in.readInt();
            int b = in.readInt();
            int c = in.readInt();
            int d = in.readInt();
            ArrayIndex ai = new ArrayIndex(c + 1, d + 1, 2);
            int[] dp = new int[ai.totalSize()];
            dp[ai.indexOf(a, b, 0)] = 1;
            Modular mod = new Modular(998244353);
            for (int i = a; i <= c; i++) {
                for (int j = b; j <= d; j++) {
                    for (int k = 0; k < 2; k++) {
                        if (dp[ai.indexOf(i, j, k)] == 0) {
                            continue;
                        }
                        if (k == 0 && j + 1 <= d) {
                            dp[ai.indexOf(i, j + 1, 0)] = mod.plus(dp[ai.indexOf(i, j + 1, 0)], mod.mul(dp[ai.indexOf(i, j, k)], i));
                        }
                        if (k == 0 && i + 1 <= c) {
                            dp[ai.indexOf(i + 1, j, 1)] = mod.plus(dp[ai.indexOf(i + 1, j, 1)], mod.mul(dp[ai.indexOf(i, j, k)], j));
                        }
                        if (k == 1 && j + 1 <= d) {
                            dp[ai.indexOf(i, j + 1, 0)] = mod.plus(dp[ai.indexOf(i, j + 1, 0)], mod.mul(dp[ai.indexOf(i, j, k)], 1));
                        }
                        if (k == 1 && i + 1 <= c) {
                            dp[ai.indexOf(i + 1, j, 1)] = mod.plus(dp[ai.indexOf(i + 1, j, 1)], mod.mul(dp[ai.indexOf(i, j, k)], j));
                        }
                    }
                }
            }

            // debug.debug("dp", dp);
            debug.debug("dp", dp, ai);

            int ans = mod.plus(dp[ai.indexOf(c, d, 0)], dp[ai.indexOf(c, d, 1)]);
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

        public int[] inverse(int i) {
            int[] ans = new int[dimensions.length];
            for (int j = dimensions.length - 1; j >= 0; j--) {
                ans[j] = i % dimensions[j];
                i /= dimensions[j];
            }
            return ans;
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

        public Debug debug(String name, Object x, ArrayIndex ai) {
            if (offline) {
                if (x == null) {
                    debug(name, x);
                    return this;
                }
                if (!x.getClass().isArray()) {
                    throw new IllegalArgumentException();
                }
                if (x instanceof byte[]) {
                    byte[] arr = (byte[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof short[]) {
                    short[] arr = (short[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof boolean[]) {
                    boolean[] arr = (boolean[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof char[]) {
                    char[] arr = (char[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof int[]) {
                    int[] arr = (int[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof float[]) {
                    float[] arr = (float[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof double[]) {
                    double[] arr = (double[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else if (x instanceof long[]) {
                    long[] arr = (long[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                } else {
                    Object[] arr = (Object[]) x;
                    for (int i = 0; i < arr.length; i++) {
                        int[] indexes = ai.inverse(i);
                        debug(name, arr[i], indexes);
                    }
                }
            }
            return this;
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

    static class Modular {
        int m;

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
}

