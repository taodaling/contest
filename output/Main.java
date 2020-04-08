import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.AbstractMap;
import java.util.TreeMap;
import java.io.Closeable;
import java.util.Map;
import java.io.Writer;
import java.util.Map.Entry;
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
            TheodoreRoosevelt solver = new TheodoreRoosevelt();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class TheodoreRoosevelt {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            int k = in.readInt();
            Point2D a = readPoint2D(in);
            Point2D b = readPoint2D(in);
            Point2D c = readPoint2D(in);
            DynamicConvexHull dch = new DynamicConvexHull(a, b, c);
            for (int i = 3; i < n; i++) {
                dch.add(readPoint2D(in));
            }
            int hit = 0;
            for (int i = 0; i < m; i++) {
                Point2D pt = readPoint2D(in);
                if (dch.contain(pt, true)) {
                    hit++;
                }
            }
            if (hit >= k) {
                out.println("YES");
            } else {
                out.println("NO");
            }
        }

        public Point2D readPoint2D(FastInput in) {
            return new Point2D(in.readInt(), in.readInt());
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

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput println(String c) {
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

    static class GeometryUtils {
        public static final double PREC = 1e-15;

        public static double valueOf(double x) {
            return x > -PREC && x < PREC ? 0 : x;
        }

        public static boolean near(double a, double b) {
            return valueOf(a - b) == 0;
        }

        public static double cross(double x1, double y1, double x2, double y2) {
            return valueOf(x1 * y2 - y1 * x2);
        }

        public static double theta(double y, double x) {
            double theta = Math.atan2(y, x);
            if (theta < 0) {
                theta += Math.PI * 2;
            }
            return theta;
        }

    }

    static class Line2D {
        public final Point2D a;
        public final Point2D b;
        public final Point2D d;

        public Line2D(Point2D a, Point2D b) {
            this.a = a;
            this.b = b;
            d = new Point2D(b.x - a.x, b.y - a.y);
        }

        public String toString() {
            return d.toString();
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

    static class Segment2D extends Line2D {
        public Segment2D(Point2D a, Point2D b) {
            super(a, b);
        }

        public boolean contain(Point2D p) {
            return GeometryUtils.cross(p.x - a.x, p.y - a.y, d.x, d.y) == 0
                    && GeometryUtils.valueOf(p.x - Math.min(a.x, b.x)) >= 0
                    && GeometryUtils.valueOf(p.x - Math.max(a.x, b.x)) <= 0
                    && GeometryUtils.valueOf(p.y - Math.min(a.y, b.y)) >= 0
                    && GeometryUtils.valueOf(p.y - Math.max(a.y, b.y)) <= 0;
        }

        public boolean containWithoutEndpoint(Point2D p) {
            return contain(p) && !p.equals(a) && !p.equals(b);
        }

    }

    static class Point2D {
        public final double x;
        public final double y;

        public Point2D(double x, double y) {
            this.x = x;//GeometryUtils.valueOf(x);
            this.y = y;//GeometryUtils.valueOf(y);
        }

        public double distance2Between(Point2D another) {
            double dx = x - another.x;
            double dy = y - another.y;
            return dx * dx + dy * dy;
        }

        public double cross(Point2D a, Point2D b) {
            return GeometryUtils.cross(a.x - x, a.y - y, b.x - x, b.y - y);
        }

        public String toString() {
            return String.format("(%f, %f)", x, y);
        }

        public int hashCode() {
            return (int) (Double.doubleToLongBits(x) * 31 + Double.doubleToLongBits(y));
        }

        public boolean equals(Object obj) {
            Point2D other = (Point2D) obj;
            return x == other.x && y == other.y;
        }

    }

    static class DynamicConvexHull {
        private TreeMap<Double, Point2D> pts = new TreeMap<>((a, b) -> GeometryUtils.near(a, b) ? 0 : a.compareTo(b));
        private Point2D center;

        public DynamicConvexHull(Point2D center) {
            this.center = center;
        }

        public DynamicConvexHull(Point2D a, Point2D b, Point2D c) {
            this(new Point2D((a.x + b.x + c.x) / 3, (a.y + b.y + c.y) / 3));
            add(a);
            add(b);
            add(c);
        }

        private Map.Entry<Double, Point2D> clockwise(Double theta) {
            Map.Entry<Double, Point2D> floor = pts.floorEntry(theta);
            if (floor == null) {
                floor = pts.lastEntry();
            }
            return floor;
        }

        private Map.Entry<Double, Point2D> countclockwise(Double theta) {
            Map.Entry<Double, Point2D> ceil = pts.ceilingEntry(theta);
            if (ceil == null) {
                ceil = pts.firstEntry();
            }
            return ceil;
        }

        private boolean contain(Point2D point, Double theta, boolean close) {
            Point2D cw = clockwise(theta).getValue();
            Point2D ccw = countclockwise(theta).getValue();

            if (cw == ccw) {
                Segment2D seg = new Segment2D(center, ccw);
                if (close) {
                    return seg.contain(point);
                }
                return seg.containWithoutEndpoint(point);
            }

            if (close) {
                return center.cross(cw, point) >= 0 && cw.cross(ccw, point) >= 0 && ccw.cross(center, point) >= 0;
            }
            return center.cross(cw, point) > 0 && cw.cross(ccw, point) > 0 && ccw.cross(center, point) > 0;
        }

        public boolean contain(Point2D point, boolean close) {
            if (pts.isEmpty()) {
                return false;
            }
            return contain(point, GeometryUtils.theta(point.y - center.y, point.x - center.x), close);
        }

        public void add(Point2D point) {
            Double theta = GeometryUtils.theta(point.y - center.y, point.x - center.x);
            if (pts.size() < 3) {
                Point2D exists = pts.get(theta);
                if (exists != null && center.distance2Between(exists) >= center.distance2Between(point)) {
                    return;
                }
                pts.put(theta, point);
                return;
            }

            if (contain(point, theta, true)) {
                return;
            }

            // clockwise
            while (pts.size() >= 3) {
                Map.Entry<Double, Point2D> cw = clockwise(theta);
                pts.remove(cw.getKey());
                Point2D next = clockwise(theta).getValue();
                if (point.cross(cw.getValue(), next) < 0) {
                    pts.put(cw.getKey(), cw.getValue());
                    break;
                }
            }
            // counterclockwise
            while (pts.size() >= 3) {
                Map.Entry<Double, Point2D> ccw = countclockwise(theta);
                pts.remove(ccw.getKey());
                Point2D next = countclockwise(theta).getValue();
                if (point.cross(ccw.getValue(), next) > 0) {
                    pts.put(ccw.getKey(), ccw.getValue());
                    break;
                }
            }

            pts.put(theta, point);
        }

    }
}

