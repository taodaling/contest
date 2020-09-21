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
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.AbstractMap;
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
        double prec = 1e-12;
        CircleCoverDetect ccd = new CircleCoverDetect(prec);
        DoubleIntervalMap dm = new DoubleIntervalMap();

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

            List<Point2> pts = new ArrayList<>();
            pts.addAll(Arrays.asList(red));
            pts.addAll(Arrays.asList(blue));
            List<Point2> convex = new ArrayList<>(ConvexHull2.grahamScan(pts));
            for (int i = 0; i < convex.size(); i++) {
                Point2 a = convex.get(i);
                Point2 b = convex.get((i + 1) % convex.size());
                for (Point2 r : red) {
                    if (Point2.onSegment(a, b, r)) {
                        out.println(-1);
                        return;
                    }
                }
            }

            Randomized.shuffle(red);
            Randomized.shuffle(blue);

            double ans = prec;
            for (Point2 x : red) {
                DoubleFixRoundBinarySearch dbs = new DoubleFixRoundBinarySearch(100) {

                    public boolean check(double mid) {
                        return !checkRed(red, blue, x, mid);
                    }
                };
                if (dbs.check(ans)) {
                    continue;
                }
                double local = dbs.binarySearch(ans, 1e20);
                ans = Math.max(ans, local);
            }
            for (Point2 x : blue) {
                DoubleFixRoundBinarySearch dbs = new DoubleFixRoundBinarySearch(100) {

                    public boolean check(double mid) {
                        return !checkBlue(red, blue, x, mid);
                    }
                };
                if (dbs.check(ans)) {
                    continue;
                }
                double local = dbs.binarySearch(ans, 1e20);
                ans = Math.max(ans, local);
            }

            out.println(ans);
        }

        public boolean check(Point2[] red, Point2[] blue, Point2 center, double r, boolean skip) {
            ccd.reset(center, r, (a, b) -> dm.add(a, b));
            if (!skip)
                for (Point2 b : red) {
                    ccd.addCircle(b, r);
                }
            ccd.reset(center, r, (a, b) -> dm.remove(a, b));
            for (Point2 b : blue) {
                ccd.addCircle(b, r);
            }
            return !dm.isEmpty();
        }

        public boolean checkBlue(Point2[] red, Point2[] blue, Point2 center, double r) {
            dm.clear();
            return check(red, blue, center, r, false);
        }

        public boolean checkRed(Point2[] red, Point2[] blue, Point2 center, double r) {
            dm.clear();
            dm.add(0, Math.PI * 2);
            return check(red, blue, center, r, true);
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

    static interface DoubleBinaryConsumer {
        void accept(double a, double b);

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

        public static double dist(Point2 a, Point2 b) {
            return Math.sqrt(dist2(a, b));
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
        public static final double PREC = 1e-12;

        public static double valueOf(double x) {
            return x > -PREC && x < PREC ? 0 : x;
        }

    }

    static class RandomWrapper {
        private Random random;
        public static final RandomWrapper INSTANCE = new RandomWrapper(new Random());

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

    static class DoubleIntervalMap implements Iterable<DoubleIntervalMap.Interval> {
        private double total = 0;
        private TreeMap<Double, DoubleIntervalMap.Interval> map = new TreeMap<>();

        private void add(DoubleIntervalMap.Interval interval) {
            if (interval.length() <= 0) {
                return;
            }
            map.put(interval.l, interval);
            total += interval.length();
        }

        private void remove(DoubleIntervalMap.Interval interval) {
            map.remove(interval.l);
            total -= interval.length();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public Iterator<DoubleIntervalMap.Interval> iterator() {
            return map.values().iterator();
        }

        public void add(double l, double r) {
            if (l >= r) {
                return;
            }
            DoubleIntervalMap.Interval interval = new DoubleIntervalMap.Interval();
            interval.l = l;
            interval.r = r;
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> ceilEntry = map.ceilingEntry(interval.l);
                if (ceilEntry == null || ceilEntry.getValue().l > interval.r) {
                    break;
                }
                DoubleIntervalMap.Interval ceil = ceilEntry.getValue();
                remove(ceil);
                interval.r = Math.max(interval.r, ceil.r);
            }
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> floorEntry = map.floorEntry(interval.l);
                if (floorEntry == null || floorEntry.getValue().r < interval.l) {
                    break;
                }
                DoubleIntervalMap.Interval floor = floorEntry.getValue();
                remove(floor);
                interval.l = Math.min(interval.l, floor.l);
                interval.r = Math.max(interval.r, floor.r);
            }
            add(interval);
        }

        public void remove(double l, double r) {
            if (l >= r) {
                return;
            }
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> ceilEntry = map.ceilingEntry(l);
                if (ceilEntry == null || ceilEntry.getValue().l >= r) {
                    break;
                }
                DoubleIntervalMap.Interval ceil = ceilEntry.getValue();
                remove(ceil);
                ceil.l = r;
                add(ceil);
            }
            while (true) {
                Map.Entry<Double, DoubleIntervalMap.Interval> floorEntry = map.floorEntry(l);
                if (floorEntry == null || floorEntry.getValue().r <= l) {
                    break;
                }
                DoubleIntervalMap.Interval floor = floorEntry.getValue();
                remove(floor);
                if (floor.r > r) {
                    DoubleIntervalMap.Interval left = floor;
                    DoubleIntervalMap.Interval right = new DoubleIntervalMap.Interval();
                    right.l = r;
                    right.r = left.r;
                    left.r = l;
                    add(left);
                    add(right);
                    break;
                }
                floor.r = l;
                add(floor);
            }
        }

        public String toString() {
            return map.values().toString();
        }

        public void clear() {
            map.clear();
            total = 0;
        }

        public static class Interval {
            public double l;
            public double r;

            public double length() {
                return r - l;
            }

            public String toString() {
                return "[" + l + "," + r + ")";
            }

        }

    }

    static class CircleCoverDetect {
        private Point2 center;
        private double cr;
        private double eps;
        private static double max = 2 * Math.PI;
        private DoubleBinaryConsumer consumer;

        public CircleCoverDetect(double eps) {
            this.eps = eps;
        }

        public void reset(Point2 center, double r, DoubleBinaryConsumer consumer) {
            this.center = center;
            this.cr = r;
            this.consumer = consumer;
        }

        private void add(double l, double r) {
            consumer.accept(l, r);
        }

        private void addForbidden(double theta, double angle) {
            double l = theta - angle;
            double r = theta + angle;

            if (l < 0) {
                add(max + l, max);
                l = 0;
            }
            if (r > max) {
                add(0, r - max);
                r = max;
            }
            add(l, r);
        }

        public void addCircle(Point2 pt, double r) {
            double dist = Point2.dist(center, pt);
            if (dist + eps >= r + cr) {
                return;
            }
            //no intersection case
            if (dist - eps <= cr - r) {
                return;
            }
            if (dist - eps <= r - cr) {
                add(0, Math.PI * 2);
                return;
            }

            double theta = GeoConstant.theta(pt.y - center.y, pt.x - center.x);
            double angle = GeoConstant.triangleAngle(r, cr, dist);
            addForbidden(theta, angle);
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

    static class GeoConstant {
        public static final double PREC = 1e-12;

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

        public static double triangleAngle(double a, double b, double c) {
            double cosa = (b * b + c * c - a * a) / (2 * b * c);
            return Math.acos(cosa);
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

    static abstract class DoubleFixRoundBinarySearch {
        private int round;

        public DoubleFixRoundBinarySearch(int round) {
            this.round = round;
        }

        public abstract boolean check(double mid);

        public double binarySearch(double l, double r) {
            if (l > r) {
                throw new IllegalArgumentException();
            }
            int remain = round;
            while (remain-- > 0) {
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

    static class SequenceUtils {
        public static <T> void swap(T[] data, int i, int j) {
            T tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

    }
}

