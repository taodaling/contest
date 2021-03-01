package template.geometry.geo2;

import template.utils.GeoConstant;
import template.math.DigitUtils;
import template.utils.CompareUtils;

import java.util.Arrays;
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
    public static final Comparator<IntegerPoint2> SORT_BY_XY = (a, b) ->
    {
        int ans = Long.compare(a.x, b.x);
        if (ans == 0) {
            ans = Long.compare(a.y, b.y);
        }
        return ans;
    };

    public static Comparator<IntegerPoint2> sortByPolarAngleAround(IntegerPoint2 center) {
        return (a, b) -> {
            int aHalf = half(a.x - center.x, a.y - center.y);
            int bHalf = half(b.x - center.x, b.y - center.y);
            if (aHalf != bHalf) {
                return aHalf - bHalf;
            }
            return orient(center, b, a);
        };
    }

    public static final IntegerPoint2 ORIGIN = new IntegerPoint2(0, 0);

    public final long x, y;

    public IntegerPoint2(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public IntegerPoint2() {
        this(0, 0);
    }

    public double arg() {
        return Math.atan2(y, x);
    }

    /**
     * (0, PI] for upper half return 1, (-PI, 0] for bottom half return 0
     */
    public int half() {
        return half(x, y);
    }

    private static int half(double x, double y) {
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


    public static IntegerPoint2 mul(IntegerPoint2 a, IntegerPoint2 b) {
        return new IntegerPoint2(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
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
        return Long.signum(cross(b, c));
    }

    public static int orient(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        return Long.signum(cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y));
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

    /**
     * 判断c是否落在以a与b为直径两端的圆中（包含边界）
     */
    public static boolean inDisk(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c) {
        return Long.signum(dot(a.x - c.x, a.y - c.y, b.x - c.x, b.y - c.y)) <= 0;
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
    public static boolean properIntersect(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c, IntegerPoint2 d) {
        long oa = cross(c, d, a);
        long ob = cross(c, d, b);
        long oc = cross(a, b, c);
        long od = cross(a, b, d);

        if (oa * ob < 0 && oc * od < 0) {
            return true;
        }
        return false;
    }

    /**
     * 计算多边形面积（如果以逆时针给定顶点，则结果符号为正数，否则为负数）
     */
    public static long area2(IntegerPoint2[] polygon) {
        int n = polygon.length;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += cross(polygon[i], polygon[(i + 1) % n]);
        }
        return sum;
    }

    public static boolean intersect(IntegerPoint2 a, IntegerPoint2 b, IntegerPoint2 c, IntegerPoint2 d) {
        return properIntersect(a, b, c, d) || onSegment(a, b, c) || onSegment(a, b, d) || onSegment(c, d, a) || onSegment(c, d, b);
    }

    private static int above(IntegerPoint2 a, IntegerPoint2 b) {
        return b.y >= a.y ? 1 : 0;
    }

    private static boolean crossRay(IntegerPoint2 a, IntegerPoint2 p, IntegerPoint2 q) {
        return (above(a, q) - above(a, p)) * orient(a, p, q) > 0;
    }

    /**
     * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘，0表示矩形外，顶点逆时针放置
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

    public static IntegerPoint2[] theNearestPointPair(List<IntegerPoint2> pts) {
        IntegerPoint2[] ptsSortByX = pts.toArray(new IntegerPoint2[0]);
        IntegerPoint2[] buf = new IntegerPoint2[ptsSortByX.length];
        Arrays.sort(ptsSortByX, (a, b) -> Long.compare(a.x, b.x));
        return findTheNearestPointPair(ptsSortByX, buf, 0, ptsSortByX.length - 1);
    }

    private static long pow2(long x){
        return x * x;
    }

    private static IntegerPoint2[] findTheNearestPointPair(IntegerPoint2[] pts, IntegerPoint2[] buf, int l, int r) {
        if (l >= r) {
            return null;
        }
        int m = (l + r) / 2;
        long lMaxX = pts[m].x;
        long rMinX = pts[m + 1].x;

        IntegerPoint2[] lAns = findTheNearestPointPair(pts, buf, l, m);
        IntegerPoint2[] rAns = findTheNearestPointPair(pts, buf, m + 1, r);
        long inf = (long)8e18;
        long lDist = lAns == null ? inf : IntegerPoint2.dist2(lAns[0], lAns[1]);
        long rDist = rAns == null ? inf : IntegerPoint2.dist2(rAns[0], rAns[1]);
        IntegerPoint2[] ans;
        long farthest;

        if (lDist <= rDist) {
            ans = lAns;
            farthest = lDist;
        } else {
            ans = rAns;
            farthest = rDist;
        }
        if (ans == null) {
            ans = new IntegerPoint2[2];
        }

        //copy to buf
        int lr = l - 1;
        int rr = m;
        for (int i = l; i <= m; i++) {
            if (pow2(rMinX - pts[i].x) >= farthest) {
                continue;
            }
            buf[++lr] = pts[i];
        }
        for (int i = m + 1; i <= r; i++) {
            if (pow2(pts[i].x - lMaxX) >= farthest) {
                continue;
            }
            buf[++rr] = pts[i];
        }

        //merge
        int intervalL = m + 1;
        int intervalR = m;

        for (int i = l; i <= lr; i++) {
            while (intervalR + 1 <= rr && (buf[intervalR + 1].y <= buf[i].y || pow2(buf[intervalR + 1].y - buf[i].y) <= farthest)) {
                intervalR++;
            }
            while (intervalL <= rr && buf[i].y >= buf[intervalL].y && pow2(buf[i].y - buf[intervalL].y) >= farthest) {
                intervalL++;
            }
            if (intervalR - intervalL + 1 > 6) {
                throw new RuntimeException();
            }
            for (int j = intervalL; j <= intervalR; j++) {
                long d2 = dist2(buf[i], buf[j]);
                if (d2 < farthest) {
                    farthest = d2;
                    ans[0] = buf[i];
                    ans[1] = buf[j];
                }
            }
        }

        //merge sort
        CompareUtils.<IntegerPoint2>mergeAscending(pts, l, m, pts, m + 1, r, buf, l, (a, b) -> Long.compare(a.y, b.y));
        System.arraycopy(buf, l, pts, l, r - l + 1);
        return ans;
    }
}
