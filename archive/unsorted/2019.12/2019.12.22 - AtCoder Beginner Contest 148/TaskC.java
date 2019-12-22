package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.readInt();
        long b = in.readInt();
        out.println(a / GCDs.gcd(a, b) * b);
    }
}
