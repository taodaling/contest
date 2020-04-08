package template.math;

public class GrayCode {
    public static int transform(int x) {
        return x ^ (x >>> 1);
    }

    public static int inverse(int x) {
        int y = 0;
        for (; x != 0; x >>>= 1) {
            y ^= x;
        }
        return y;
    }
}
