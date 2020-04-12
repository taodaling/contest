package template.geometry.geo2;

import template.geometry.GeoConstant;

import java.util.Comparator;

public class Line2 {
    public Point2 vec;
    public double c;

    public Line2(Point2 vec, double c) {
        this.vec = vec;
        this.c = c;
    }

    /**
     * ax + by = c
     */
    public Line2(double a, double b, double c) {
        this(new Point2(b, -a), c);
    }

    public Line2(Point2 a, Point2 b) {
        this.vec = Point2.minus(b, a);
        this.c = Point2.cross(vec, a);
    }

    private double side0(Point2 pt) {
        return Point2.cross(vec, pt) - c;
    }

    public int side(Point2 pt) {
        return GeoConstant.sign(side0(pt));
    }

    public double dist(Point2 pt) {
        return Math.abs(side0(pt)) / vec.abs();
    }

    public double squareDist(Point2 pt) {
        double side = Point2.cross(vec, pt) - c;
        return side * side / vec.square();
    }

    public Line2 perpendicularThrough(Point2 pt) {
        return new Line2(pt, Point2.plus(pt, vec.perpendicular()));
    }

    public static Comparator<Point2> sortPointOnLine(Line2 line) {
        return new Comparator<Point2>() {
            @Override
            public int compare(Point2 a, Point2 b) {
                return Double.compare(Point2.dot(a, line.vec), Point2.dot(b, line.vec));
            }
        };
    }

    public Line2 translate(Point2 pt) {
        return new Line2(vec, c + Point2.cross(vec, pt));
    }

    public Line2 shiftLeft(double dist) {
        return new Line2(vec, c + dist * vec.abs());
    }

    public static Point2 intersect(Line2 a, Line2 b) {
        if (Point2.orient(a.vec, b.vec) == 0) {
            return null;
        }
        return Point2.div(Point2.minus(Point2.mul(a.vec, a.c), Point2.mul(b.vec, b.c)), Point2.mul(a.vec, b.vec));
    }

    public Point2 projection(Point2 pt) {
        return Point2.minus(pt, Point2.mul(vec.perpendicular(), side0(pt) / vec.square()));
    }

    public Point2 reflect(Point2 pt) {
        return Point2.minus(pt, Point2.mul(vec.perpendicular(), 2 * side0(pt) / vec.square()));
    }

    public Comparator<Point2> sortPointAlongLine() {
        return (a, b) -> Double.compare(Point2.dot(vec, a), Point2.dot(vec, b));
    }
}
