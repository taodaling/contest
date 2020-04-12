package template.geometry;

public class GeoConstant {
    public static final double PREC = 1e-10;

    public static boolean isZero(double x) {
        return -PREC <= x && x <= PREC;
    }

    public static int sign(double x) {
        return isZero(x) ? 0 : x < 0 ? -1 : 1;
    }

    public static int compare(double a, double b) {
        return sign(a - b);
    }
}
