package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

public class ASimpleMath2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        int m = in.ri();
        Power pow = new Power(m * m);
        int ans = pow.pow(10, n) / m;
        out.println(ans);
    }
}
