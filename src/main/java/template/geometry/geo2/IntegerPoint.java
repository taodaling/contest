package template.geometry.geo2;

import template.math.DigitUtils;

import java.util.Comparator;
import java.util.List;

public class IntegerPoint {
    public static final Comparator<IntegerPoint> SORT_BY_POLAR_ANGLE = (a, b) ->
    {
        if (a.half() != b.half()) {
            return a.half() - b.half();
        }
        return orient(b, a);
    };
    public static final IntegerPoint ORIGIN = new IntegerPoint(0, 0);

    public final long x, y;

    public IntegerPoint(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public IntegerPoint() {
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

    public long square() {
        return x * x + y * y;
    }

    public double abs() {
        return Math.sqrt(square());
    }

    public IntegerPoint conj() {
        return new IntegerPoint(x, -y);
    }

    public IntegerPoint perpendicular() {
        return new IntegerPoint(-y, x);
    }

    public static IntegerPoint plus(IntegerPoint a, IntegerPoint b) {
        return new IntegerPoint(a.x + b.x, a.y + b.y);
    }

    public static IntegerPoint minus(IntegerPoint a, IntegerPoint b) {
        return new IntegerPoint(a.x - b.x, a.y - b.y);
    }

    public static IntegerPoint mul(IntegerPoint a, long d) {
        return new IntegerPoint(a.x * d, a.y * d);
    }

    public static IntegerPoint div(IntegerPoint a, long d) {
        return new IntegerPoint(a.x / d, a.y / d);
    }

    public static IntegerPoint mul(IntegerPoint a, IntegerPoint b) {
        return new IntegerPoint(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
    }

    public static IntegerPoint div(IntegerPoint a, IntegerPoint b) {
        return div(mul(a, b.conj()), b.square());
    }

    public static long dot(IntegerPoint a, IntegerPoint b) {
        return a.x * b.x + a.y * b.y;
    }

    public static long dot(long x1, long y1, long x2, long y2) {
        return x1 * x2 + y1 * y2;
    }

    public static long cross(IntegerPoint a, IntegerPoint b) {
        return a.x * b.y - a.y * b.x;
    }

    public static long cross(IntegerPoint a, IntegerPoint b, IntegerPoint c) {
        return cross(b.x - a.x, b.y - a.y,
                c.x - a.x, c.y - a.y);
    }

    private static long cross(long x1, long y1, long x2, long y2) {
        return x1 * y2 - y1 * x2;
    }

    public static boolean isPerpendicular(IntegerPoint a, IntegerPoint b) {
        return Geo2Constant.isZero(dot(a, b));
    }

    public static boolean isParallel(IntegerPoint a, IntegerPoint b) {
        return Geo2Constant.isZero(cross(a, b));
    }

    /**
     * 获得[0,pi) angle
     */
    public static double angle(IntegerPoint a, IntegerPoint b) {
        return Math.acos(DigitUtils.clamp(dot(a, b) / a.abs() / b.abs(), -1.0, 1.0));
    }

    public static int orient(IntegerPoint b, IntegerPoint c) {
        return Geo2Constant.sign(cross(b, c));
    }

    public static int orient(IntegerPoint a, IntegerPoint b, IntegerPoint c) {
        return Geo2Constant.sign(cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y));
    }

    public static boolean inAngle(IntegerPoint a, IntegerPoint b, IntegerPoint c,
                                  IntegerPoint p) {
        if (orient(a, b, c) < 0) {
            IntegerPoint tmp = b;
            b = c;
            c = tmp;
        }
        return orient(a, b, p) >= 0 && orient(a, c, p) <= 0;
    }

    public static double orientedAngle(IntegerPoint a, IntegerPoint b, IntegerPoint c) {
        double angle = angle(minus(b, a), minus(c, a));
        if (orient(a, b, c) >= 0) {
            return angle;
        } else {
            return 2 * Math.PI - angle;
        }
    }

    public static boolean isConvex(List<IntegerPoint> p) {
        boolean hasPos = false, hasNeg = false;
        for (int i = 0, n = p.size(); i < n; i++) {
            int o = orient(p.get(i), p.get((i + 1) % n), p.get((i + 2) % n));
            if (o > 0) hasPos = true;
            if (o < 0) hasNeg = true;
        }
        return !(hasPos & hasNeg);
    }

    public static IntegerPoint translate(IntegerPoint pt, IntegerPoint vec) {
        return plus(pt, vec);
    }

    public static IntegerPoint scale(IntegerPoint pt, long d) {
        return mul(pt, d);
    }

    public static IntegerPoint scale(IntegerPoint origin, IntegerPoint pt, long d) {
        return plus(origin, mul(minus(pt, origin), d));
    }

    public static IntegerPoint linearTransform(IntegerPoint p, IntegerPoint fp, IntegerPoint q, IntegerPoint fq, IntegerPoint r) {
        return plus(fp, mul(minus(r, p), div(minus(fq, fp), minus(q, p))));
    }

    /**
     * 判断c是否落在以a与b为直径两端的圆中（包含边界）
     */
    public static boolean inDisk(IntegerPoint a, IntegerPoint b, IntegerPoint c) {
        return Geo2Constant.sign(dot(a.x - c.x, a.y - c.y, b.x - c.x, b.y - c.y)) <= 0;
    }

    /**
     * 判断c是否在a到b的线段上
     */
    public static boolean onSegment(IntegerPoint a, IntegerPoint b, IntegerPoint c) {
        return orient(a, b, c) == 0 && inDisk(a, b, c);
    }

    /**
     * 获取线段a->b与线段c->d的交点
     */
    public static IntegerPoint properIntersect(IntegerPoint a, IntegerPoint b, IntegerPoint c, IntegerPoint d) {
        long oa = cross(c, d, a);
        long ob = cross(c, d, b);
        long oc = cross(a, b, c);
        long od = cross(a, b, d);

        if (oa * ob < 0 && oc * od < 0) {
            return plus(mul(a, ob / (ob - oa)), mul(b, -oa / (ob - oa)));
        }
        return null;
    }

    public static IntegerPoint intersect(IntegerPoint a, IntegerPoint b, IntegerPoint c, IntegerPoint d) {
        IntegerPoint pt = properIntersect(a, b, c, d);
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

    private static int above(IntegerPoint a, IntegerPoint b) {
        return b.y >= a.y ? 1 : 0;
    }

    private static boolean crossRay(IntegerPoint a, IntegerPoint p, IntegerPoint q) {
        return (above(a, q) - above(a, p)) * orient(a, p, q) > 0;
    }

    /**
     * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘，0表示矩形外
     */
    public static int inPolygon(List<IntegerPoint> polygon, IntegerPoint pt) {
        int cross = 0;
        for (int i = 0, n = polygon.size(); i < n; i++) {
            IntegerPoint cur = polygon.get(i);
            IntegerPoint next = polygon.get((i + 1) % n);
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
    public static int inPolygonBorder(List<IntegerPoint[]> polygon, IntegerPoint pt) {
        int cross = 0;
        for (IntegerPoint[] pts : polygon) {
            IntegerPoint cur = pts[0];
            IntegerPoint next = pts[1];
            if (onSegment(cur, next, pt)) {
                return 2;
            }
            if (crossRay(pt, cur, next)) {
                cross++;
            }
        }
        return cross % 2;
    }

    public static long dist2(IntegerPoint a, IntegerPoint b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    public static double dist(IntegerPoint a, IntegerPoint b) {
        return Math.sqrt(dist2(a, b));
    }

    @Override
    public IntegerPoint clone() {
        try {
            return (IntegerPoint) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
