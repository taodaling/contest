import java.util.Iterator;
import java.util.Collection;
import java.util.AbstractMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;

public class EllysIncinerator {
    double eps = 1e-12;

    public double getMax(double[] X, double[] Y, double[] R) {
        int n = X.length;
        double ans = 0;

        Point2[] pts = new Point2[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point2(X[i], Y[i]);
        }
        CircleCoverDetect ccd = new CircleCoverDetect(eps);
        Point2[] corners = new Point2[]{
                new Point2(0, 0),
                new Point2(1000, 0),
                new Point2(1000, 1000),
                new Point2(0, 1000)
        };

        Line2[] lines = new Line2[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = new Line2(corners[i], corners[(i + 1) % 4]);
        }


        for (int i = 0; i < n; i++) {
            int t = i;
            DoubleBinarySearch dbs = new DoubleBinarySearch(eps, eps) {

                public boolean check(double mid) {
                    ccd.reset(pts[t], R[t] + mid);
                    for (Line2 line : lines) {
                        ccd.addLine(line);
                    }
                    for (int i = 0; i < n; i++) {
                        if (i == t) {
                            continue;
                        }
                        ccd.addCircle(pts[i], R[i] + mid);
                    }
                    return ccd.findAnyUncoverRadian() < 0;
                }
            };

            double radius = dbs.binarySearch(0, 2000);
            ans = Math.max(ans, radius);
        }
        return ans;
    }

}

class GeometryUtils {
    public static double triangleAngle(double a, double b, double c) {
        double cosa = (b * b + c * c - a * a) / (2 * b * c);
        return Math.acos(cosa);
    }

    public static double theta(double y, double x) {
        double theta = Math.atan2(y, x);
        if (theta < 0) {
            theta += Math.PI * 2;
        }
        return theta;
    }

}

abstract class DoubleBinarySearch {
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

class Point2 implements Cloneable {
    public final double x;
    public final double y;

    public Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2() {
        this(0, 0);
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

    public static double cross(Point2 a, Point2 b) {
        return a.x * b.y - a.y * b.x;
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

class DoubleIntervalMap implements Iterable<DoubleIntervalMap.Interval> {
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

    public void clear() {
        total = 0;
        map.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public DoubleIntervalMap.Interval first() {
        return map.firstEntry().getValue();
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

    public String toString() {
        return map.values().toString();
    }

    public static class Interval {
        public double l;
        public double r;

        public double length() {
            return r - l;
        }

        public String toString() {
            return "(" + l + "," + r + ")";
        }

    }

}

class CircleCoverDetect {
    private DoubleIntervalMap dm = new DoubleIntervalMap();
    private Point2 center;
    private double cr;
    private double eps;
    private static double min = 0;
    private static double max = 2 * Math.PI;

    public CircleCoverDetect(double eps) {
        this.eps = eps;
    }

    public void reset(Point2 center, double r) {
        this.center = center;
        this.cr = r;
        dm.clear();
    }

    private void addForbidden(double theta, double angle) {
        double l = theta - angle;
        double r = theta + angle;

        if (l < min) {
            dm.add(max + l, max);
            l = 0;
        }
        if (r > max) {
            dm.add(0, r - max);
            r = max;
        }
        dm.add(l, r);
    }

    public void addCircle(Point2 pt, double r) {
        double dist = Point2.dist(center, pt);
        if (dist + eps >= r + cr) {
            return;
        }
        //no intersection case
        if (dist + r + eps <= cr) {
            return;
        }
        if (dist + cr + eps <= r) {
            dm.add(0, Math.PI * 2);
            return;
        }

        double theta = GeometryUtils.theta(pt.y - center.y, pt.x - center.x);
        double angle = GeometryUtils.triangleAngle(r, cr, dist);
        addForbidden(theta, angle);
    }

    private double valueOfRadian(double x) {
        while (x > max) {
            x -= Math.PI * 2;
        }
        while (x < min) {
            x += Math.PI * 2;
        }
        return x;
    }

    public void addLine(Line2 line) {
        int side = line.side(center);
        double dist = line.dist(center);
        if (dist + eps >= cr) {
            if (side == -1) {
                dm.add(min, max);
            }
            return;
        }
        double theta = valueOfRadian(GeometryUtils.theta(line.vec.y, line.vec.x) - Math.PI / 2);
        double angle = side == 0 ? Math.PI / 2 : Math.acos(dist / cr);
        if (side == -1) {
            angle = Math.PI - angle;
        }
        addForbidden(theta, angle);
    }

    public double findAnyUncoverRadian() {
        if (dm.isEmpty()) {
            return 0;
        }
        DoubleIntervalMap.Interval first = dm.first();
        if (first.l == min && first.r == max) {
            return -1;
        }
        return first.l == 0 ? first.r : first.l;
    }

    public String toString() {
        return dm.toString();
    }

}

class GeoConstant {
    public static final double PREC = 1e-15;

    public static boolean isZero(double x) {
        return -PREC <= x && x <= PREC;
    }

    public static int sign(double x) {
        return isZero(x) ? 0 : x < 0 ? -1 : 1;
    }

}

class Line2 {
    public Point2 vec;
    public double c;

    public Line2(Point2 vec, double c) {
        this.vec = vec;
        this.c = c;
    }

    public Line2(double a, double b, double c) {
        this(new Point2(b, -a), c);
    }

    public Line2(Point2 a, Point2 b) {
        this.vec = Point2.minus(b, a);
        this.c = Point2.cross(vec, a);
    }

    private double side0(Point2 pt) {
        return Point2.cross(vec, pt) - c;
    }

    public int side(Point2 pt) {
        return GeoConstant.sign(side0(pt));
    }

    public double dist(Point2 pt) {
        return Math.abs(side0(pt)) / vec.abs();
    }

    public String toString() {
        return -vec.y + "x + " + vec.x + "y = " + c;
    }

}
