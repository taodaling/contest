package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.IntervalBitwiseOrExpandGroup;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readLong();
        long b = in.readLong();

        out.println(IntervalBitwiseOrExpandGroup.expand(a, b));
    }
}
