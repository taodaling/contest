package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class ABC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long l = in.ri();
        long r = in.ri();
        long R = r - 2 * l + 1;
        long L = r - 2 * l - (r - l) + 1;
        L = Math.max(L, 0);
        long sum = IntMath.sumOfInterval(L, R);
        out.println(sum);
    }
}
