package template.geometry.geo2;

import java.util.Comparator;

public class Line {
    public Point vec;
    public double c;

    public Line(Point vec, double c) {
        this.vec = vec;
        this.c = c;
    }

    /**
     * ax + by = c
     */
    public Line(double a, double b, double c) {
        this(new Point(b, -a), c);
    }

    public Line(Point a, Point b) {
        this.vec = Point.minus(b, a);
        this.c = Point.cross(vec, a);
    }

    private double side0(Point pt) {
        return Point.cross(vec, pt) - c;
    }

    public int side(Point pt) {
        return Geo2Constant.sign(side0(pt));
    }

    public double dist(Point pt) {
        return Math.abs(side0(pt)) / vec.abs();
    }

    public double squareDist(Point pt) {
        double side = Point.cross(vec, pt) - c;
        return side * side / vec.square();
    }

    public Line perpendicularThrough(Point pt) {
        return new Line(pt, Point.plus(pt, vec.perpendicular()));
    }

    public static Comparator<Point> sortPointOnLine(Line line) {
        return new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {
                return Double.compare(Point.dot(a, line.vec), Point.dot(b, line.vec));
            }
        };
    }

    public Line translate(Point pt) {
        return new Line(vec, c + Point.cross(vec, pt));
    }

    public Line shiftLeft(double dist) {
        return new Line(vec, c + dist * vec.abs());
    }

    public static Point intersect(Line a, Line b) {
        if (Point.orient(a.vec, b.vec) == 0) {
            return null;
        }
        return Point.div(Point.minus(Point.mul(a.vec, a.c), Point.mul(b.vec, b.c)), Point.mul(a.vec, b.vec));
    }

    public Point projection(Point pt) {
        return Point.minus(pt, Point.mul(vec.perpendicular(), side0(pt) / vec.square()));
    }

    public Point reflect(Point pt) {
        return Point.minus(pt, Point.mul(vec.perpendicular(), 2 * side0(pt) / vec.square()));
    }
}
