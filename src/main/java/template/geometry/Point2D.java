package template.geometry;

import template.utils.GeometryUtils;

import java.util.Comparator;

public class Point2D {
    public final double x;
    public final double y;
    static final Point2D ORIGIN = new Point2D(0, 0);
    static final Comparator<Point2D> SORT_BY_X_AND_Y = new Comparator<Point2D>() {
        @Override
        public int compare(Point2D a, Point2D b) {
            return a.x == b.x ? Double.compare(a.y, b.y) : Double.compare(a.x, b.x);
        }
    };
    static final Comparator<Point2D> SORT_BY_Y_AND_X = new Comparator<Point2D>() {
        @Override
        public int compare(Point2D a, Point2D b) {
            return a.y == b.y ? Double.compare(a.x, b.x) : Double.compare(a.y, b.y);
        }
    };

    public Point2D(double x, double y) {
        this.x = GeometryUtils.valueOf(x);
        this.y = GeometryUtils.valueOf(y);
    }

    public double distance2Between(Point2D another) {
        double dx = x - another.x;
        double dy = y - another.y;
        return GeometryUtils.valueOf(dx * dx + dy * dy);
    }

    public double distanceBetween(Point2D another) {
        return GeometryUtils.valueOf(Math.sqrt(distance2Between(another)));
    }

    public boolean near(Point2D another) {
        return distance2Between(another) <= GeometryUtils.PREC;
    }

    /**
     * 以自己为起点，判断线段a和b的叉乘
     */
    public double cross(Point2D a, Point2D b) {
        return GeometryUtils.cross(a.x - x, a.y - y, b.x - x, b.y - y);
    }

    public Point2D getApeak() {
        return new Point2D(-y, x);
    }

    public Point2D add(Point2D vector) {
        return new Point2D(x + vector.x, y + vector.y);
    }

    public Point2D add(Point2D vector, double times) {
        return new Point2D(x + vector.x * times, y + vector.y * times);
    }

    public Point2D normalize() {
        double d = distanceBetween(ORIGIN);
        return new Point2D(x / d, y / d);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public static double cross(Point2D a, Point2D b, Point2D c, Point2D d) {
        return GeometryUtils.cross(b.x - a.x, b.y - a.y, d.x - c.x, d.y - c.y);
    }

    @Override
    public int hashCode() {
        return (int) (Double.doubleToLongBits(x) * 31 + Double.doubleToLongBits(y));
    }

    @Override
    public boolean equals(Object obj) {
        Point2D other = (Point2D) obj;
        return x == other.x && y == other.y;
    }
}
