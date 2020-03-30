package template.geometry.geo2;

import template.math.DigitUtils;

import java.util.Comparator;
import java.util.List;

public class Point implements Cloneable {
    public static final Comparator<Point> SORT_BY_POLAR_ANGLE = (a, b) ->
    {
        if (a.half() != b.half()) {
            return a.half() - b.half();
        }
        return orient(b, a);
    };
    public static final Point ORIGIN = new Point(0, 0);

    public final double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public double arg() {
        return Math.atan2(y, x);
    }

    /**
     * (0, PI] for upper half return 1, (-PI, 0] for bottom half return 0
     */
    public int half() {
        return y > 0 || y == 0 && x < 0 ? 1 : 0;
    }

    public double square() {
        return x * x + y * y;
    }

    public double abs() {
        return Math.sqrt(square());
    }

    public Point norm() {
        return norm(1);
    }

    public Point norm(double d) {
        return mul(this, d / abs());
    }

    public Point conj() {
        return new Point(x, -y);
    }

    public Point perpendicular() {
        return new Point(-y, x);
    }

    public static Point plus(Point a, Point b) {
        return new Point(a.x + b.x, a.y + b.y);
    }

    public static Point minus(Point a, Point b) {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static Point mul(Point a, double d) {
        return new Point(a.x * d, a.y * d);
    }

    public static Point div(Point a, double d) {
        return new Point(a.x / d, a.y / d);
    }

    public static Point mul(Point a, Point b) {
        return new Point(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
    }

    public static Point div(Point a, Point b) {
        return div(mul(a, b.conj()), b.square());
    }

    /**
     * 绕原点逆时针旋转angle角度
     */
    public static Point rotate(Point a, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Point(a.x * c - a.y * s, a.x * s + a.y * c);
    }

    /**
     * a绕origin逆时针旋转angle角度
     */
    public static Point rotate(Point origin, Point a, double angle) {
        return plus(origin, rotate(minus(a, origin), angle));
    }

    public static double dot(Point a, Point b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double dot(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    public static double cross(Point a, Point b) {
        return a.x * b.y - a.y * b.x;
    }

    public static double cross(Point a, Point b, Point c) {
        return cross(b.x - a.x, b.y - a.y,
                c.x - a.x, c.y - a.y);
    }

    private static double cross(double x1, double y1, double x2, double y2) {
        return x1 * y2 - y1 * x2;
    }

    public static boolean isPerpendicular(Point a, Point b) {
        return Geo2Constant.isZero(dot(a, b));
    }

    public static boolean isParallel(Point a, Point b) {
        return Geo2Constant.isZero(cross(a, b));
    }

    /**
     * 获得[0,pi) angle
     */
    public static double angle(Point a, Point b) {
        return Math.acos(DigitUtils.clamp(dot(a, b) / a.abs() / b.abs(), -1.0, 1.0));
    }

    public static int orient(Point b, Point c) {
        return Geo2Constant.sign(cross(b, c));
    }

    public static int orient(Point a, Point b, Point c) {
        return Geo2Constant.sign(cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y));
    }

    public static boolean inAngle(Point a, Point b, Point c,
                                  Point p) {
        if (orient(a, b, c) < 0) {
            Point tmp = b;
            b = c;
            c = tmp;
        }
        return orient(a, b, p) >= 0 && orient(a, c, p) <= 0;
    }

    public static double orientedAngle(Point a, Point b, Point c) {
        double angle = angle(minus(b, a), minus(c, a));
        if (orient(a, b, c) >= 0) {
            return angle;
        } else {
            return 2 * Math.PI - angle;
        }
    }

    public static boolean isConvex(List<Point> p) {
        boolean hasPos = false, hasNeg = false;
        for (int i = 0, n = p.size(); i < n; i++) {
            int o = orient(p.get(i), p.get((i + 1) % n), p.get((i + 2) % n));
            if (o > 0) hasPos = true;
            if (o < 0) hasNeg = true;
        }
        return !(hasPos & hasNeg);
    }

    public static Point translate(Point pt, Point vec) {
        return plus(pt, vec);
    }

    public static Point scale(Point pt, double d) {
        return mul(pt, d);
    }

    public static Point scale(Point origin, Point pt, double d) {
        return plus(origin, mul(minus(pt, origin), d));
    }

    public static Point linearTransform(Point p, Point fp, Point q, Point fq, Point r) {
        return plus(fp, mul(minus(r, p), div(minus(fq, fp), minus(q, p))));
    }

    /**
     * 判断c是否落在以a与b为直径两端的圆中（包含边界）
     */
    public static boolean inDisk(Point a, Point b, Point c) {
        return Geo2Constant.sign(dot(a.x - c.x, a.y - c.y, b.x - c.x, b.y - c.y)) <= 0;
    }

    /**
     * 判断c是否在a到b的线段上
     */
    public static boolean onSegment(Point a, Point b, Point c) {
        return orient(a, b, c) == 0 && inDisk(a, b, c);
    }

    /**
     * 获取线段a->b与线段c->d的交点
     */
    public static Point properIntersect(Point a, Point b, Point c, Point d) {
        double oa = cross(c, d, a);
        double ob = cross(c, d, b);
        double oc = cross(a, b, c);
        double od = cross(a, b, d);

        if (oa * ob < 0 && oc * od < 0) {
            return plus(mul(a, ob / (ob - oa)), mul(b, -oa / (ob - oa)));
        }
        return null;
    }

    public static Point intersect(Point a, Point b, Point c, Point d) {
        Point pt = properIntersect(a, b, c, d);
        if (pt == null && onSegment(a, b, c)) {
            pt = c;
        }
        if (pt == null && onSegment(a, b, d)) {
            pt = d;
        }
        if (pt == null && onSegment(c, d, a)) {
            pt = a;
        }
        if (pt == null && onSegment(c, d, b)) {
            pt = b;
        }
        return pt;
    }

    private static int above(Point a, Point b) {
        return b.y >= a.y ? 1 : 0;
    }

    private static boolean crossRay(Point a, Point p, Point q) {
        return (above(a, q) - above(a, p)) * orient(a, p, q) > 0;
    }

    /**
     * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘，0表示矩形外
     */
    public static int inPolygon(List<Point> polygon, Point pt) {
        int cross = 0;
        for (int i = 0, n = polygon.size(); i < n; i++) {
            Point cur = polygon.get(i);
            Point next = polygon.get((i + 1) % n);
            if (onSegment(cur, next, pt)) {
                return 2;
            }
            if (crossRay(pt, cur, next)) {
                cross++;
            }
        }
        return cross % 2;
    }

    /**
     * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘，0表示矩形外
     */
    public static int inPolygonBorder(List<Point[]> polygon, Point pt) {
        int cross = 0;
        for (Point[] pts : polygon) {
            Point cur = pts[0];
            Point next = pts[1];
            if (onSegment(cur, next, pt)) {
                return 2;
            }
            if (crossRay(pt, cur, next)) {
                cross++;
            }
        }
        return cross % 2;
    }

    /**
     * 计算多边形面积（如果以逆时针给定顶点，则结果符号为正数，否则为负数）
     */
    public static double area(List<Point> polygon) {
        double ans = 0;
        int n = polygon.size();
        for (int i = 0; i < n; i++) {
            ans += cross(polygon.get(i), polygon.get((i + 1) % n));
        }
        return ans / 2;
    }

    public static double dist2(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    public static double dist(Point a, Point b) {
        return Math.sqrt(dist2(a, b));
    }

    @Override
    public Point clone() {
        try {
            return (Point) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f)", x, y);
    }
}
