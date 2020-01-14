package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class ADeadline {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int d = in.readInt();
        int ans = d;
        for (int l = 1, r; l <= d; l = r + 1) {
            if (d % l == 0) {
                r = l;
            } else {
                r = d / (d / l);
                if (d % r == 0) {
                    r--;
                }
            }
            ans = Math.min(ans, l - 1 + DigitUtils.ceilDiv(d, l));
        }

        out.println(ans <= n ? "YES" : "NO");
    }
}
