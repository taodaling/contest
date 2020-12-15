package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class Billing {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.ri();
        long k1 = in.ri();
        long k2 = in.ri();
        long p1 = in.ri();
        long p2 = in.ri();
        long p3 = in.ri();
        long time = 0;
        if (n < p1) {
            out.println(0);
            return;
        }
        time += k1;
        n -= p1;
        if (n <= p2 * k2) {
            out.println(time + DigitUtils.ceilDiv(n, p2));
            return;
        }
        time += k2;
        n -= p2 * k2;
        out.println(time + DigitUtils.ceilDiv(n, p3));
    }
}
