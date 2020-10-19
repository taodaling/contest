package contest;


import template.algo.DoubleTernarySearch;
import template.io.FastInput;
import template.math.SimpsonIntegral;

import java.io.PrintWriter;
import java.util.function.DoubleUnaryOperator;

public class Task {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        double g = 9.81;
        double speed = 20;
        double h = 100;

        SimpsonIntegral low = new SimpsonIntegral(1e-10, 1e-2, x -> {
            double delta = x - h;
            double a = 0.5 * g;
            double c = delta;
            double yl = delta <= 0 ? 0 : Math.sqrt(4 * a * c);
            if (yl >= speed) {
                return 0;
            }
            DoubleUnaryOperator op = y -> {
                double r = (y + Math.sqrt(y * y - 4 * a * c)) / (2 * a) * Math.sqrt(speed * speed - y * y);
                return r;
            };
            DoubleTernarySearch dts = new DoubleTernarySearch(op, 1e-10, 1e-10);
            double r = op.applyAsDouble(dts.find(yl, speed));
            return Math.PI * r * r;
        });
        double ans = low.integral(0, 200);
        out.println(ans);
    }
}
