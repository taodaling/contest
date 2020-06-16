package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class FireCircle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long r = in.readInt();
        long area = 0;

        double prec = 1e-9;
        for (int i = 0; i < r; i++) {
            double x = i;
            double y = Math.sqrt(r * r - x * x);
            if (Math.abs(y - DigitUtils.round(y)) <= prec) {
                y = DigitUtils.round(y);
            }

            int h = (int)Math.ceil(y);
            area += h;
        }

        out.println(area * 4);
    }
}
