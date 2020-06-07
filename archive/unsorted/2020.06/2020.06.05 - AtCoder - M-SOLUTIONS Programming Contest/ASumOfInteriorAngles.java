package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class ASumOfInteriorAngles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        out.println((n - 2) * 180);
    }
}
