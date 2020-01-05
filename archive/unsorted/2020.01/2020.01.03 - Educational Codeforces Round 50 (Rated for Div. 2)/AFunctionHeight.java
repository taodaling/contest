package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class AFunctionHeight {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long k = in.readLong();
        long ans = DigitUtils.ceilDiv(k, n);
        out.println(ans);
    }
}
