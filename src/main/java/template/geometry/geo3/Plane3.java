package template.geometry.geo3;

import template.geometry.GeoConstant;

public class Plane3 {
    public final Point3 n;
    public final double d;


    public Plane3(Point3 n, double d) {
        this.n = n;
        this.d = d;
    }

    /**
     * Given normal vector and point on plane
     */
    public Plane3(Point3 n, Point3 p) {
        this(n, Point3.dot(n, p));
    }

    /**
     * Given three points on plane
     */
    public Plane3(Point3 a, Point3 b, Point3 c) {
        this(Point3.cross(Point3.minus(c, a), Point3.minus(b, a)), a);
    }

    /**
     * ax + by + cz = d
     *
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public Plane3(double a, double b, double c, double d) {
        this(new Point3(a, b, c), d);
    }

    /**
     * Return 0 if p on the plane, else return 1 if p above the plane, else -1
     */
    public int side(Point3 p) {
        return GeoConstant.sign(side0(p));
    }

    public double side0(Point3 p) {
        return Point3.dot(n, p) - d;
    }

    public double dist(Point3 p) {
        return side0(p) / n.abs();
    }

    public Plane3 translate(Point3 vec) {
        return new Plane3(n, Point3.dot(n, vec) + d);
    }

    public Point3 project(Point3 p) {
        return Point3.minus(p, Point3.mul(n, side0(p) / n.square()));
    }

    public Point3 reflect(Point3 p) {
        return Point3.minus(p, Point3.mul(n, 2 * side0(p) / n.square()));
    }

    public boolean contain(Point3 p) {
        return side(p) == 0;
    }

    public boolean contain(Line3 line) {
        return contain(line.o) && contain(Point3.plus(line.o, line.d));
    }

    public static double angle(Plane3 p1, Plane3 p2) {
        return Point3.angle(p1.n, p2.n);
    }

    public static boolean isParallel(Plane3 p1, Plane3 p2) {
        return Point3.cross(p1.n, p2.n).equals(Point3.ORIGIN);
    }

    public static boolean isPerpendicular(Plane3 p1, Plane3 p2) {
        return GeoConstant.sign(Point3.dot(p1.n, p2.n)) == 0;
    }

    public static double angle(Plane3 p, Line3 l) {
        return Math.PI / 2 - Point3.angle(p.n, l.d);
    }

    public static boolean isParallel(Plane3 p, Line3 l) {
        return GeoConstant.sign(Point3.dot(p.n, l.d)) == 0;
    }

    public static boolean isPerpendicular(Plane3 p, Line3 l) {
        return Point3.cross(p.n, l.d).equals(Point3.ORIGIN);
    }

    public Line3 perpendicularThrough(Point3 pt) {
        return new Line3(pt, n);
    }
}
