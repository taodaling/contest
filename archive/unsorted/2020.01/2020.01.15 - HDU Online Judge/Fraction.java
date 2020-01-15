package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.ILongModular;
import template.math.MinimumFraction;

public class Fraction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long p = in.readLong();
        long x = in.readLong();
        long[] ans = MinimumFraction.findMinimumFraction(x, p, x - 1, p, false, false);
        long b = ans[1];
        long a = ILongModular.getInstance(p).mul(b, x);
        out.append(a).append('/').append(b).println();
    }
}
