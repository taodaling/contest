package contest;

import template.datastructure.DiscreteMap;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class BTag {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readInt();
        long v = in.readInt();
        long b = in.readInt();
        long w = in.readInt();

        long dist = Math.abs(a - b);
        long t = in.readInt();
        dist -= t * (v - w);

        if (dist <= 0) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }
}
