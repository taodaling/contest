package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class BZOJ2987 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long A = in.rl();
        long B = in.rl();
        long C = in.rl();
        long ans = IntMath.sumFloorArithmeticSequenceBeta(C / A, -A, C + B, B);
        out.println(ans);
    }
}
