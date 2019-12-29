package template.math;

import template.utils.SequenceUtils;

public class MinimumFraction {
    /**
     * O(log n) Find minimum x,y>0 satisfy b/a < y/x < d/c, if lEq is true, then we allow b/a = y/x, the same if rEq is true, we
     * allow y/x=d/c.
     * <br>
     * you should ensure a>0, b>=0, c>0, d>=0 and there is a valid solution.
     * <br>
     * return value is [x,y]
     */
    public static long[] findMinimumFraction(long a, long b, long c, long d, boolean lEq, boolean rEq) {
        if (lEq && b % a == 0) {
            return new long[]{1, b / a};
        }
        long left = b / a + 1;
        long right = rEq ? d / c : DigitUtils.ceilDiv(d, c) - 1;
        if (left <= right) {
            return new long[]{1, left};
        }
        long integer = left - 1;
        b -= integer * a;
        d -= integer * c;
        if (b == 0) {
            long[] ans = new long[2];
            ans[1] = 1;
            if (rEq && c % d == 0) {
                ans[0] = c / d;
            } else {
                ans[0] = c / d + 1;
            }
            ans[1] += ans[0] * integer;
            return ans;
        }
        long[] ans = findMinimumFraction(d, c, b, a, rEq, lEq);
        SequenceUtils.swap(ans, 0, 1);
        ans[1] += ans[0] * integer;
        return ans;
    }
}
