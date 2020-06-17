package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class SummitOnlineJudge {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readLong();
        long y = in.readLong();
        long delta = y - x;
        long l = in.readLong();
        long r = in.readLong();
        //ky >= x - 1
        long inf = (long) 2e18;
        long k = delta == 0 ? inf : DigitUtils.ceilDiv(x - 1, delta);
        long first = DigitUtils.ceilDiv(l, y);
        long last = DigitUtils.ceilDiv(r, y);
        l = Math.max(l, first * x);
        if (l > r) {
            out.println(0);
            return;
        }

        if (first == last) {
            long ans = subset(l, r, first * x, first * y);
            out.println(ans);
            return;
        }

        if (k <= first) {
            out.println(r - l + 1);
            return;
        }

        long ans = subset(l, r, first * x, first * y);
        if (k >= last) {
            ans += span(x, y, first + 1, last - 1);
            ans += subset(l, r, last * x, last * y);
        } else {
            ans += span(x, y, first + 1, k - 1);
            ans += subset(l, r, k * x, r);
        }

        out.println(ans);
    }

    public long span(long x, long y, long a, long b) {
        long delta = y - x;
        //a delta + ... + b delta
        return (a + b) * (b - a + 1) / 2 * delta + (b - a + 1);
    }

    public long subset(long l, long r, long ll, long rr) {
        long fixL = Math.max(l, ll);
        long fixR = Math.min(r, rr);
        return Math.max(0, fixR - fixL + 1);
    }
}
