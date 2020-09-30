package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.KahanSummation;

public class AAsFastAsPossible {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readInt();
        long l = in.readInt();
        long v1 = in.readInt();
        long v2 = in.readInt();
        long k = in.readInt();

        double ans = (double) l / v1;
        double t = (double) l / (2d * v1 * v2 / (v1 + v2) * (DigitUtils.ceilDiv(n, k) - 1) + v2);
        double cand = t * (2d * v2 / (v1 + v2) * (DigitUtils.ceilDiv(n, k) - 1)) + t;
        out.println(Math.min(ans, cand));
    }
}
