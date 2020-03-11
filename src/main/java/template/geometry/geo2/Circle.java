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


}
