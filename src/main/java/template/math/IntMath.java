package template.math;

public class IntMath {
    private IntMath() {
    }

    /**
     * l + (l + 1) + ... + r
     */
    public static long sumOfInterval(long l, long r) {
        return (r + l) * (r - l + 1) / 2;
    }

    /**
     * <pre>
     * https://atcoder.jp/contests/practice2/submissions/16564332
     * find b that ab%mod=1
     * </pre>
     */
    public static long invl(long a, long mod) {
        long b = mod;
        long p = 1, q = 0;
        while (b > 0) {
            long c = a / b;
            long d;
            d = a;
            a = b;
            b = d % b;
            d = p;
            p = q;
            q = d - c * q;
        }
        return p < 0 ? p + mod : p;
    }


    /**
     * <pre>
     * https://judge.yosupo.jp/submission/21924
     * \sum_{i=0}^{n-1} floor((ai+b)/m)
     * fast enough for 500000 operation in one second
     * </pre>
     */
    public static long sumFloorArithmeticSequence(long n, long m, long a, long b) {
        long ans = 0;
        if (a >= m) {
            ans += (n - 1) * n * (a / m) / 2;
            a %= m;
        }
        if (b >= m) {
            ans += n * (b / m);
            b %= m;
        }
        long y_max = (a * n + b) / m, x_max = (y_max * m - b);
        if (y_max == 0) return ans;
        ans += (n - (x_max + a - 1) / a) * y_max;
        ans += sumFloorArithmeticSequence(y_max, a, m, (a - x_max % a) % a);
        return ans;
    }
}
