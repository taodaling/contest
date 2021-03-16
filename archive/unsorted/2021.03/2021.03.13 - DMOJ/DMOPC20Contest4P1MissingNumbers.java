package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.IntMath;

public class DMOPC20Contest4P1MissingNumbers {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        long s = in.rl();
        long exp = IntMath.sumOfInterval(1, n);
        long delta = exp - s;
        if (delta < 3) {
            out.println(0);
            return;
        }
        if (delta > n + (n - 1)) {
            out.println(0);
            return;
        }
        long l = Math.max(1, delta - n);
        long r = Math.min(n, delta - 1);
        if (l > r) {
            out.println(0);
            return;
        }
        long ans = (r - l + 1) / 2;
        out.println(ans);
    }
}
