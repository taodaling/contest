package template.utils;

public class GeometryUtils {
    public static final double PREC = 1e-12;
    public static final double INF = 1e30;

    public static double valueOf(double x) {
        return x > -PREC && x < PREC ? 0 : x;
    }

    public static boolean near(double a, double b) {
        return valueOf(a - b) == 0;
    }

    public static double pow2(double x) {
        return x * x;
    }

    /**
     * For triangle ABC, the edge is a, b, and c. Return A as result.
     */
    public static double triangleAngle(double a, double b, double c) {
        double cosa = (b * b + c * c - a * a) / (2 * b * c);
        return Math.acos(cosa);
    }

    /**
     * 计算两个向量的叉乘
     */
    public static double cross(double x1, double y1, double x2, double y2) {
        return valueOf(x1 * y2 - y1 * x2);
    }

    public static int signOf(double x) {
        return x > 0 ? 1 : x < 0 ? -1 : 0;
    }

    public static double theta(double y, double x) {
        double theta = Math.atan2(y, x);
        if (theta < 0) {
            theta += Math.PI * 2;
        }
        return theta;
    }
}
