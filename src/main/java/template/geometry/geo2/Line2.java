package template.geometry.geo2;

import template.utils.GeoConstant;

import java.util.Comparator;

public class Line2 {
    /**
     * direction of line
     */
    public Point2 vec;
    public double c;

    public Line2(Point2 vec, double c) {
        this.vec = vec;
        this.c = c;
    }

    public Point2 anyPoint() {
        double a = -vec.y;
        double b = vec.x;
        if (GeoConstant.sign(a) != 0) {
            return new Point2(c / a, 0);
        } else {
            return new Point2(0, c / b);
        }
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

    public void init(Point2 a, Point2 b){
        vec.x = b.x - a.x;
        vec.y = b.y - a.y;
        this.c = Point2.cross(vec, a);
    }

    private double side0(Point2 pt) {
        return Point2.cross(vec, pt) - c;
    }

    /**
     * 如果pt在逆时针方向，返回1，顺时针方向返回-1，否则返回0
     */
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

    /**
     * 判断一条平行的直线处于当前的上方还是下方，上方为1，下方为-1，重合为0
     */
    public int side(Line2 line) {
        //return GeoConstant.compare(line.c, c);
        return side(line.anyPoint());
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
        double d = Point2.cross(a.vec, b.vec);
        if (GeoConstant.sign(d) == 0) {
            return null;
        }
        return Point2.div(Point2.minus(Point2.mul(b.vec, a.c), Point2.mul(a.vec, b.c)), d);
    }

    public Point2 projection(Point2 pt) {
        double factor = side0(pt) / vec.square();
        return new Point2(pt.x - (-vec.y * factor), pt.y - vec.x * factor);
        //return Point2.minus(pt, Point2.mul(vec.perpendicular(), side0(pt) / vec.square()));
    }

    public Point2 reflect(Point2 pt) {
        return Point2.minus(pt, Point2.mul(vec.perpendicular(), 2 * side0(pt) / vec.square()));
    }

    public Comparator<Point2> sortPointAlongLine() {
        return (a, b) -> GeoConstant.compare(Point2.dot(vec, a), Point2.dot(vec, b));
    }

    @Override
    public String toString() {
        return -vec.y + "x + " + vec.x + "y = " + c;
    }

    public static Line2[] asLinePolygon(Point2[] pts){
        int n = pts.length;
        Line2[] ans = new Line2[n];
        for(int i = 0; i < n; i++){
            ans[i] = new Line2(pts[i], pts[(i + 1) % n]);
        }
        return ans;
    }
}
