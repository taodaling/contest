package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;

public class ALengtheningSticks {
    public long alloc3(long x) {
        //x1+x2+x3=x
        return IntMath.sumOfInterval(1, x + 1);
    }

    public long mirror(long x, long m){
        return m + (m - x);
    }

    public long solveNegative(long l, long aLow, long bLow, long cLow, long d) {
        long ans = 0;
        for (long a = aLow; a < 0; a++) {
            long bAtLeast = Math.max(-a + d, bLow);
            long cAtLeast = Math.max(-a + d, cLow);
            long remain = l - a - bAtLeast - cAtLeast;
            if (remain < 0) {
                continue;
            }
            ans += alloc3(remain);
        }
        return ans;
    }

    public long solve(long l, long aLow, long bLow, long cLow, long d) {
        long ans = solveNegative(l, aLow, bLow, cLow, d)
                + solveNegative(l, bLow, aLow, cLow, d)
                + solveNegative(l, cLow, aLow, bLow, d);
        for (long a = Math.max(0, aLow); a <= l; a++) {
            long bAtLeast = Math.max(0, bLow);
            long cAtLeast = Math.max(0, cLow);
            long remain = l - a - bAtLeast - cAtLeast;
            if (remain < 0) {
                continue;
            }
            ans += alloc3(remain);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long A = in.rl();
        long B = in.rl();
        long C = in.rl();
        int l = in.ri();
        long aLow = -(B + C) + A + 1;
        long bLow = -(A + C) + B + 1;
        long cLow = -(B + A) + C + 1;

        //all even
        long even = solve(DigitUtils.floorDiv(l, 2), (long) Math.ceil(aLow / 2.0), (long) Math.ceil(bLow / 2.0), (long) Math.ceil(cLow / 2.0), 0);
        long odd = solve(DigitUtils.floorDiv(l - 3, 2), (long) Math.ceil((aLow - 1) / 2.0), (long) Math.ceil((bLow - 1) / 2.0),
                (long) Math.ceil((cLow - 1) / 2.0), -1);
        out.println(even + odd);
    }
}
