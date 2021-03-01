package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;
import template.utils.Debug;

public class SumOfFloorOfLinear {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl() - 1;
        long c = in.rl();
        long a = in.rl();
        long b = in.rl();
        long ans = IntMath.sumFloorArithmeticSequenceBeta(n, a, b, c);
        debug.debug("ans", ans);
        out.println(ans);
    }
}
