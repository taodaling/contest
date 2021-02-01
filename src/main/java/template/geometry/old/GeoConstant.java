package template.geometry.old;

import template.geometry.geo2.Point2;

public class GeoConstant {
    public static final double PREC = 1e-10;

    public static final double INF = 1e50;

    public static boolean isZero(double x) {
        return -PREC <= x && x <= PREC;
    }

    public static int sign(double x) {
        return isZero(x) ? 0 : x < 0 ? -1 : 1;
    }

    public static int compare(double a, double b) {
        return sign(a - b);
    }

    public static double cross(double x1, double y1, double x2, double y2) {
        return x1 * y2 - y1 * x2;
    }

    public static double pow2(double x) {
        return x * x;
    }

    /**
     * [0, 2 PI)
     */
    public static double theta(double x, double y) {
        double theta = Math.atan2(y, x);
        if (theta < 0) {
            theta += Math.PI * 2;
        }
        return theta;
    }

    public static double theta(Point2 pt) {
        return theta(pt.x, pt.y);
    }

    /**
     * For triangle ABC, the edge is a, b, and c. Return A as result.
     */
    public static double triangleAngle(double a, double b, double c) {
        double cosa = (b * b + c * c - a * a) / (2 * b * c);
        return Math.acos(cosa);
    }
}
