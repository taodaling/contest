package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BLittleArtemAndDance {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long a = 0;
        long b = 1;
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                int x = in.ri();
                int y = Math.abs(x);
                if (y % 2 == 1) {
                    if (x < 0) {
                        long tmp = a + 2;
                        a = b;
                        b = tmp;
                    } else {
                        long tmp = b - 2;
                        b = a;
                        a = tmp;
                    }
                }
                a -= x / 2 * 2;
                b -= x / 2 * 2;
            }else{
                long tmp = a;
                a = b;
                b = tmp;
            }
        }
        a = DigitUtils.mod(a, n);
        b = DigitUtils.mod(b, n);
        for (int i = 0; i < n; i += 2, a += 2, b += 2) {
            if (a >= n) {
                a -= n;
            }
            if (b >= n) {
                b -= n;
            }
            out.append(a + 1).append(' ').append(b + 1).append(' ');
        }
    }
}
