package template.geometry.geo2;

import template.geometry.GeoConstant;

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


}
