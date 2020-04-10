import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
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
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            OptimistVsPessimist solver = new OptimistVsPessimist();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class OptimistVsPessimist {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int k = in.readInt();
            double[] areas = new double[n];
            int inf = (int) 1e8;
            for (int i = 0; i < n; i++) {
                double l = inf;
                double r = -inf;
                double b = inf;
                double t = -inf;
                for (int j = 0; j < 4; j++) {
                    double x = in.readInt();
                    double y = in.readInt();
                    l = Math.min(l, x);
                    r = Math.max(r, x);
                    b = Math.min(b, y);
                    t = Math.max(t, y);
                }

                Point2 center = new Point2((l + r) / 2.0, (b + t) / 2.0);
                Point2 c1 = new Point2(in.readInt(), in.readInt());
                Point2 c2 = new Point2(in.readInt(), in.readInt());
                if (Point2.onSegment(center, c1, c2) || Point2.onSegment(center, c2, c1)) {
                    areas[i] = 0;
                } else {
                    areas[i] = (r - l) * (t - b) / 2.0;
                }
            }

            Arrays.sort(areas);
            double max = 0;
            double min = 0;
            for (int i = 0; i < k; i++) {
                min += areas[i];
                max += areas[n - 1 - i];
            }
            out.append(min).append(' ').append(max);
        }

    }

    static class Point2 implements Cloneable {
        public final double x;
        public final double y;

        public Point2(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point2() {
            this(0, 0);
        }

        public static double dot(double x1, double y1, double x2, double y2) {
            return x1 * x2 + y1 * y2;
        }

        private static double cross(double x1, double y1, double x2, double y2) {
            return x1 * y2 - y1 * x2;
        }

        public static int orient(Point2 a, Point2 b, Point2 c) {
            return GeoConstant.sign(cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y));
        }

        public static boolean inDisk(Point2 a, Point2 b, Point2 c) {
            return GeoConstant.sign(dot(a.x - c.x, a.y - c.y, b.x - c.x, b.y - c.y)) <= 0;
        }

        public static boolean onSegment(Point2 a, Point2 b, Point2 c) {
            return orient(a, b, c) == 0 && inDisk(a, b, c);
        }

        public Point2 clone() {
            try {
                return (Point2) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            return String.format("(%.6f, %.6f)", x, y);
        }

    }

    static class GeoConstant {
        public static final double PREC = 1e-6;

        public static boolean isZero(double x) {
            return -PREC <= x && x <= PREC;
        }

        public static int sign(double x) {
            return isZero(x) ? 0 : x < 0 ? -1 : 1;
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

        public FastOutput append(double c) {
            cache.append(new BigDecimal(c).toPlainString());
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

