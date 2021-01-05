package template.geometry.geo2;

import template.geometry.old.GeoConstant;
import template.math.DigitUtils;
import template.math.KahamSummation;
import template.utils.CompareUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Point2 implements Cloneable {
    //按照极角进行排序，极角范围为(-pi,pi]
    public static final Comparator<Point2> SORT_BY_POLAR_ANGLE = (a, b) ->
    {
        if (a.half() != b.half()) {
            return a.half() - b.half();
        }
        return orient(b, a);
    };

    public static final Comparator<Point2> SORT_BY_XY = (a, b) ->
    {
        int ans = GeoConstant.compare(a.x, b.x);
        if (ans == 0) {
            ans = GeoConstant.compare(a.y, b.y);
        }
        return ans;
    };

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

    public static final Point2 ORIGIN = new Point2(0, 0);

    public final double x, y;

    public Point2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2() {
        this(0, 0);
    }

    public double arg() {
        return Math.atan2(y, x);
    }

    public Point2 negate() {
        return new Point2(-x, -y);
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

    public double square() {
        return dot(this, this);
    }

    public double abs() {
        return Math.sqrt(square());
    }

    public Point2 norm() {
        return norm(1);
    }

    public Point2 norm(double d) {
        return mul(this, d / abs());
    }

    public Point2 conj() {
        return new Point2(x, -y);
    }

    public Point2 perpendicular() {
        return new Point2(-y, x);
    }

    public static Point2 plus(Point2 a, Point2 b) {
        return new Point2(a.x + b.x, a.y + b.y);
    }

    public static Point2 minus(Point2 a, Point2 b) {
        return new Point2(a.x - b.x, a.y - b.y);
    }

    public static Point2 mul(Point2 a, double d) {
        return new Point2(a.x * d, a.y * d);
    }

    public static Point2 div(Point2 a, double d) {
        return new Point2(a.x / d, a.y / d);
    }

    public static Point2 mul(Point2 a, Point2 b) {
        return new Point2(a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x);
    }

    public static Point2 div(Point2 a, Point2 b) {
        return div(mul(a, b.conj()), b.square());
    }

    /**
     * 绕原点逆时针旋转angle角度
     */
    public static Point2 rotate(Point2 a, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Point2(a.x * c - a.y * s, a.x * s + a.y * c);
    }

    /**
     * a绕origin逆时针旋转angle角度
     */
    public static Point2 rotate(Point2 origin, Point2 a, double angle) {
        return plus(origin, rotate(minus(a, origin), angle));
    }

    public static double dot(Point2 a, Point2 b) {
        return a.x * b.x + a.y * b.y;
    }


    public static double dot(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    public static double cross(double x1, double y1, double x2, double y2) {
        return x1 * y2 - y1 * x2;
    }

    public static double cross(Point2 a, Point2 b) {
        return a.x * b.y - a.y * b.x;
    }

    public static double cross(Point2 a, Point2 b, Point2 c) {
        return GeoConstant.cross(b.x - a.x, b.y - a.y,
                c.x - a.x, c.y - a.y);
    }


    public static boolean isPerpendicular(Point2 a, Point2 b) {
        return GeoConstant.isZero(dot(a, b));
    }

    public static boolean isParallel(Point2 a, Point2 b) {
        return GeoConstant.isZero(cross(a, b));
    }

    public static Point2 middle(Point2 a, Point2 b) {
        return new Point2((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    /**
     * 获得[0,pi) angle
     */
    public static double angle(Point2 a, Point2 b) {
        return Math.acos(DigitUtils.clamp(dot(a, b) / a.abs() / b.abs(), -1.0, 1.0));
    }

    public static int orient(Point2 b, Point2 c) {
        return GeoConstant.sign(cross(b, c));
    }

    /**
     * 获取c处于ab的方向
     */
    public static int orient(Point2 a, Point2 b, Point2 c) {
        return GeoConstant.sign(GeoConstant.cross(b.x - a.x, b.y - a.y, c.x - a.x, c.y - a.y));
    }

    public static boolean inAngle(Point2 a, Point2 b, Point2 c,
                                  Point2 p) {
        if (orient(a, b, c) < 0) {
            Point2 tmp = b;
            b = c;
            c = tmp;
        }
        return orient(a, b, p) >= 0 && orient(a, c, p) <= 0;
    }

    public static double orientedAngle(Point2 a, Point2 b, Point2 c) {
        double angle = angle(minus(b, a), minus(c, a));
        if (orient(a, b, c) >= 0) {
            return angle;
        } else {
            return 2 * Math.PI - angle;
        }
    }

    public static boolean isConvex(List<Point2> p) {
        boolean hasPos = false;
        boolean hasNeg = false;
        for (int i = 0, n = p.size(); i < n; i++) {
            int o = orient(p.get(i), p.get((i + 1) % n), p.get((i + 2) % n));
            if (o > 0) {
                hasPos = true;
            }
            if (o < 0) {
                hasNeg = true;
            }
        }
        return !(hasPos & hasNeg);
    }

    public static Point2 translate(Point2 pt, Point2 vec) {
        return plus(pt, vec);
    }

    public static Point2 scale(Point2 pt, double d) {
        return mul(pt, d);
    }

    public static Point2 scale(Point2 origin, Point2 pt, double d) {
        return plus(origin, mul(minus(pt, origin), d));
    }

    public static Point2 linearTransform(Point2 p, Point2 fp, Point2 q, Point2 fq, Point2 r) {
        return plus(fp, mul(minus(r, p), div(minus(fq, fp), minus(q, p))));
    }

    /**
     * 判断c是否落在以a与b为直径两端的圆中（包含边界）
     */
    public static boolean inDisk(Point2 a, Point2 b, Point2 c) {
        return GeoConstant.sign(dot(a.x - c.x, a.y - c.y, b.x - c.x, b.y - c.y)) <= 0;
    }

    /**
     * 判断c是否在a到b的线段上
     */
    public static boolean onSegment(Point2 a, Point2 b, Point2 c) {
        return orient(a, b, c) == 0 && inDisk(a, b, c);
    }

    /**
     * 获取线段a->b与线段c->d的交点
     */
    public static Point2 properIntersect(Point2 a, Point2 b, Point2 c, Point2 d) {
        double oa = cross(c, d, a);
        double ob = cross(c, d, b);
        double oc = cross(a, b, c);
        double od = cross(a, b, d);

        if (oa * ob < 0 && oc * od < 0) {
            return plus(mul(a, ob / (ob - oa)), mul(b, -oa / (ob - oa)));
        }
        return null;
    }

    public static Point2 intersect(Point2 a, Point2 b, Point2 c, Point2 d) {
        Point2 pt = properIntersect(a, b, c, d);
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

    private static int above(Point2 a, Point2 b) {
        return b.y >= a.y ? 1 : 0;
    }

    private static boolean crossRay(Point2 a, Point2 p, Point2 q) {
        return (above(a, q) - above(a, p)) * orient(a, p, q) > 0;
    }

    /**
     * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘，0表示矩形外，顶点逆时针放置
     */
    public static int inPolygon(List<Point2> polygon, Point2 pt) {
        int cross = 0;
        for (int i = 0, n = polygon.size(); i < n; i++) {
            Point2 cur = polygon.get(i);
            Point2 next = polygon.get((i + 1) % n);
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
    public static int inPolygonBorder(List<Point2[]> polygon, Point2 pt) {
        int cross = 0;
        for (Point2[] pts : polygon) {
            Point2 cur = pts[0];
            Point2 next = pts[1];
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
    public static double area(Point2[] polygon) {
        KahamSummation sum = new KahamSummation();
        int n = polygon.length;
        for (int i = 0; i < n; i++) {
            sum.add(cross(polygon[i], polygon[(i + 1) % n]));
        }
        return sum.sum() / 2;
    }


    /**
     * Get mirror of a
     */
    public static Point2 mirror(Point2 symmetricCenter, Point2 a) {
        return new Point2(symmetricCenter.x * 2 - a.x, symmetricCenter.y * 2 - a.y);
    }

    public static double dist2(Point2 a, Point2 b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    public static double dist(Point2 a, Point2 b) {
        return Math.sqrt(dist2(a, b));
    }

    @Override
    public Point2 clone() {
        try {
            return (Point2) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("(%.6f, %.6f)", x, y);
    }

    /**
     * distance from c to line (a, b)
     */
    public double distanceToLine(Point2 a, Point2 b, Point2 c) {
        double len = dist(a, b);
        if (len == 0) {
            return dist(a, c);
        }
        double area2 = Math.abs(cross(minus(a, c), minus(b, c)));
        return area2 / len;
    }

    public static Point2[] theNearestPointPair(List<Point2> pts) {
        Point2[] ptsSortByX = pts.toArray(new Point2[0]);
        Point2[] buf = new Point2[ptsSortByX.length];
        Arrays.sort(ptsSortByX, (a, b) -> Double.compare(a.x, b.x));
        return findTheNearestPointPair(ptsSortByX, buf, 0, ptsSortByX.length - 1);
    }

    private static Point2[] findTheNearestPointPair(Point2[] pts, Point2[] buf, int l, int r) {
        if (l >= r) {
            return null;
        }
        int m = (l + r) / 2;
        double lMaxX = pts[m].x;
        double rMinX = pts[m + 1].x;

        Point2[] lAns = findTheNearestPointPair(pts, buf, l, m);
        Point2[] rAns = findTheNearestPointPair(pts, buf, m + 1, r);
        double lDist = lAns == null ? GeoConstant.INF : Point2.dist2(lAns[0], lAns[1]);
        double rDist = rAns == null ? GeoConstant.INF : Point2.dist2(rAns[0], rAns[1]);
        Point2[] ans;
        double farthest;

        if (lDist <= rDist) {
            ans = lAns;
            farthest = lDist;
        } else {
            ans = rAns;
            farthest = rDist;
        }
        if (ans == null) {
            ans = new Point2[2];
        }

        //copy to buf
        int lr = l - 1;
        int rr = m;
        for (int i = l; i <= m; i++) {
            if (GeoConstant.pow2(rMinX - pts[i].x) >= farthest) {
                continue;
            }
            buf[++lr] = pts[i];
        }
        for (int i = m + 1; i <= r; i++) {
            if (GeoConstant.pow2(pts[i].x - lMaxX) >= farthest) {
                continue;
            }
            buf[++rr] = pts[i];
        }

        //merge
        int intervalL = m + 1;
        int intervalR = m;

        for (int i = l; i <= lr; i++) {
            while (intervalR + 1 <= rr && (buf[intervalR + 1].y <= buf[i].y || GeoConstant.pow2(buf[intervalR + 1].y - buf[i].y) <= farthest)) {
                intervalR++;
            }
            while (intervalL <= rr && buf[i].y >= buf[intervalL].y && GeoConstant.pow2(buf[i].y - buf[intervalL].y) >= farthest) {
                intervalL++;
            }
            if (intervalR - intervalL + 1 > 6) {
                throw new RuntimeException();
            }
            for (int j = intervalL; j <= intervalR; j++) {
                double d2 = dist2(buf[i], buf[j]);
                if (d2 < farthest) {
                    farthest = d2;
                    ans[0] = buf[i];
                    ans[1] = buf[j];
                }
            }
        }

        //merge sort
        CompareUtils.<Point2>mergeAscending(pts, l, m, pts, m + 1, r, buf, l, (a, b) -> Double.compare(a.y, b.y));
        System.arraycopy(buf, l, pts, l, r - l + 1);
        return ans;
    }

    public static Point2[] asPointPolygon(Line2[] line2s) {
        int n = line2s.length;
        Point2[] ans = new Point2[n];
        for (int i = 0; i < n; i++) {
            ans[i] = Line2.intersect(line2s[i], line2s[(i + 1) % n]);
        }
        return ans;
    }

    private static double ang(Point2 l, Point2 mid, Point2 r) {
        double x = dot(l.x - mid.x, l.y - mid.y, r.x - mid.x, r.y - mid.y);
        double y = cross(l.x - mid.x, l.y - mid.y, r.x - mid.x, r.y - mid.y);
        double res = Math.atan2(x, y);
        return res;
    }

    public static boolean inCicle(Point2 a, Point2 b, Point2 c, Point2 d) {
        double kek = ang(a, b, c) + ang(c, d, a) - ang(b, c, d) - ang(d, a, b);
        if (kek > 1e-8)
            return true;
        else
            return false;
    }
}
