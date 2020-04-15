import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.RuntimeException;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
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
            ConnectedGraph solver = new ConnectedGraph();
            try {
                int testNumber = 1;
                while (true)
                    solver.solve(testNumber++, in, out);
            } catch (UnknownError e) {
                out.close();
            }
        }
    }

    static class ConnectedGraph {
        BigInteger[][] comp = new BigInteger[51][51];
        BigInteger[] pow2 = new BigInteger[2000];
        BigInteger[] f = new BigInteger[51];

        public BigInteger pow2(int i) {
            if (pow2[i] == null) {
                if (i == 0) {
                    return pow2[i] = BigInteger.ONE;
                }
                pow2[i] = pow2(i - 1).multiply(BigInteger.valueOf(2));
            }
            return pow2[i];
        }

        public BigInteger comp(int n, int m) {
            if (n < m) {
                return BigInteger.ZERO;
            }
            if (comp[n][m] == null) {
                if (m == 0) {
                    return comp[n][m] = BigInteger.ONE;
                }
                comp[n][m] = comp(n - 1, m).add(comp(n - 1, m - 1));
            }
            return comp[n][m];
        }

        int pick2(int n) {
            return n * (n - 1) / 2;
        }

        public BigInteger f(int n) {
            if (f[n] == null) {
                f[n] = pow2(pick2(n));
                for (int i = 1; i <= n - 1; i++) {
                    BigInteger sub = comp(n - 1, i - 1).multiply(f(i)).multiply(pow2(pick2(n - i)));
                    f[n] = f[n].subtract(sub);
                }
            }
            return f[n];
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            if (n == 0) {
                throw new UnknownError();
            }
            out.println(f(n));
        }

    }

    static class FastOutput implements Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(1 << 13);
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

        public FastOutput append(Object c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(Object c) {
            return append(c).println();
        }

        public FastOutput println() {
            cache.append('\n');
            return this;
        }

        public FastOutput flush() {
            try {
                os.append(cache);
                os.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
}

