package template.math;

public class SimpsonIntegral {

    public static void main(String[] args) {
        System.out.println(
                new SimpsonIntegral(1e-10, x -> {
                    var sin = Math.sin(x);
                    var cos = Math.cos(x);
                    return (1 + cos) * (1 + cos) * sin * sin * (-sin * cos - (1 + cos) * sin);
                }).integral(0, Math.PI / 2)
        );

        System.out.println((15.0 / 48 * Math.PI - 2.0 / 3) * Math.PI);
    }

    private final double eps;
    private Function function;
    private final double threshold;

    public SimpsonIntegral(double eps, Function function, double threshold) {
        this.eps = eps;
        this.threshold = threshold;
        this.function = function;
    }

    public SimpsonIntegral(double eps, Function function) {
        this(eps, function, 1e50);
    }

    public static interface Function {
        double y(double x);
    }


    private double simpson(double l, double r) {
        return (r - l) / 6 * (function.y(l) + 4 * function.y((l + r) / 2) + function.y(r));
    }

    private double integral(double l, double r, double totalArea) {
        assert !Double.isNaN(totalArea);
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