package template.geometry.geo3;

import template.utils.GeoConstant;
import template.math.DigitUtils;

import java.util.Comparator;

public class Point3 {
    public final double x;
    public final double y;
    public final double z;

    public static final Point3 ORIGIN = new Point3(0, 0, 0);
    public static final Comparator<Point3> SORT_BY_XYZ = (a, b) -> {
        int ans = GeoConstant.compare(a.x, b.x);
        if (ans == 0) {
            ans = GeoConstant.compare(a.y, b.y);
            if (ans == 0) {
                ans = GeoConstant.compare(a.z, b.z);
            }
        }
        return ans;
    };

    public Point3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point3 minus(Point3 a, Point3 b) {
        return new Point3(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Point3 plus(Point3 a, Point3 b) {
        return new Point3(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Point3 mul(Point3 a, double factor) {
        return new Point3(a.x * factor, a.y * factor, a.z * factor);
    }

    public static Point3 div(Point3 a, double factor) {
        return new Point3(a.x / factor, a.y / factor, a.z / factor);
    }

    @Override
    public boolean equals(Object obj) {
        Point3 other = (Point3) obj;
        return GeoConstant.compare(x, other.x) == 0 &&
                GeoConstant.compare(y, other.y) == 0 &&
                GeoConstant.compare(z, other.z) == 0;
    }

    /**
     * |a||b|cos(a,b)
     *
     * @param a
     * @param b
     * @return
     */
    public static double dot(Point3 a, Point3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    private static double square(double x) {
        return x * x;
    }

    public static double dist2(Point3 a, Point3 b) {
        return square(a.x - b.x) + square(a.y - b.y) + square(a.z - b.z);
    }

    public static double dist(Point3 a, Point3 b) {
        return Math.sqrt(dist2(a, b));
    }

    public double square() {
        return dot(this, this);
    }

    public double abs() {
        return Math.sqrt(square());
    }

    public Point3 norm() {
        return norm(1);
    }

    public Point3 norm(double d) {
        return mul(this, d / abs());
    }

    public static double angle(Point3 a, Point3 b) {
        return Math.acos(DigitUtils.clamp(dot(a, b) / a.abs() / b.abs(), -1.0, 1.0));
    }

    /**
     * |v||w|sin(v,w) p while p is the perpendicular vector obey right hand rule
     *
     * @param v
     * @param w
     * @return
     */
    public static Point3 cross(Point3 v, Point3 w) {
        return new Point3(v.y * w.z - v.z * w.y,
                v.z * w.x - v.x * w.z,
                v.x * w.y - v.y * w.x);
    }

    /**
     * 记p为a、b构成的平面。如果c在p上，返回0，c在p的正面返回1，c在p的反面返回-1。
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int orient(Point3 a, Point3 b, Point3 c) {
        return GeoConstant.sign(dot(cross(a, b), c));
    }

    /**
     * 记p为oa、ob构成的平面。如果oc在p上，返回0，oc在p的正面返回1，oc在p的反面返回-1。
     *
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static int orient(Point3 o, Point3 a, Point3 b, Point3 c) {
        return orient(minus(a, o), minus(b, o), minus(c, o));
    }

    /**
     * norm是a、b、c构成平面的法向量，判断ac在ab的顺逆时针方向。
     * <br>
     * 平行返回0，逆时针返回1，顺时针返回-1。
     *
     * @param a
     * @param b
     * @param c
     * @param norm
     * @return
     */
    public static double orientByNormal(Point3 a, Point3 b, Point3 c, Point3 norm) {
        return orient(minus(b, a), minus(c, a), norm);
    }
}
