package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Nimber;

public class NimProduct {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readLong();
        long b = in.readLong();
        out.println(Nimber.product(a, b));
    }
}
