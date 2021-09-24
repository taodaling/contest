package template.math;

import template.primitve.generated.datastructure.IntToDoubleFunction;

import java.math.BigDecimal;


/**
 * kahan summation algorithm
 * O(1) error for real number summation
 */
public class SumOfFloat implements Comparable<SumOfFloat> {
    private double error;
    private double sum;

    public void reset() {
        set(0);
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

    public void set(double x) {
        error = 0;
        sum = x;
    }

    @Override
    public int compareTo(SumOfFloat o) {
        double ans = (sum - o.sum) + (error - o.error);
        return ans < 0 ? -1 : ans > 0 ? 1 : 0;
    }

    public void copy(SumOfFloat s) {
        sum = s.sum;
        error = s.error;
    }

    public void add(SumOfFloat s) {
        add(s.sum);
        add(s.error);
    }

    @Override
    public String toString() {
        return new BigDecimal(sum).toString();
    }
}
