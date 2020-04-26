package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ARoadToZero {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readInt();
        long y = in.readInt();
        long a = in.readInt();
        long b = in.readInt();
        long ans = (Math.abs(x) + Math.abs(y)) * a;
        if (Long.signum(x) == Long.signum(y)) {
            ans = Math.min(ans, Math.min(Math.abs(x), Math.abs(y)) * b + Math.abs(x - y) * a);
        }

        out.println(ans);
    }
}
