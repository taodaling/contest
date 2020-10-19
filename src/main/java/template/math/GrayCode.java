package template.math;

public class GrayCode {
    public static long apply(long x) {
        return x ^ (x >>> 1);
    }

    public static long inverse(long x) {
        long y = 0;
        for (; x != 0; x >>>= 1) {
            y ^= x;
        }
        return y;
    }
}
