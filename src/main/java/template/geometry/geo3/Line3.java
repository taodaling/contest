package template.geometry.geo3;

import template.geometry.GeoConstant;

import java.util.Comparator;

public class Line3 {
    Point3 o;
    Point3 d;

    public Line3(Point3 o, Point3 d) {
        this.o = o;
        this.d = d;
    }


    public Line3(Plane3 p1, Plane3 p2) {
        d = Point3.cross(p1.n, p2.n);
        o = Point3.cross(Point3.minus(Point3.mul(p2.n, p1.d), Point3.mul(p1.n, p2.d)), Point3.div(d, d.square()));
    }

    public boolean contain(Point3 pt) {
        return Point3.cross(d, Point3.minus(pt, o)).equals(Point3.ORIGIN);
    }

    public Point3 intersect(Plane3 p) {
        return Point3.minus(o, Point3.div(Point3.mul(d, p.side0(o)), Point3.dot(d, p.n)));
    }

    public double squareDist(Point3 p) {
        return Point3.div(Point3.cross(d, Point3.minus(p, o)), d.square()).square();
    }

    public double dist(Point3 p) {
        return Math.sqrt(squareDist(p));
    }

    public Comparator<Point3> sortPointAlongLine() {
        return (a, b) -> Double.compare(Point3.dot(d, a), Point3.dot(d, b));
    }

    public Point3 project(Point3 p) {
        return Point3.plus(o, Point3.mul(d, Point3.dot(d, Point3.minus(p, o)) / d.square()));
    }

    public Point3 reflect(Point3 p) {
        return Point3.minus(Point3.mul(project(p), 2), p);
    }

    public static double dist(Line3 l1, Line3 l2) {
        Point3 n = Point3.cross(l1.d, l2.d);
        if (n.equals(Point3.ORIGIN)) {
            return l1.dist(l2.o);
        }
        return Math.abs(Point3.dot(Point3.minus(l2.o, l1.o), n)) / n.abs();
    }

    public static Point3 closestOnL1(Line3 l1, Line3 l2) {
        Point3 n2 = Point3.cross(l2.d, Point3.cross(l1.d, l2.d));
        return Point3.plus(l1.o, Point3.mul(l1.d, Point3.dot(Point3.minus(l2.o, l1.o), n2) / Point3.dot(l1.d, n2)));
    }

    public static double angle(Line3 l1, Line3 l2) {
        return Point3.angle(l1.d, l2.d);
    }

    public static boolean isParallel(Line3 l1, Line3 l2) {
        return Point3.cross(l1.d, l2.d).equals(Point3.ORIGIN);
    }

    public static boolean isPerpendicular(Line3 l1, Line3 l2) {
        return GeoConstant.sign(Point3.dot(l1.d, l2.d)) == 0;
    }
}
