package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class AAddMoreZero {
    double log = Math.log10(2);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        int m = in.readInt();
        int ans = (int)(log * m);
        out.printf("Case #%d: %d", testNumber, ans).println();
    }
}
