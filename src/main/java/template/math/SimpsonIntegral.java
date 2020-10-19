package template.math;

public class SimpsonIntegral {
    private final double eps;
    private Function function;
    private final double threshold;

    public SimpsonIntegral(double eps, double threshold, Function function) {
        this.eps = eps;
        this.threshold = threshold;
        this.function = function;
    }

    public static interface Function {
        double y(double x);
    }


    private double simpson(double l, double r) {
        return (r - l) / 6 * (function.y(l) + 4 * function.y((l + r) / 2) + function.y(r));
    }

    private double integral(double l, double r, double totalArea) {
        double m = (l + r) / 2;
        double lArea = simpson(l, m);
        double rArea = simpson(m, r);
        if (r - l <= threshold && Math.abs(lArea + rArea - totalArea) <= 15 * eps) {
            return lArea + rArea + (lArea + rArea - totalArea) / 15;
        }
        return integral(l, m, lArea) + integral(m, r, rArea);
    }

    public double integral(double l, double r) {
        return integral(l, r, simpson(l, r));
    }
}