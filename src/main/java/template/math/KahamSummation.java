package template.math;

import template.primitve.generated.datastructure.IntToDoubleFunction;

import java.math.BigDecimal;


/**
 * O(1) error for real number summation
 */
public class KahamSummation {
    private double error;
    private double sum;

    public void reset() {
        error = 0;
        sum = 0;
    }

    public double sum() {
        return sum;
    }

    public void add(double x) {
        x = x - error;
        double t = sum + x;
        error = (t - sum) - x;
        sum = t;
    }

    public void subtract(double x) {
        add(-x);
    }

    public void addAll(IntToDoubleFunction func, int l, int r) {
        for (int i = l; i <= r; i++) {
            add(func.apply(i));
        }
    }

    @Override
    public String toString() {
        return new BigDecimal(sum).toString();
    }
}
