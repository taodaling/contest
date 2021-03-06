package template.math;

import java.util.function.LongUnaryOperator;

public class ExpressionSolver {
    private static LongExtGCDObject extGCD = new LongExtGCDObject();
    private static LinearFunctionCut range = new LinearFunctionCut();


    /**
     * Find how many pair (x,y) exists while following constraints obeyed.
     *
     * <pre>
     * ax+by=c
     * xl<=x<=xr
     * yl<=y<=yr
     * x is an integer
     * y is an integer
     * </pre>
     * <p>
     * The time complexity of this method is O(log2(max(a,b)))</p>
     */
    public static long findWaysToAssignXYSatisfyEquation(long a, long b, long c, long xl, long xr, long yl, long yr) {
        if (a == 0 && b == 0) {
            if (c == 0 && xr >= xl && yr >= yl) {
                return (xr - xl + 1) * (yr - yl + 1);
            }
            return 0;
        }
        if (a < 0) {
            a = -a;
            b = -b;
            c = -c;
        }
        long g = extGCD.extgcd(a, Math.abs(b));
        if (c % g != 0) {
            return 0;
        }
        a /= g;
        b /= g;
        c /= g;
        long x0 = extGCD.getX() * c;
        long y0 = extGCD.getY() * c;
        if (b < 0) {
            y0 = -y0;
        }
        range.reset();
        range.between(b, x0, xl, xr);
        range.between(-a, y0, yl, yr);
        return range.length();
    }

    /**
     * 1 + 2 + ... + n
     */
    public static long sumOfIncrementSequence(long n) {
        return (1 + n) * n / 2;
    }

    /**
     * l + (l + 1) + ... + (r - 1) + r
     */
    public static long sumOfIncrementSequence(long l, long r) {
        return sumOfIncrementSequence(r) - sumOfIncrementSequence(l - 1);
    }

    /**
     * sum_{i=0}^y \lfloor i/x \rfloor
     */
    public static long sumOfImmutableDenominator(long x, long y) {
        long round = y / x;
        long extra = y - round * x + 1;
        return sumOfIncrementSequence(round - 1) * x + extra * round;
    }

    /**
     * sum_{i=b}^y \lfloor i/x \rfloor
     */
    public static long sumOfImmutableDenominator(long b, long x, long y) {
        if (b == 0) {
            return sumOfImmutableDenominator(x, y);
        }
        return sumOfImmutableDenominator(x, y) - sumOfImmutableDenominator(x, b - 1);
    }


    /**
     * sum_{i=l}^r function(\lfloor m / i \rfloor)
     */
    public static long sumOfFunctionFloorDiv(LongUnaryOperator function, long l, long r, long m) {
        long ans = 0;
        for (long i = l, j; i <= r; i = j + 1) {
            j = Math.min(r, m / (m / i));
            ans += function.applyAsLong(i) * (j - i + 1);
        }
        return ans;
    }

    /**
     * Return an integer interval [l, r], for any element x in this interval, denote y = n % x. Following expression always true:
     * <pre>
     * \lfloor n / x \rfloor = d
     * lx <= x <= rx
     * ly <= y <= ry
     * <pre/>
     * If there is no answer, then ans[0] > ans[1]
     * <br>
     *  You should promise n, lx, rx, ly, ry >= 0, d > 0
     */
    public static void findRangeForDivisor(long n, long d, long lx, long rx, long ly, long ry, long[] ans) {
        range.reset(lx, rx);
        range.lessThanOrEqual(d, n);
        range.greaterThan(d + 1, n);
        range.between(-d, n, ly, ry);
        ans[0] = range.getL();
        ans[1] = range.getR();
    }
}
