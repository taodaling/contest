package template.math;

import template.polynomial.ModContinuousInterpolation;

public class IntMath {
    private IntMath() {
    }

    /**
     * l + (l + 1) + ... + r
     */
    public static long sumOfInterval(long l, long r) {
        if (l > r) {
            return 0;
        }
        long a = r + l;
        long b = r - l + 1;
        if ((a & 1) == 0) {
            return (a >> 1) * b;
        } else {
            return a * (b >> 1);
        }
    }

    /**
     * l^2 + (l + 1)^2 + ... + r^2
     */
    public static long sumOfInterval2(long l, long r) {
        if (r < l) {
            return 0;
        }
        return (r * (r + 1) * (2 * r + 1) - (l - 1) * l * (2 * l - 1)) / 6;
    }

    /**
     * 1^2 + 2^2 + ... + n^2
     */
    public static long sumOfInterval2(long n) {
        if (n <= 0) {
            return 0;
        }
        return n * (n + 1) * (2 * n + 1) / 6;
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

    public static long pow2(long x) {
        return x * x;
    }

    public static int ceilSqrt(long x) {
        int ans = floorSqrt(x);
        if ((long) ans * ans < x) {
            ans++;
        }
        return ans;
    }

    public static int floorSqrt(long x) {
        int lo = 0;
        int hi = (int) 2e9;

        while (lo < hi) {
            int mid = (lo + hi + 1) >> 1;
            if ((long) mid * mid <= x) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }

        return lo;
    }

    /**
     * <p>
     * \sum_{i=0}^{\infty} f(i) r^i % mod
     * </p>
     * <p>
     * f is a polynomial, denote d as the degree of f, init[i] = f(i) for 0 <= i <= d
     * </p>
     * <p>
     * O(d + \log_2 mod) time and space complexity, O(d) invocation of comb objet
     * </p>
     * <pre>
     * reference:
     * - https://hitonanode.github.io/cplib-cpp/formal_power_series/sum_of_exponential_times_polynomial_limit.hpp
     * </pre>
     *
     * @param r    r != 1
     * @param mod  gcd(1 - r, mod) = 1
     * @param comb comb(i, j), 0 <= j <= i <= d + 1
     */
    public static int sumOfExponentialTimesPolynomial(int r, int[] init, int mod, IntCombination comb) {
        assert r != 1;
        if (init.length == 0) {
            return 0;
        }
        int inv = (int) DigitUtils.modInverse(DigitUtils.mod(1 - r, mod), mod);
        if (init.length == 1) {
            return (int) ((long) init[0] * inv % mod);
        }
        int[] bs = init.clone();
        int d = init.length - 1;
        long rp = 1;
        for (int i = 1; i <= d; i++) {
            rp = rp * r % mod;
            bs[i] = (int) (((long) bs[i] * rp + bs[i - 1]) % mod);
        }
        long ret = 0;
        rp = 1;
        for (int i = 0; i <= d; i++) {
            ret += (long) bs[d - i] * comb.combination(d + 1, i) % mod * rp % mod;
            rp = rp * -r % mod;
        }
        ret = ret % mod * DigitUtils.modPow(inv, d + 1, mod) % mod;
        return DigitUtils.mod(ret, mod);
    }

    /**
     * <p>
     * \sum_{i=0}^{n-1} f(i) r^i % mod
     * </p>
     * <p>
     * f is a polynomial, denote d as the degree of f, init[i] = f(i) for 0 <= i <= d
     * </p>
     * <p>
     * O(d + \log_2 mod) time and space complexity, O(d) invocation of fact objet
     * </p>
     * <pre>
     * reference:
     * - https://hitonanode.github.io/cplib-cpp/formal_power_series/sum_of_exponential_times_polynomial.hpp
     * </pre>
     *
     * @param r    r != 1
     * @param mod  should be a prime
     * @param fact fact(i), 0 <= i <= d + 1
     */
    public static int sumOfExponentialTimesPolynomial(int r, int[] init, int mod, Factorial fact, long n) {
        if (n <= 0) {
            return 0;
        }
        if (r == 0) {
            if (init.length <= 0) {
                return 0;
            }
            return DigitUtils.mod(init[0], mod);
        }
        if (init.length == 1) {
            long ans;
            if (r == 1) {
                ans = DigitUtils.mod(n, mod);
            } else {
                long inv = DigitUtils.modInverse(DigitUtils.mod(1 - r, mod), mod);
                ans = inv * (1 - DigitUtils.modPow(r, n, mod)) % mod;
            }
            ans = ans * init[0] % mod;
            return DigitUtils.mod(ans, mod);
        }

        int[] S = new int[init.length + 1];
        long rp = 1;
        for (int i = 0; i < init.length; i++) {
            S[i + 1] = DigitUtils.mod(S[i] + init[i] * rp, mod);
            rp = rp * r % mod;
        }
        if (n < S.length) {
            return DigitUtils.mod(S[(int) n], mod);
        }

        ModContinuousInterpolation interpolation = new ModContinuousInterpolation(S.length, fact);
        if (r == 1) {
            return interpolation.interpolate(0, S, S.length, n);
        }

        int Sinf = sumOfExponentialTimesPolynomial(r, init, mod, new Combination(fact));
        long rinv = DigitUtils.modInverse(r, mod);
        long rinvp = 1;
        for (int i = 0; i < S.length; i++) {
            S[i] = DigitUtils.mod((S[i] - Sinf) * rinvp, mod);
            rinvp = rinvp * rinv % mod;
        }
        long ans = (long) interpolation.interpolate(0, S, S.length, n) * DigitUtils.modPow(r, n, mod) + Sinf;
        ans = DigitUtils.mod(ans, mod);
        return (int) ans;
    }
}
