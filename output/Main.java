import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
            CFindACar solver = new CFindACar();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class CFindACar {
        Modular mod = new Modular(1e9 + 7);
        Debug debug = new Debug(true);
        int c1 = 0;
        int c2 = 0;
        int[][] mem1 = new int[30][2];
        int[][] mem2 = new int[30][2];

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int q = in.readInt();

            int highestBit = 29;
            for (int i = 0; i < q; i++) {
                int x1 = in.readInt();
                int y1 = in.readInt();
                int x2 = in.readInt();
                int y2 = in.readInt();
                int k = in.readInt() - 1;
                int a11 = dfs(highestBit, x2, y2, k, 0)[0];
                int a01 = dfs(highestBit, x2, y1 - 1, k, 0)[0];
                int a10 = dfs(highestBit, x1 - 1, y2, k, 0)[0];
                int a00 = dfs(highestBit, x1 - 1, y1 - 1, k, 0)[0];

                int ans = mod.plus(a11, a00);
                ans = mod.subtract(ans, a01);
                ans = mod.subtract(ans, a10);
                out.println(ans);
            }

            debug.debug("c1", c1);
            debug.debug("c2", c2);
        }

        public int sum(int n) {
            //1 + ... + n
            return mod.valueOf((long) (n + 1) * n / 2);
        }

        public int[] dfs(int bit, int n, int m, int k, int trace) {
            if (n > m) {
                int tmp = n;
                n = m;
                m = tmp;
            }
            if (n == 0 || m == 0 || trace > k) {
                return new int[2];
            }
            //full
            //
            int size = 1 << (bit + 1);
            if (n == size && m == size) {
                int[] ans = new int[2];
                int allow = Math.min((trace | (size - 1)), k) - trace;
                int cnt = mod.mul(size, allow + 1);
                ans[0] = cnt;
                ans[1] = mod.mul(sum(allow), size);
                ans[1] = mod.plus(ans[1], mod.mul(cnt, (trace + 1)));
                return ans;
            }

            if (n == size || m == size) {
                c1++;
            } else {
                c2++;
            }

            int[] nSub = mem1[bit];
            nSub[0] = Math.min(n, size / 2);
            nSub[1] = n - nSub[0];
            int[] mSub = mem2[bit];
            mSub[0] = Math.min(m, size / 2);
            mSub[1] = m - mSub[0];

            //00 or 11
            int[] ans = new int[2];

            if (m < size || (trace | (size - 1)) > k) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        int[] ret = dfs(bit - 1, nSub[i], mSub[j], k, Bits.setBit(trace, bit, (i ^ j) == 1));
                        ans[0] = mod.plus(ans[0], ret[0]);
                        ans[1] = mod.plus(ans[1], ret[1]);
                    }
                }
            } else {
                for (int i = 0; i < 1; i++) {
                    for (int j = 0; j < 2; j++) {
                        int[] ret = dfs(bit - 1, nSub[i], mSub[j], k, Bits.setBit(trace, bit, (i ^ j) == 1));
                        ans[0] = mod.plus(ans[0], ret[0]);
                        ans[1] = mod.plus(ans[1], ret[1]);
                    }
                }
                int[] ret = dfs(bit - 1, nSub[1], mSub[0], k, Bits.setBit(trace, bit, true));
                ans[0] = mod.plus(ans[0], ret[0]);
                ans[0] = mod.plus(ans[0], ret[0]);
                ans[1] = mod.plus(ans[1], ret[1]);
                ans[1] = mod.plus(ans[1], ret[1]);
                ans[1] = mod.plus(ans[1], mod.mul(ret[0], 1 << bit));
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

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, int x) {
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

    static class Bits {
        private Bits() {
        }

        public static int setBit(int x, int i, boolean v) {
            if (v) {
                x |= 1 << i;
            } else {
                x &= ~(1 << i);
            }
            return x;
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

        public int subtract(int x, int y) {
            return valueOf(x - y);
        }

        public String toString() {
            return "mod " + m;
        }

    }
}

