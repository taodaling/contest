package template.math;

import java.math.BigInteger;

public class BigCombination {
    static BigInteger[][] cache = new BigInteger[256][256];

    public static BigInteger combination(long m, long n) {
        n = Math.min(n, m - n);
        if (m < n) {
            return BigInteger.ZERO;
        }
        if (m < 256 && cache[(int) m][(int) n] != null) {
            return cache[(int) m][(int) n];
        }
        BigInteger ans;
        if (n == 0) {
            ans = BigInteger.ONE;
        } else {
            ans = combination(m - 1, n - 1).multiply(BigInteger.valueOf(m)).divide(BigInteger.valueOf(n));
        }
        if (m < 256) {
            cache[(int) m][(int) n] = ans;
        }
        return ans;
    }
}
