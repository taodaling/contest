package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class APainting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int h = in.readInt();
        int w = in.readInt();
        int n = in.readInt();
        out.println(DigitUtils.ceilDiv(n, Math.max(h, w)));
    }
}
