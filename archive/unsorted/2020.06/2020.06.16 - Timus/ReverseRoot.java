package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ReverseRoot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readLong();
        if (in.hasMore()) {
            solve(testNumber, in, out);
        }
        double ans = Math.sqrt(x);
        out.printf("%.4f\n", ans);
    }
}
