package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class AVasyaAndTriangle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readInt();
        long m = in.readInt();
        long k = in.readInt();
        if (2 * n * m % k != 0) {
            out.println("NO");
            return;
        }
        out.println("YES");
        out.println("0 0");
        long area = 2 * n * m / k;

        long x3 = 1;
        long y3 = m;
        long x2 = DigitUtils.ceilDiv(area, y3);
        long y2 = x2 * y3 - area;

        out.append(x2).append(' ').append(y2).println();
        out.append(x3).append(' ').append(y3).println();
    }
}
