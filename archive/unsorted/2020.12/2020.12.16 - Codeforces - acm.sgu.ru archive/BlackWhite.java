package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;

public class BlackWhite {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long len = in.rl();
        long n = (len + 1) / 2;
        long g = GCDs.gcd(2 * n - 1, n + 1);
        long ans = g * DigitUtils.floorDiv(len / g, 2);
        out.println(ans + 1);
    }
}
