package template.math;


import template.binary.Log2;
import template.polynomial.NumberTheoryTransform;

/**
 * for all i, prepare all s(n,i) in O(nlog2n) time complexity
 */
public class SecondStirlingNumber {
    private NumberTheoryTransform ntt;
    private Modular mod;
    private Factorial factorial;
    private Log2 log2 = new Log2();
    private Power power;
    private int[] stirling;

    public int stirling(int i) {
        return stirling[i];
    }

    public SecondStirlingNumber(NumberTheoryTransform ntt, Factorial factorial, int n) {
        this.ntt = ntt;
        this.mod = factorial.getModular();
        this.factorial = factorial;
        power = new Power(factorial.getModular());
        stirling = getStirling(n);
    }

    private int[] getStirling(int n) {
        int m = log2.ceilLog(n + 1) + 1;
        int proper = 1 << m;
        int[] a = new int[proper];
        int[] b = new int[proper];

        for (int i = 0; i <= n; i++) {
            a[i] = factorial.inv[i];
            if (i % 2 == 1) {
                a[i] = mod.valueOf(-a[i]);
            }
            b[i] = factorial.inv[i];
            b[i] = mod.mul(b[i], power.pow(i, n));
        }

        ntt.dft(a, m);
        ntt.dft(b, m);
        ntt.dotMul(a, b, a, m);
        ntt.idft(a, m);
        return a;
    }
}
