package template.geometry.geo2;

import template.geometry.GeoConstant;
import template.math.DigitUtils;

import java.util.Comparator;
import java.util.List;

public class IntegerPoint2 {
    public static final Comparator<IntegerPoint2> SORT_BY_POLAR_ANGLE = (a, b) ->
    {
        if (a.half() != b.half()) {
            return a.half() - b.half();
        }
        return orient(b, a);
    };
    public static final IntegerPoint2 ORIGIN = new IntegerPoint2(0, 0);

    public final long x, y;

    public IntegerPoint2(long x, long y) {
        this.x = x;
        this.y = y;
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

    public IntegerPoint2 conj() {
        return new IntegerPoint2(x, -y);
    }

    public IntegerPoint2 perpendicular() {
        return new IntegerPoint2(-y, x);
    }

    public static IntegerPoint2 plus(IntegerPoint2 a, IntegerPoint2 b) {
        return new IntegerPoint2(a.x + b.x, a.y + b.y);
    }

    public static IntegerPoint2 minus(IntegerPoint2 a, IntegerPoint2 b) {
        return new IntegerPoint2(a.x - b.x, a.y - b.y);
    }

    public static IntegerPoint2 mul(IntegerPoint2 a, long d) {
        return new IntegerPoint2(a.x * d, a.y * d);
    }

    public static IntegerPoint2 div(IntegerPoint2 a, long d) {
        return new IntegerPoint2(a.x / d, a.y / d);
    }

    public static IntegerPoint2 mul(IntegerPoint2 a, IntegerPoint2 b) {
        return new IntegerPoint2(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
    }

    public static IntegerPoint2 div(IntegerPoint2 a, IntegerPoint2 b) {
        return div(mul(a, b.conj()), b.square());
    }

    public static long dot(IntegerPoint2 a, IntegerPoint2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static long dot(long x1, long y1, long x2, long y2) {
        return x1 * x2 + y1 * y2;
    }

    public static long cross(IntegerPoint2 a, IntegerPoint2 b) {
        return a.x * b.y - a.y * b.x;
    }

    public static long cross(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        return cross(b.x - a.x, b.y - a.y,
                c.x - a.x, c.y - a.y);
    }

    private static long cross(long x1, long y1, long x2, long y2) {
        return x1 * y2 - y1 * x2;
    }

    public static boolean isPerpendicular(IntegerPoint2 a, IntegerPoint2 b) {
        return GeoConstant.isZero(dot(a, b));
    }

    public static boolean isParallel(IntegerPoint2 a, IntegerPoint2 b) {
        return GeoConstant.isZero(cross(a, b));
    }

    /**
     * 获得[0,pi) angle
     */
    public static double angle(IntegerPoint2 a, IntegerPoint2 b) {
        return Math.acos(DigitUtils.clamp(dot(a, b) / a.abs() / b.abs(), -1.0, 1.0));
    }

    public static int orient(IntegerPoint2 b, IntegerPoint2 c) {
        return GeoConstant.sign(cross(b, c));
    }

    public static int orient(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        return GeoConstant.sign(cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y));
    }

    public static boolean inAngle(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c,
                                  IntegerPoint2 p) {
        if (orient(a, b, c) < 0) {
            IntegerPoint2 tmp = b;
            b = c;
            c = tmp;
        }
        return orient(a, b, p) >= 0 && orient(a, c, p) <= 0;
    }

    public static double orientedAngle(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        double angle = angle(minus(b, a), minus(c, a));
        if (orient(a, b, c) >= 0) {
            return angle;
        } else {
            return 2 * Math.PI - angle;
        }
    }

    public static boolean isConvex(List<IntegerPoint2> p) {
        boolean hasPos = false, hasNeg = false;
        for (int i = 0, n = p.size(); i < n; i++) {
            int o = orient(p.get(i), p.get((i + 1) % n), p.get((i + 2) % n));
            if (o > 0) hasPos = true;
            if (o < 0) hasNeg = true;
        }
        return !(hasPos & hasNeg);
    }

    public static IntegerPoint2 translate(IntegerPoint2 pt, IntegerPoint2 vec) {
        return plus(pt, vec);
    }

    public static IntegerPoint2 scale(IntegerPoint2 pt, long d) {
        return mul(pt, d);
    }

    public static IntegerPoint2 scale(IntegerPoint2 origin, IntegerPoint2 pt, long d) {
        return plus(origin, mul(minus(pt, origin), d));
    }

    public static IntegerPoint2 linearTransform(IntegerPoint2 p, IntegerPoint2 fp, IntegerPoint2 q, IntegerPoint2 fq, IntegerPoint2 r) {
        return plus(fp, mul(minus(r, p), div(minus(fq, fp), minus(q, p))));
    }

    /**
     * 判断c是否落在以a与b为直径两端的圆中（包含边界）
     */
    public static boolean inDisk(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        return GeoConstant.sign(dot(a.x - c.x, a.y - c.y, b.x - c.x, b.y - c.y)) <= 0;
    }

    /**
     * 判断c是否在a到b的线段上
     */
    public static boolean onSegment(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        return orient(a, b, c) == 0 && inDisk(a, b, c);
    }

    /**
     * 获取线段a->b与线段c->d的交点
     */
    public static IntegerPoint2 properIntersect(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c, IntegerPoint2 d) {
        long oa = cross(c, d, a);
        long ob = cross(c, d, b);
        long oc = cross(a, b, c);
        long od = cross(a, b, d);

        if (oa * ob < 0 && oc * od < 0) {
            return plus(mul(a, ob / (ob - oa)), mul(b, -oa / (ob - oa)));
        }
        return null;
    }

    public static IntegerPoint2 intersect(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c, IntegerPoint2 d) {
        IntegerPoint2 pt = properIntersect(a, b, c, d);
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

    private static int above(IntegerPoint2 a, IntegerPoint2 b) {
        return b.y >= a.y ? 1 : 0;
    }

    private static boolean crossRay(IntegerPoint2 a, IntegerPoint2 p, IntegerPoint2 q) {
        return (above(a, q) - above(a, p)) * orient(a, p, q) > 0;
    }

    /**
     * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘，0表示矩形外
     */
    public static int inPolygon(List<IntegerPoint2> polygon, IntegerPoint2 pt) {
        int cross = 0;
        for (int i = 0, n = polygon.size(); i < n; i++) {
            IntegerPoint2 cur = polygon.get(i);
            IntegerPoint2 next = polygon.get((i + 1) % n);
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
    public static int inPolygonBorder(List<IntegerPoint2[]> polygon, IntegerPoint2 pt) {
        int cross = 0;
        for (IntegerPoint2[] pts : polygon) {
            IntegerPoint2 cur = pts[0];
            IntegerPoint2 next = pts[1];
            if (onSegment(cur, next, pt)) {
                return 2;
            }
            if (crossRay(pt, cur, next)) {
                cross++;
            }
        }
        return cross % 2;
    }

    public static long dist2(IntegerPoint2 a, IntegerPoint2 b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    public static double dist(IntegerPoint2 a, IntegerPoint2 b) {
        return Math.sqrt(dist2(a, b));
    }

    @Override
    public IntegerPoint2 clone() {
        try {
            return (IntegerPoint2) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
