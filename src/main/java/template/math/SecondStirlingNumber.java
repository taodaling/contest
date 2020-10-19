package template.math;


import template.binary.Log2;
import template.polynomial.IntPoly;
import template.polynomial.NumberTheoryTransform;
import template.polynomial.Polynomials;
import template.utils.PrimitiveBuffers;

import java.rmi.dgc.DGC;
import java.util.Arrays;

/**
 * for all i, prepare all s(n,i) in O(nlog2n) time complexity
 */
public class SecondStirlingNumber {
    private int mod;
    private int[] stirling;

    public int stirling(int i) {
        return stirling[i];
    }

    public SecondStirlingNumber(Factorial factorial, int n, IntPoly poly) {
        this.mod = factorial.getMod();
        stirling = getStirling(n, factorial, poly, new Power(mod));
    }

    private int[] getStirling(int n, Factorial factorial, IntPoly poly, Power power) {
        int[] a = PrimitiveBuffers.allocIntPow2(n + 1);
        int[] b = PrimitiveBuffers.allocIntPow2(n + 1);

        for (int i = 0; i <= n; i++) {
            a[i] = factorial.invFact(i);
            if (i % 2 == 1) {
                a[i] = DigitUtils.negate(a[i], mod);
            }
            b[i] = DigitUtils.mod((long) factorial.invFact(i) * power.pow(i, n), mod);
        }

        int[] c = poly.convolution(a, b);
        int[] ans = Arrays.copyOf(c, n + 1);
        PrimitiveBuffers.release(a, b, c);
        return ans;
    }
}
