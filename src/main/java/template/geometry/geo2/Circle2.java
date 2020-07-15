package template.geometry.geo2;

import template.geometry.GeoConstant;
import template.rand.Randomized;

import java.util.List;

public class Circle2 {
    public Point2 center;
    public double r;

    public Circle2(Point2 center, double r) {
        this.center = center;
        this.r = r;
    }

    public boolean contain(Point2 pt) {
        return GeoConstant.sign(Point2.dist2(center, pt) - r * r) <= 0;
    }

    /**
     * 获取与直线的所有交点
     *
     * @param line
     * @param list
     * @return
     */
    public int intersect(Line2 line, List<Point2> list) {
        Point2 proj = line.projection(center);
        if (!contain(proj)) {
            return 0;
        }
        double h = Math.sqrt(r * r - Point2.dist2(proj, center));
        if (GeoConstant.sign(h) == 0) {
            list.add(proj);
            return 1;
        } else {
            Point2 vec = line.vec.norm(h);
            list.add(Point2.plus(proj, vec));
            list.add(Point2.minus(proj, vec));
            return 2;
        }
    }

    /**
     * 获取两个圆的交点
     *
     * @param c1
     * @param c2
     * @param list
     * @return
     */
    public int intersect(Circle2 c1, Circle2 c2, List<Point2> list) {
        Point2 d = Point2.minus(c2.center, c1.center);
        double d2 = d.square();
        if (d2 == 0) {
            return 0;
        }
        double pd = (d2 + c1.r * c1.r - c2.r * c2.r) / 2;
        double h2 = c1.r * c1.r - pd * pd / d2;
        if (h2 >= 0) {
            Point2 p = Point2.plus(c1.center, Point2.mul(d, pd / d2));
            if (GeoConstant.isZero(h2)) {
                list.add(p);
                return 0;
            }
            Point2 h = Point2.mul(d.perpendicular(), Math.sqrt(h2 / d2));
            list.add(Point2.minus(p, h));
            list.add(Point2.plus(p, h));
            return 2;
        }
        return 0;
    }

    /**
     * O(r-l+1) algorithm solve min circle cover problem
     */
    public static Circle2 minCircleCover(Point2[] pts, int l, int r) {
        Randomized.shuffle(pts, l, r + 1);
        Circle2 circle = new Circle2(Point2.ORIGIN, 0);
        for (int i = l; i <= r; i++) {
            if (circle.contain(pts[i])) {
                continue;
            }
            circle = new Circle2(pts[i], 0);
            for (int j = l; j < i; j++) {
                if (circle.contain(pts[j])) {
                    continue;
                }
                circle = getCircleByDiameter(pts[i], pts[j]);
                for (int k = l; k < j; k++) {
                    if (circle.contain(pts[k])) {
                        continue;
                    }
                    circle = outerCircleOfTriangle(pts[i], pts[j], pts[k]);
                }
            }
        }
        return circle;
    }

    public static Circle2 getCircleByDiameter(Point2 a, Point2 b) {
        return new Circle2(new Point2((a.x + b.x) / 2, (a.y + b.y) / 2), Point2.dist(a, b) / 2);
    }

    public static Circle2 innerCircleOfTriangle(Point2 A, Point2 B, Point2 C) {
        double a = Point2.dist(B, C);
        double b = Point2.dist(A, C);
        double c = Point2.dist(A, B);
        double area = Math.abs(Point2.cross(A, B, C)) / 2;
        double r = area * 2 / (a + b + c);
        double x = (a * A.x + b * B.x + c * C.x) / (a + b + c);
        double y = (a * A.y + b * B.y + c * C.y) / (a + b + c);
        return new Circle2(new Point2(x, y), r);
    }

    private static double det(double a11, double a12, double a21, double a22, double a31, double a32) {
        return a11 * a22 - a21 * a12 - a11 * a32 + a31 * a12
                + a21 * a32 - a31 * a22;
    }

    public static Circle2 outerCircleOfTriangle(Point2 A, Point2 B, Point2 C) {
        double a = Point2.dist(B, C);
        double b = Point2.dist(A, C);
        double c = Point2.dist(A, B);
        double area = Math.abs(Point2.cross(A, B, C)) / 2;
        double r = a * b * c / 4 / area;
        double bot = det(A.x, A.y, B.x, B.y, C.x, C.y) * 2;
        double x = det(A.square(), A.y, B.square(), B.y, C.square(), C.y) / bot;
        double y = det(A.x, A.square(), B.x, B.square(), C.x, C.square()) / bot;
        return new Circle2(new Point2(x, y), r);
    }
}
