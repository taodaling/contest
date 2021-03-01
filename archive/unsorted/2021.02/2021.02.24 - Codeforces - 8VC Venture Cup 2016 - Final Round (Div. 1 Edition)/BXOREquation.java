package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BXOREquation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long sum = in.rl();
        long xor = in.rl();
        long and = sum - xor;
        if (and < 0 || and % 2 != 0) {
            out.println(0);
            return;
        }
        and /= 2;
        if ((and & xor) > 0) {
            out.println(0);
            return;
        }
        int bit = Long.bitCount(xor);
        long ans = 1L << bit;
        if (and == 0) {
            ans -= 2;
        }
        out.println(ans);
    }
}
