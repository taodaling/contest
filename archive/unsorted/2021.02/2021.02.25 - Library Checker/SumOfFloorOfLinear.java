package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EuclidLikeFunction;

public class SumOfFloorOfLinear {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl() - 1;
        long c = in.rl();
        long a = in.rl();
        long b = in.rl();
        long ans = EuclidLikeFunction.f(n, a, b, c);
        out.println(ans);
    }
}
