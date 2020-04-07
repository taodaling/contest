package template.algo;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * Used to find the maximum value of a Upper convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
public class DoubleTernarySearch {
    private DoubleUnaryOperator operator;
    private double relative;
    private double absolute;

    public DoubleTernarySearch(DoubleUnaryOperator operator, double relative, double absolute) {
        this.operator = operator;
        this.relative = relative;
        this.absolute = absolute;
    }

    public double find(double l, double r) {
        while (r - l > absolute) {
            if (r < 0 && (r - l) / -r <= relative ||
                    l > 0 && (r - l) / l <= relative) {
                break;
            }
            double dist = (r - l) / 3;
            double ml = l + dist;
            double mr = r - dist;
            if (operator.applyAsDouble(ml) < operator.applyAsDouble(mr)) {
                l = ml;
            } else {
                r = mr;
            }
        }
        return (l + r) / 2;
    }
}
