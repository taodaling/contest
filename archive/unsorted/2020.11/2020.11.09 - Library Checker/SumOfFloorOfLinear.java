package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class SumOfFloorOfLinear {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long m = in.readLong();
        long a = in.readLong();
        long b = in.readLong();
        long ans = IntMath.sumFloorArithmeticSequence(n, m, a, b);
        out.println(ans);
    }
}
