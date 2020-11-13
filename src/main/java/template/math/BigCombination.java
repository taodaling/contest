package template.math;

import java.math.BigInteger;

public class BigCombination {
    public static BigInteger combination(long m, long n) {
        n = Math.min(n, m - n);
        if (m < n) {
            return BigInteger.ZERO;
        }
        if (n == 0) {
            return BigInteger.ONE;
        }
        return combination(m - 1, n - 1).multiply(BigInteger.valueOf(m)).divide(BigInteger.valueOf(n));
    }
}
