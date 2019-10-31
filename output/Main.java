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
 * Built using CHelper plug-in Actual solution is at the top
 * 
 * @author daltao
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "daltao", 1 << 27);
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
            SkolemXORTree solver = new SkolemXORTree();
            solver.solve(1, in, out);
            out.close();
        }
    }
    static class SkolemXORTree {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            if (Integer.bitCount(n) == 1) {
                out.println("No");
                return;
            }
            out.println("Yes");
            DigitUtils.Log2 log2 = new DigitUtils.Log2();
            int floorLog = log2.floorLog(n + 1);

            int m = (1 << floorLog);
            for (int i = 2; i < m; i++) {
                printEdge(out, i - 1, i);
            }
            printEdge(out, m - 1, n + 1);
            for (int i = 2; i < m; i++) {
                printEdge(out, n + i - 1, n + i);
            }


            if (m <= n) {
                int since = m;
                int until = n;
                if (DigitUtils.isOdd(until - since + 1)) {
                    until--;
                }

                for (int i = since + 1; i <= until; i++) {
                    printEdge(out, i - 1, i);
                    printEdge(out, i - 1 + n, i + n);
                }
                printEdge(out, until - (m - 1), until);
                printEdge(out, m - 1, n + since);

                if (until == n - 1) {
                    printEdge(out, n, until);
                    printEdge(out, n + n, until - (m - 1) - 1);
                }
            }
        }

        public void printEdge(FastOutput out, int a, int b) {
            out.append(a).append(' ').append(b).append('\n');
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

        public FastOutput append(char c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(int c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
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
    static class DigitUtils {
        private static final long[] DIGIT_VALUES = new long[19];
        static {
            DIGIT_VALUES[0] = 1;
            for (int i = 1; i < 19; i++) {
                DIGIT_VALUES[i] = DIGIT_VALUES[i - 1] * 10;
            }
        }

        private DigitUtils() {}

        public static boolean isOdd(int x) {
            return (x & 1) == 1;
        }

        public static class Log2 {
            public int floorLog(int x) {
                return 31 - Integer.numberOfLeadingZeros(x);
            }

        }

    }
}

