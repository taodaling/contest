package template.math;

import template.polynomial.IntPoly;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

/**
 * 计算C(n,0),C(n,1),...,C(n,m)在模p的情况下的值，其中p不一定是素数。
 * <p>
 * (x+1)^n=\sum_{i=0}^n {n \choose i} x^i
 */
public class BinomialCombination {
    private int[] c;

    /**
     * O(m\log_2m)
     * @param n
     * @param m
     * @param poly
     */
    public BinomialCombination(long n, int m, IntPoly poly) {
        int[] p = PrimitiveBuffers.allocIntPow2(2);
        p[0] = p[1] = 1;
        int[] pow = poly.modpowByLnExp(p, n, m + 1);
        c = Arrays.copyOf(pow, m + 1);
        PrimitiveBuffers.release(p, pow);
    }

    public int get(int i) {
        assert i < c.length;
        return c[i];
    }
}
