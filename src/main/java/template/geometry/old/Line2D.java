package template.geometry.old;

import java.util.Comparator;

import template.utils.GeometryUtils;

public class Line2D {
    public final Point2D a;
    public final Point2D b;
    public final Point2D d;
    private double theta = -1e50;
    /**
     * 按照[0,2pi)极角对线排序
     */
    static final Comparator<Line2D> SORT_BY_ANGLE = new Comparator<Line2D>() {
        @Override
        public int compare(Line2D a, Line2D b) {
            return Double.compare(a.getTheta(), b.getTheta());
        }
    };

    public double getTheta() {
        if (theta == -1e50) {
            theta = Math.atan2(d.y, d.x);
        }
        return theta;
    }

    public Line2D(Point2D a, Point2D b) {
        this.a = a;
        this.b = b;
        d = new Point2D(b.x - a.x, b.y - a.y);
    }

    public Point2D getVector(){
        return d;
    }

    /**
     * 判断a处于b的哪个方向，返回1，表示处于逆时针方向，返回-1，表示处于顺时针方向。0表示共线。
     */
    public int onWhichSide(Line2D b) {
        return Double.compare(GeometryUtils.cross(d.x, d.y, b.d.x, b.d.y), 0);
    }

    /**
     * 判断pt处于自己的哪个方向，返回1，表示处于逆时针方向，返回-1，表示处于顺时针方向。0表示共线。
     */
    public int whichSideIs(Point2D pt) {
        return Double.compare(a.cross(b, pt), 0);
    }

    public double getSlope() {
        return a.y / a.x;
    }

    public double getB() {
        return a.y - getSlope() * a.x;
    }

    public Point2D intersect(Line2D another) {
        double m11 = b.x - a.x;
        double m01 = another.b.x - another.a.x;
        double m10 = a.y - b.y;
        double m00 = another.a.y - another.b.y;

        double div = GeometryUtils.valueOf(m00 * m11 - m01 * m10);
        if (div == 0) {
            return null;
        }

        double v0 = (another.a.x - a.x) / div;
        double v1 = (another.a.y - a.y) / div;

        double alpha = m00 * v0 + m01 * v1;
        return getPoint(alpha);
    }

    /**
     * 获取与线段的交点，null表示无交点或有多个交点
     */
    public Point2D getPoint(double alpha) {
        return new Point2D(a.x + d.x * alpha, a.y + d.y * alpha);
    }

    public Line2D moveAlong(Point2D vector) {
        return new Line2D(a.add(vector), b.add(vector));
    }

    public Line2D moveAlong(Point2D vector, double times) {
        return new Line2D(a.add(vector, times), b.add(vector, times));
    }


    @Override
    public String toString() {
        return d.toString();
    }
}
