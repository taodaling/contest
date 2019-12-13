package template.math;

public class ExpressionSolver {
    private static ExtGCD extGCD = new ExtGCD();

    /**
     * Find ka=b(mod c) where k is the minimum possible non negative integer. <br>
     * If it's impossible, -1 will be returned.
     * <br>
     * The time complexity of this method is O(log2(max(a,b)))
     */
    public long findMinimumKForModEquation(long a, long b, long c) {
        a = DigitUtils.mod(a, c);
        b = DigitUtils.mod(b, c);
        int g = (int) extGCD.extgcd((int) a, c);
        if (b % g != 0) {
            return -1;
        }
        long m = c / g;
        return DigitUtils.mod(b / g * extGCD.getX(), m);
    }


    /**
     * Find how many pair xy exists while following constraints obeyed.
     *
     * <pre>
     * ax+by=c
     * xl<=x<=xr
     * yl<=y<=yr
     * x is an integer
     * y is an integer
     * </pre>
     * <p>
     * The time complexity of this method is O(log2(max(a,b)))
     */
    public long findWaysToAssignXYSatisfyEquation(long a, long b, long c, long xl, long xr, long yl, long yr) {
        if (c < 0) {
            a = -a;
            b = -b;
            c = -c;
        }
        if (a < 0) {
            a = -a;
            long l = xl;
            long r = xr;
            xl = -r;
            xr = -l;
        }
        if (b < 0) {
            b = -b;
            long l = yl;
            long r = yr;
            yl = -r;
            yr = -l;
        }
        if (xl > xr || yl > yr) {
            return 0;
        }
        if (a == 0 && b == 0) {
            if (c != 0) {
                return 0;
            }
            return (xr - xl + 1) * (yr - yl + 1);
        }
        if (a == 0) {
            if (c % b != 0) {
                return 0;
            }
            long y = c / b;
            if (y < yl || y > yr) {
                return 0;
            }
            return xr - xl + 1;
        }
        if (b == 0) {
            if (c % a != 0) {
                return 0;
            }
            long x = c / a;
            if (x < xl || x > xr) {
                return 0;
            }
            return yr - yl + 1;
        }
        long g = extGCD.extgcd(a, b);
        if (c % g != 0) {
            return 0;
        }
        a /= g;
        b /= g;
        c /= g;
        long x = extGCD.getX() * c;
        long y = extGCD.getY() * c;
        if (x < xl) {
            // x + kb >= xl
            // k >= (xl - x)/b
            long k = DigitUtils.ceilDiv(xl - x, b);
            x += k * b;
            y -= k * a;
        } else {
            // x - kb >= xl
            // (x - xl)/b>=k
            long k = DigitUtils.floorDiv(x - xl, b);
            x -= k * b;
            y += k * a;
        }

        if (y > yr) {
            // y - ka <= yr
            // k >= (y - yr)/a
            long k = DigitUtils.ceilDiv(y - yr, a);
            x += k * b;
            y -= k * a;
        }

        if (x > xr || y < yl) {
            return 0;
        }

        long xSpare = Math.max(0, (xr - x) / b);
        long ySpare = Math.max(0, (y - yl) / a);
        return 1 + Math.min(xSpare, ySpare);
    }
}
