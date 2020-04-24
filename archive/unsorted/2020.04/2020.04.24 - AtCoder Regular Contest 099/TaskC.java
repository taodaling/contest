package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        out.println(DigitUtils.ceilDiv(n - 1, k - 1));
    }
}
