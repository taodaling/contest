import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Deque;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Map;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.TreeMap;
import java.io.Closeable;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.ArrayDeque;
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
            DHongcowDrawsACircle solver = new DHongcowDrawsACircle();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class DHongcowDrawsACircle {
        Debug debug = new Debug(true);

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int m = in.readInt();

            Point2[] red = new Point2[n];
            Point2[] blue = new Point2[m];
            for (int i = 0; i < n; i++) {
                red[i] = new Point2(in.readInt(), in.readInt());
            }
            for (int i = 0; i < m; i++) {
                blue[i] = new Point2(in.readInt(), in.readInt());
            }

            Randomized.shuffle(red);
            Randomized.shuffle(blue);
            List<Point2> allPts = new ArrayList<>(n + m);
            allPts.addAll(Arrays.asList(red));
            allPts.addAll(Arrays.asList(blue));
            Point2[] ch = ConvexHull2.grahamScan(allPts).toArray(new Point2[0]);
            for (Point2 r : red) {
                for (int i = 0; i < ch.length; i++) {
                    Point2 cur = ch[i];
                    Point2 next = ch[(i + 1) % ch.length];
                    if (Point2.onSegment(cur, next, r)) {
                        out.println(-1);
                        return;
                    }
                }
            }


            double errorTolerant = 1e-5;
            double low = 0;
            for (Point2 pt : red) {
                DoubleBinarySearch dbs = new DoubleBinarySearch(errorTolerant, errorTolerant) {

                    public boolean check(double mid) {
                        return !DHongcowDrawsACircle.this.check(red, blue, pt, true, mid);
                    }
                };
                if (dbs.check(low)) {
                    continue;
                }
                low = dbs.binarySearch(low, 1e30);
            }
            debug.debug("red", low);

            for (Point2 pt : blue) {
                DoubleBinarySearch dbs = new DoubleBinarySearch(errorTolerant, errorTolerant) {

                    public boolean check(double mid) {
                        return !DHongcowDrawsACircle.this.check(red, blue, pt, false, mid);
                    }
                };
                if (dbs.check(low)) {
                    continue;
                }
                low = dbs.binarySearch(low, 1e30);
            }

            out.println(low);
        }

        public void radian(DoubleIntervalMap dm, Point2 a, double r, boolean include) {
            if (Point2.dist2(a, Point2.ORIGIN) >= r * r * 4) {
                return;
            }
            if (Point2.SORT_BY_XY.compare(a, Point2.ORIGIN) == 0) {
                return;
            }
            double dx = a.x;
            double dy = a.y;
            double dist = a.abs();
            double mx = a.x / 2;
            double my = a.y / 2;
            double moveX = -dy / dist;
            double moveY = dx / dist;
            double h = Math.sqrt(r * r - dist / 2 * dist / 2);
            double tx = mx + moveX * h;
            double ty = my + moveY * h;
            double bx = mx - moveX * h;
            double by = my - moveY * h;

            double trad = GeoConstant.theta(tx, ty);
            double brad = GeoConstant.theta(bx, by);
            if (trad >= brad) {
                dm.set(brad, trad, include);
            } else {
                dm.set(brad, 2 * Math.PI, include);
                dm.set(0, trad, include);
            }
        }

        public boolean check(Point2[] red, Point2[] blue, Point2 center, boolean isRedCenter, double r) {
            DoubleIntervalMap dm = new DoubleIntervalMap();
            if (isRedCenter) {
                dm.setTrue(0, 2 * Math.PI);
            } else {
                for (Point2 pt : red) {
                    radian(dm, Point2.minus(pt, center), r, true);
                }
            }

            for (Point2 pt : blue) {
                radian(dm, Point2.minus(pt, center), r, false);
            }

            return dm.firstTrue(0) != null;
        }

    }

    static class RandomWrapper {
        private Random random;
        public static final RandomWrapper INSTANCE = new RandomWrapper(new Random(0));

        public RandomWrapper() {
            this(new Random());
        }

        public RandomWrapper(Random random) {
            this.random = random;
        }

        public int nextInt(int l, int r) {
            return random.nextInt(r - l + 1) + l;
        }

    }

    static class Point2 implements Cloneable {
        public static final Comparator<Point2> SORT_BY_XY = (a, b) ->
        {
            int ans = GeoConstant.compare(a.x, b.x);
            if (ans == 0) {
                ans = GeoConstant.compare(a.y, b.y);
            }
            return ans;
        };
        public static final Point2 ORIGIN = new Point2(0, 0);
        public final double x;
        public final double y;

        public static Comparator<Point2> sortByPolarAngleAround(Point2 center) {
            return (a, b) -> {
                int aHalf = half(a.x - center.x, a.y - center.y);
                int bHalf = half(b.x - center.x, b.y - center.y);
                if (aHalf != bHalf) {
                    return aHalf - bHalf;
                }
                return orient(center, b, a);
            };
        }

        public Point2(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point2() {
            this(0, 0);
        }

        private static int half(double x, double y) {
            return y > 0 || y == 0 && x < 0 ? 1 : 0;
        }

        public double square() {
            return dot(this, this);
        }

        public double abs() {
            return Math.sqrt(square());
        }

        public static Point2 minus(Point2 a, Point2 b) {
            return new Point2(a.x - b.x, a.y - b.y);
        }

        public static double dot(Point2 a, Point2 b) {
            return a.x * b.x + a.y * b.y;
        }

        public static double dot(double x1, double y1, double x2, double y2) {
            return x1 * x2 + y1 * y2;
        }

        public static double cross(Point2 a, Point2 b, Point2 c) {
            return GeoConstant.cross(b.x - a.x, b.y - a.y,
                    c.x - a.x, c.y - a.y);
        }

        public static int orient(Point2 a, Point2 b, Point2 c) {
            return GeoConstant.sign(GeoConstant.cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y));
        }

        public static boolean inDisk(Point2 a, Point2 b, Point2 c) {
            return GeoConstant.sign(dot(a.x - c.x, a.y - c.y, b.x - c.x, b.y - c.y)) <= 0;
        }

        public static boolean onSegment(Point2 a, Point2 b, Point2 c) {
            return orient(a, b, c) == 0 && inDisk(a, b, c);
        }

        public static double dist2(Point2 a, Point2 b) {
            double dx = a.x - b.x;
            double dy = a.y - b.y;
            return dx * dx + dy * dy;
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

    static class GeometryUtils {
        public static final double PREC = 1e-15;

        public static double valueOf(double x) {
            return x > -PREC && x < PREC ? 0 : x;
        }

    }

    static class KahanSummation {
        private double error;
        private double sum;

        public void add(double x) {
            x = x - error;
            double t = sum + x;
            error = (t - sum) - x;
            sum = t;
        }

        public void subtract(double x) {
            add(-x);
        }

        public String toString() {
            return new BigDecimal(sum).toString();
        }

    }

    static abstract class DoubleBinarySearch {
        private final double relativeErrorTolerance;
        private final double absoluteErrorTolerance;

        public DoubleBinarySearch(double relativeErrorTolerance, double absoluteErrorTolerance) {
            this.relativeErrorTolerance = relativeErrorTolerance;
            this.absoluteErrorTolerance = absoluteErrorTolerance;
        }

        public abstract boolean check(double mid);

        public double binarySearch(double l, double r) {
            if (l > r) {
                throw new IllegalArgumentException();
            }
            while (r - l > absoluteErrorTolerance) {
                if ((r < 0 && (r - l) < -r * relativeErrorTolerance) || (l > 0 && (r - l) < l * relativeErrorTolerance)) {
                    break;
                }

                double mid = (l + r) / 2;
                if (check(mid)) {
                    r = mid;
                } else {
                    l = mid;
                }
            }
            return (l + r) / 2;
        }

    }

    static class ConvexHull2 {
        public static Collection<Point2> grahamScan(List<Point2> pointPolygon) {
            final Point2[] points = pointPolygon.toArray(new Point2[0]);
            int n = points.length;
            for (int i = 1; i < n; i++) {
                int cmp = Point2.SORT_BY_XY.compare(points[i], points[0]);
                if (cmp >= 0) {
                    continue;
                }
                SequenceUtils.swap(points, 0, i);
            }


            Comparator<Point2> cmp = Point2.sortByPolarAngleAround(points[0]);
            Arrays.sort(points, 1, n, cmp);

            int shrinkSize = 2;
            for (int i = 2; i < n; i++) {
                if (cmp.compare(points[i], points[shrinkSize - 1]) == 0) {
                    if (Point2.dist2(points[i], points[0]) > Point2.dist2(points[shrinkSize - 1], points[0])) {
                        points[shrinkSize - 1] = points[i];
                    }
                } else {
                    points[shrinkSize++] = points[i];
                }
            }

            n = shrinkSize;
            Deque<Point2> stack = new ArrayDeque<>(n);
            stack.addLast(points[0]);
            for (int i = 1; i < n; i++) {
                while (stack.size() >= 2) {
                    Point2 last = stack.removeLast();
                    Point2 second = stack.peekLast();
                    if (GeometryUtils.valueOf(Point2.cross(second, points[i], last)) < 0) {
                        stack.addLast(last);
                        break;
                    }
                }
                stack.addLast(points[i]);
            }

            return stack;
        }

    }

    static class SequenceUtils {
        public static <T> void swap(T[] data, int i, int j) {
            T tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, double x) {
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

    static class Randomized {
        public static <T> void shuffle(T[] data) {
            shuffle(data, 0, data.length - 1);
        }

        public static <T> void shuffle(T[] data, int from, int to) {
            to--;
            for (int i = from; i <= to; i++) {
                int s = nextInt(i, to);
                T tmp = data[i];
                data[i] = data[s];
                data[s] = tmp;
            }
        }

        public static int nextInt(int l, int r) {
            return RandomWrapper.INSTANCE.nextInt(l, r);
        }

    }

    static class DoubleIntervalMap implements Iterable<DoubleIntervalMap.Interval> {
        private TreeMap<Double, DoubleIntervalMap.Interval> map = new TreeMap<>();
        private KahanSummation total = new KahanSummation();

        private void removeInterval(DoubleIntervalMap.Interval interval) {
            map.remove(interval.l);
            total.subtract(interval.length());
        }

        private void addInterval(DoubleIntervalMap.Interval interval) {
            if (interval.length() <= 0) {
                return;
            }
            map.put(interval.l, interval);
            total.add(interval.length());
        }

        public Double firstTrue(double l) {
            Map.Entry<Double, DoubleIntervalMap.Interval> entry = map.floorEntry(l);
            if (entry != null && entry.getValue().r >= l) {
                return Math.max(l, entry.getValue().l);
            }
            entry = map.ceilingEntry(l);
            if (entry != null) {
                return entry.getKey();
            }
            return null;
        }

        public void set(double l, double r, boolean v) {
            if (v) {
                setTrue(l, r);
            } else {
                setFalse(l, r);
            }
        }

        public void setTrue(double l, double r) {
            if (r <= l) {
                return;
            }
            DoubleIntervalMap.Interval interval = new DoubleIntervalMap.Interval();
            interval.l = l;
            interval.r = r;
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> floorEntry = map.floorEntry(interval.l);
                if (floorEntry == null) {
                    break;
                }
                DoubleIntervalMap.Interval floorInterval = floorEntry.getValue();
                if (floorInterval.r >= interval.r) {
                    return;
                } else if (floorInterval.r < interval.l) {
                    break;
                } else {
                    interval.l = Math.min(interval.l, floorInterval.l);
                    removeInterval(floorInterval);
                }
            }
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> ceilEntry = map.ceilingEntry(interval.l);
                if (ceilEntry == null) {
                    break;
                }
                DoubleIntervalMap.Interval ceilInterval = ceilEntry.getValue();
                if (ceilInterval.l <= interval.l) {
                    return;
                } else if (ceilInterval.l > interval.r) {
                    break;
                } else {
                    interval.r = Math.max(interval.r, ceilInterval.r);
                    removeInterval(ceilInterval);
                }
            }

            addInterval(interval);
        }

        public void setFalse(double l, double r) {
            if (r <= l) {
                return;
            }
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> floorEntry = map.floorEntry(l);
                if (floorEntry == null) {
                    break;
                }
                DoubleIntervalMap.Interval floorInterval = floorEntry.getValue();
                if (floorInterval.r < l) {
                    break;
                } else if (floorInterval.r > r) {
                    removeInterval(floorInterval);
                    DoubleIntervalMap.Interval lPart = floorInterval;
                    DoubleIntervalMap.Interval rPart = new DoubleIntervalMap.Interval();
                    rPart.l = r;
                    rPart.r = floorInterval.r;
                    lPart.r = l;
                    addInterval(lPart);
                    addInterval(rPart);
                    return;
                } else if (floorInterval.l >= l) {
                    removeInterval(floorInterval);
                } else {
                    removeInterval(floorInterval);
                    floorInterval.r = l;
                    addInterval(floorInterval);
                    break;
                }
            }
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> ceilEntry = map.ceilingEntry(l);
                if (ceilEntry == null) {
                    break;
                }
                DoubleIntervalMap.Interval ceilInterval = ceilEntry.getValue();
                if (ceilInterval.l > r) {
                    break;
                } else if (ceilInterval.l < l) {
                    removeInterval(ceilInterval);
                    DoubleIntervalMap.Interval lPart = new DoubleIntervalMap.Interval();
                    DoubleIntervalMap.Interval rPart = ceilInterval;
                    lPart.l = ceilInterval.l;
                    lPart.r = l;
                    rPart.l = r;
                    addInterval(lPart);
                    addInterval(rPart);
                    return;
                } else if (ceilInterval.r <= r) {
                    removeInterval(ceilInterval);
                } else {
                    removeInterval(ceilInterval);
                    ceilInterval.l = r;
                    addInterval(ceilInterval);
                    break;
                }
            }
        }

        public Iterator<DoubleIntervalMap.Interval> iterator() {
            return map.values().iterator();
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (DoubleIntervalMap.Interval interval : map.values()) {
                builder.append(interval).append(',');
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }

        public static class Interval {
            public double l;
            public double r;

            public double length() {
                return r - l;
            }

            public String toString() {
                return String.format("[%f, %f]", l, r);
            }

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

    static class GeoConstant {
        public static final double PREC = 1e-15;

        public static boolean isZero(double x) {
            return -PREC <= x && x <= PREC;
        }

        public static int sign(double x) {
            return isZero(x) ? 0 : x < 0 ? -1 : 1;
        }

        public static int compare(double a, double b) {
            return sign(a - b);
        }

        public static double cross(double x1, double y1, double x2, double y2) {
            return x1 * y2 - y1 * x2;
        }

        public static double theta(double x, double y) {
            double theta = Math.atan2(y, x);
            if (theta < 0) {
                theta += Math.PI * 2;
            }
            return theta;
        }

    }
}

