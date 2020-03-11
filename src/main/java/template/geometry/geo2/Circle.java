package template.geometry.geo2;

import java.util.List;

public class Circle {
    public Point center;
    public double r;

    public Circle(Point center, double r) {
        this.center = center;
        this.r = r;
    }

    public boolean contain(Point pt) {
        return Geo2Constant.sign(Point.dist2(center, pt) - r * r) <= 0;
    }

    public int intersect(Line line, List<Point> list) {
        Point proj = line.projection(center);
        if (!contain(proj)) {
            return 0;
        }
        double h = Math.sqrt(r * r - Point.dist2(proj, center));
        if (Geo2Constant.sign(h) == 0) {
            list.add(proj);
            return 1;
        } else {
            Point vec = line.vec.norm(h);
            list.add(Point.plus(proj, vec));
            list.add(Point.minus(proj, vec));
            return 2;
        }
    }

    public int intersect(Circle c1, Circle c2, List<Point> list) {
        Point d = Point.minus(c2.center, c1.center);
        double d2 = d.square();
        if (d2 == 0) {
            return 0;
        }
        double pd = (d2 + c1.r * c1.r - c2.r * c2.r) / 2;
        double h2 = c1.r * c1.r - pd * pd / d2;
        if (h2 >= 0) {
            Point p = Point.plus(c1.center, Point.mul(d, pd / d2));
            if (Geo2Constant.isZero(h2)) {
                list.add(p);
                return 0;
            }
            Point h = Point.mul(d.perpendicular(), Math.sqrt(h2 / d2));
            list.add(Point.minus(p, h));
            list.add(Point.plus(p, h));
            return 2;
        }
        return 0;
    }


}
