package template;


/**
 * for all i, prepare all s(n,i) in O(nlog2n) time complexity
 */
public class SecondStirlingNumber {
    private static NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
    private NumberTheory.Factorial factorial;
    private static NumberTheory.Log2 log2 = new NumberTheory.Log2();
    private static NumberTheory.Power power = new NumberTheory.Power(mod);
    private int[] stirling;

    public SecondStirlingNumber(NumberTheory.Factorial factorial, int n) {
        this.factorial = factorial;
        stirling = getStirling(n);
    }

    private int[] getStirling(int n) {
        int m = log2.ceilLog(n + 1) + 1;
        int proper = 1 << m;
        int[] a = new int[proper];
        int[] b = new int[proper];
        int[] r = new int[proper];

        for (int i = 0; i <= n; i++) {
            a[i] = factorial.inv[i];
            if (i % 2 == 1) {
                a[i] = mod.valueOf(-a[i]);
            }
            b[i] = factorial.inv[i];
            b[i] = mod.mul(b[i], power.pow(i, n));
        }

        NumberTheoryTransform.prepareReverse(r, m);
        NumberTheoryTransform.dft(r, a, m);
        NumberTheoryTransform.dft(r, b, m);
        NumberTheoryTransform.dotMul(a, b, a, m);
        NumberTheoryTransform.idft(r, a, m);
        return a;
    }
}
