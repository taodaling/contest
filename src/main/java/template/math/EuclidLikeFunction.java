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
}
