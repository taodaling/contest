package template.math;

import template.utils.SequenceUtils;

public class ExtGCD {
    /**
     * ax+by=gcd(a,b), while (x=1 or |x|<b) and (y=1 or |y|<a)
     */
    public static int extGCD(int a, int b, int[] xy) {
        if (a >= b) {
            return extGCD0(a, b, xy);
        }
        int ans = extGCD0(b, a, xy);
        SequenceUtils.swap(xy, 0, 1);
        return ans;
    }

    private static int extGCD0(int a, int b, int[] xy) {
        if (b == 0) {
            xy[0] = 1;
            xy[1] = 0;
            return a;
        }
        int ans = extGCD0(b, a % b, xy);
        int x = xy[0];
        int y = xy[1];
        xy[0] = y;
        xy[1] = x - a / b * y;
        return ans;
    }

    /**
     * ax+by=gcd(a,b), while (x=1 or |x|<b) and (y=1 or |y|<a)
     */
    public static long extGCD(long a, long b, long[] xy) {
        if (a >= b) {
            return extGCD0(a, b, xy);
        }
        long ans = extGCD0(b, a, xy);
        SequenceUtils.swap(xy, 0, 1);
        return ans;
    }

    private static long extGCD0(long a, long b, long[] xy) {
        if (b == 0) {
            xy[0] = 1;
            xy[1] = 0;
            return a;
        }
        long ans = extGCD0(b, a % b, xy);
        long x = xy[0];
        long y = xy[1];
        xy[0] = y;
        xy[1] = x - a / b * y;
        return ans;
    }
}
