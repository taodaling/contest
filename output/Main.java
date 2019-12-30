import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
import java.io.UncheckedIOException;
import java.util.List;
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
            ESashaCircle solver = new ESashaCircle();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class ESashaCircle {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();
            Point2D[] a = new Point2D[n];
            Point2D[] b = new Point2D[m];
            for (int i = 0; i < n; i++) {
                a[i] = new Point2D(in.readInt(), in.readInt());
            }
            for (int i = 0; i < m; i++) {
                b[i] = new Point2D(in.readInt(), in.readInt());
            }

            if (check(Circle.minCircleCover(Arrays.asList(a)), b) ||
                    check(Circle.minCircleCover(Arrays.asList(b)), a)) {
                out.println("YES");
            } else {
                out.println("NO");
            }
        }

        public boolean check(Circle c, Point2D[] pts) {
            for (Point2D pt : pts) {
                if (c.contain(pt, true)) {
                    return false;
                }
            }
            return true;
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

    static class Line2D {
        public final Point2D a;
        public final Point2D b;
        public final Point2D d;

        public Line2D(Point2D a, Point2D b) {
            this.a = a;
            this.b = b;
            d = new Point2D(b.x - a.x, b.y - a.y);
        }

        public Point2D intersect(Line2D another) {
            double m11 = b.x - a.x;
            double m01 = another.b.x - another.a.x;
            double m10 = a.y - b.y;
            double m00 = another.a.y - another.b.y;

            double div = GeometryUtils.valueOf(m00 * m11 - m01 * m10);
            if (div == 0) {
                return null;
            }

            double v0 = (another.a.x - a.x) / div;
            double v1 = (another.a.y - a.y) / div;

            double alpha = m00 * v0 + m01 * v1;
            return getPoint(alpha);
        }

        public Point2D getPoint(double alpha) {
            return new Point2D(a.x + d.x * alpha, a.y + d.y * alpha);
        }

        public String toString() {
            return d.toString();
        }

    }

    static class Segment2D extends Line2D {
        public Segment2D(Point2D a, Point2D b) {
            super(a, b);
        }

        public Line2D getPerpendicularBisector() {
            Point2D center = new Point2D((a.x + b.x) / 2, (a.y + b.y) / 2);
            Point2D apeak = d.getApeak();
            return new Line2D(center, center.add(apeak));
        }

    }

    static class Randomized {
        static Random random = new Random();

        public static <T> void randomizedArray(T[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                T tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static class Triangle implements Shape {
        Point2D a;
        Point2D b;
        Point2D c;

        public Triangle(Point2D a, Point2D b, Point2D c) {
            if (a.cross(b, c) < 0) {
                Point2D tmp = b;
                b = c;
                c = tmp;
            }
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public Circle outerCircle() {
            Segment2D ab = new Segment2D(a, b);
            Segment2D ac = new Segment2D(a, c);
            Line2D abPB = ab.getPerpendicularBisector();
            Line2D acPB = ac.getPerpendicularBisector();
            Point2D center = abPB.intersect(acPB);
            return new Circle(center, center.distanceBetween(a));
        }

    }

    static class Circle implements Shape {
        public final Point2D center;
        public final double radius;

        public Circle(Point2D center, double radius) {
            this.center = center;
            this.radius = radius;
        }

        public boolean contain(Point2D pt, boolean cover) {
            double dx = pt.x - center.x;
            double dy = pt.y - center.y;
            double dist2 = dx * dx + dy * dy - radius * radius;
            if (cover) {
                return dist2 <= 0;
            }
            return dist2 < 0;
        }

        public static Circle minCircleCover(Point2D[] pts, int l, int r) {
            Randomized.randomizedArray(pts, l, r + 1);
            Circle circle = new Circle(new Point2D(0, 0), 0);
            for (int i = l; i <= r; i++) {
                if (circle.contain(pts[i], true)) {
                    continue;
                }
                circle = new Circle(pts[i], 0);
                for (int j = l; j < i; j++) {
                    if (circle.contain(pts[j], true)) {
                        continue;
                    }
                    circle = getCircleByDiameter(pts[i], pts[j]);
                    for (int k = l; k < j; k++) {
                        if (circle.contain(pts[k], true)) {
                            continue;
                        }
                        circle = new Triangle(pts[i], pts[j], pts[k]).outerCircle();
                    }
                }
            }
            return circle;
        }

        public static Circle getCircleByDiameter(Point2D a, Point2D b) {
            return new Circle(new Point2D((a.x + b.x) / 2, (a.y + b.y) / 2), a.distanceBetween(b) / 2);
        }

        public static Circle minCircleCover(List<Point2D> pts) {
            return minCircleCover(pts.toArray(new Point2D[0]), 0, pts.size() - 1);
        }

        public String toString() {
            return "Circle " + center + " with radius=" + radius;
        }

    }

    static class GeometryUtils {
        public static final double PREC = 1e-15;

        public static double valueOf(double x) {
            return x > -PREC && x < PREC ? 0 : x;
        }

        public static double cross(double x1, double y1, double x2, double y2) {
            return valueOf(x1 * y2 - y1 * x2);
        }

    }

    static interface Shape {
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

        public double distanceBetween(Point2D another) {
            return Math.sqrt(distance2Between(another));
        }

        public double cross(Point2D a, Point2D b) {
            return GeometryUtils.cross(a.x - x, a.y - y, b.x - x, b.y - y);
        }

        public Point2D getApeak() {
            return new Point2D(-y, x);
        }

        public Point2D add(Point2D vector) {
            return new Point2D(x + vector.x, y + vector.y);
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
}

