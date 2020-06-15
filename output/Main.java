import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.LongStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.stream.Stream;
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
            AVoltageKeepsake solver = new AVoltageKeepsake();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class AVoltageKeepsake {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int p = in.readInt();
            Point[] pts = new Point[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new Point();
                pts[i].a = in.readInt();
                pts[i].b = in.readInt();
                pts[i].threshold = pts[i].b / (double) pts[i].a;
            }

            long sum = Arrays.stream(pts).mapToLong(x -> x.a).sum();
            if (sum <= p) {
                out.println(-1);
                return;
            }
            Arrays.sort(pts, (a, b) -> Double.compare(a.threshold, b.threshold));
            long sumA = 0;
            long sumB = 0;
            double ans = pts[0].threshold;
            for (int i = 0; i < n; i++) {
                sumA += pts[i].a;
                sumB += pts[i].b;
                if (sumA <= p) {
                    ans = Math.max(ans, pts[i + 1].threshold);
                } else {
                    double local = (double) sumB / (sumA - p);
                    if (i + 1 < n) {
                        local = Math.min(local, pts[i + 1].threshold);
                    }
                    if (local >= pts[i].threshold) {
                        ans = Math.max(ans, local);
                    }
                }
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

        public FastOutput append(int c) {
            cache.append(c);
            return this;
        }

        public FastOutput append(double c) {
            cache.append(new BigDecimal(c).toPlainString());
            return this;
        }

        public FastOutput println(int c) {
            return append(c).println();
        }

        public FastOutput println(double c) {
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

    static class Point {
        int a;
        int b;
        double threshold;

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

