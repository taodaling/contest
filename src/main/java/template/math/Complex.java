package template.math;

public class Complex {
    public static void plus(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] + b[0];
        ans[1] = a[1] + b[1];
    }

    public static void subtract(double[] a, double[] b, double[] ans) {
        ans[0] = a[0] - b[0];
        ans[1] = a[1] - b[1];
    }

    public static void mul(double[] a, double[] b, double[] ans) {
        double x1 = a[0];
        double y1 = a[1];
        double x2 = b[0];
        double y2 = b[1];
        ans[0] = x1 * x2 - y1 * y2;
        ans[1] = x1 * y2 + y1 * x2;
    }

    public static void divide(double[] a, double[] b, double[] ans) {
        conj(b, ans);
        mul(a, ans, ans);
        shrink(ans, 1 / length2(b), ans);
    }

    public static double length2(double[] x) {
        return x[0] * x[0] + x[1] * x[1];
    }

    public static void reverse(double[] a, double[] ans) {
        ans[0] = -a[0];
        ans[1] = -a[1];
    }

    public static double length(double[] x) {
        return Math.sqrt(length2(x));
    }

    public static void norm(double[] x, double[] ans) {
        double sqrt = length(x);
        ans[0] = x[0] / sqrt;
        ans[1] = x[1] / sqrt;
    }

    public static void conj(double[] a, double[] ans) {
        ans[0] = a[0];
        ans[1] = -a[1];
    }

    public static void shrink(double[] a, double x, double[] ans) {
        ans[0] = a[0] * x;
        ans[1] = a[1] * x;
    }

    public static void rotate(double theta, double[] ans) {
        ans[0] = Math.cos(theta);
        ans[1] = Math.sin(theta);
    }

    public static void move(double x, double y, double[] ans) {
        ans[0] = x;
        ans[1] = y;
    }

    public static void copy(double[] a, double[] ans) {
        ans[0] = a[0];
        ans[1] = a[1];
    }
}