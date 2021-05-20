package template.math;

public class EuclidLikeFunction {
    /**
     * c > 0
     * \sum_{i=0}^n \floor((ai+b)/c)
     * O(\log_2a+\log_2c)
     */
    public static long f(long n, long a, long b, long c) {
        assert n >= 0;
        if (a == 0) {
            return (n + 1) * (b / c);
        }
        if (a >= c || b >= c) {
            return n * (n + 1) / 2 * (a / c) + (n + 1) * (b / c) + f(n, a % c, b % c, c);
        }
        long m = (a * n + b) / c;
        if (m == 0) {
            return 0;
        }
        return n * m - f(m - 1, c, c - b - 1, a);
    }

    /**
     * <p>
     * \sum_{i=0}^n \floor((ai+b)/c) i
     * </p>
     *
     * @param n
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long g(long n, long a, long b, long c) {
        assert n >= 0;
        if (a == 0) {
            return IntMath.sumOfInterval(0, n) * (b / c);
        }
        if (n == 0) {
            return 0;
        }
        if (a >= c || b >= c) {
            return g(n, a % c, b % c, c) + n * (n + 1) * (2 * n + 1) / 6 * (a / c)
                    + n * (n + 1) / 2 * (b / c);
        }
        long m = (a * n + b) / c;
        if (m == 0) {
            return 0;
        }
        long ans = m * n * (n + 1) - f(m - 1, c, -b + c - 1, a) - h(m - 1, c, -b + c - 1, a);
        return ans / 2;
    }

    /**
     * <p>
     * \sum_{i=0}^n \floor((ai+b)/c)^2
     * </p>
     * formula from
     * https://blog.csdn.net/dreaming__ldx/article/details/86768953?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.control&dist_request_id=9f6059c1-08e6-4621-bac5-389e05276d86&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-3.control
     *
     * @param n
     * @param a
     * @param b
     * @param c
     * @return
     */
    public static long h(long n, long a, long b, long c) {
        assert n >= 0;
        if (a == 0 || n == 0) {
            return (b / c) * (b / c) * (n + 1);
        }
        if (a >= c || b >= c) {
            return h(n, a % c, b % c, c) + n * (n + 1) * (2 * n + 1) / 6 * IntMath.pow2(a / c) +
                    (n + 1) * IntMath.pow2(b / c) + 2 * (b / c) * f(n, a % c, b % c, c) +
                    2 * (a / c) * g(n, a % c, b % c, c) + (a / c) * (b / c) * n * (n + 1);
        }
        long m = (a * n + b) / c;
        if (m == 0) {
            return 0;
        }
        return m * (m + 1) * n - 2 * g(m - 1, c, -b + c - 1, a) - 2 * f(m - 1, c, -b + c - 1, a)
                - f(n, a, b, c);
    }

    /**
     * Find minimum non-negative x that L <= ax + b % m <= R
     * <p>
     * O(\log_2m)
     * <p>
     * https://codeforces.com/blog/entry/90690
     *
     * @return -1 for no satisfied answer
     */
    public static long firstOccurResidue(long a, long b, long m, long L, long R) {
        L = Math.max(0, L);
        R = Math.min(m - 1, R);
        a = DigitUtils.mod(a, m);
        b = DigitUtils.mod(b, m);
        return firstOccurResidue0(a, b, m, L, R);
    }


    private static long firstOccurResidue0(long a, long b, long m, long L, long R) {
        if (L > R) {
            return -1;
        }
        long g = GCDs.gcd(a, m);
        if (g > 1) {
            return firstOccurResidue0(a / g, b / g, m / g, DigitUtils.ceilDiv(L - b % g, g), DigitUtils.floorDiv(R - b % g, g));
        }
        long ia = DigitUtils.modInverse(a, m);
        return minimumResidueOfLinearFunction(ia, (L - b) * ia, m, R - L);
    }

    /**
     * Find minimum value for ax+b % p where 0<=x<=k
     * <p>
     * O(\log_2m)
     * <p>
     * https://codeforces.com/blog/entry/90690
     *
     * @return
     */
    public static long minimumResidueOfLinearFunction(long a, long b, long m, long k) {
        assert k >= 0;
        return minimumResidueOfLinearFunction1(DigitUtils.mod(a, m), DigitUtils.mod(b, m), m, Math.min(k, m));
    }

    private static long stepRequire(long ia, long b0, long m0, long val) {
        long res = ia * (val - b0) % m0;
        if (res < 0) {
            res += m0;
        }
        return res;
    }

    private static long minimumResidueOfLinearFunction1(long a0, long b0, long m0, long k) {
        long g = GCDs.gcd(a0, m0);
        if (g > 1) {
            return minimumResidueOfLinearFunction1(a0 / g, b0 / g, m0 / g, k) * g + b0 % g;
        }
        long ia0 = DigitUtils.modInverse(a0, m0);
        long b = b0;
        long a = a0;
        long m = m0;
        for (long s; a != 0; b = s) {
            if (a * 2 <= m) {
                s = b < a ? b : DigitUtils.mod(b - m, a);
                if (stepRequire(ia0, b0, m0, s) > k) {
                    return b;
                }
                long tmp = m;
                m = a;
                a = DigitUtils.mod(-tmp, m);
            } else {
                long ma = m - a;
                s = b % ma;
                if (stepRequire(ia0, b0, m0, s) > k) {
                    long lo = 0;
                    long hi = b / ma - 1;
                    while (lo < hi) {
                        long mid = (lo + hi + 1) / 2;
                        if (stepRequire(ia0, b0, m0, b - ma * mid) > k) {
                            hi = mid - 1;
                        } else {
                            lo = mid;
                        }
                    }
                    return b - ma * lo;
                }
                m = ma;
                a %= ma;
            }
        }
        return b;
    }
}
