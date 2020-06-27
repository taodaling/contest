package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.HandyLongModular;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long x = in.readLong();
        long y = in.readLong();
        long m = in.readLong();
        HandyLongModular mod = new HandyLongModular(m);
        out.println(mod.mul(x, y));
    }
}
