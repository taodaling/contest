package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P3___AQT_and_Circles;



import template.io.FastInput;
import template.io.FastOutput;

public class P3AQTAndCircles {
    public double area(double r) {
        return r * r * Math.PI;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int r1 = in.ri();
        int r2 = in.ri();
        int r3 = in.ri();
        double total = area(r2 - r3);
        double possible = 0;
        if (r3 == r1) {
        } else if (r1 < r3) {
            int r = r3 - r1;
            r = Math.min(r2 - r3, r);
            possible += area(r);
        } else {
            possible += area(r1 - r3);
        }
        if (r1 + 2 * r3 <= r2) {
            possible += area(r2 - r3) - area(r1 + r3);
        }
        out.println(possible / total);
    }
}
