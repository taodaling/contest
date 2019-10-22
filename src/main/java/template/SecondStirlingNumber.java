package template;


/**
 * for all i, prepare all s(n,i) in O(nlog2n) time complexity
 */
public class SecondStirlingNumber {
    private NumberTheoryTransform ntt;
    private NumberTheory.Modular mod;
    private NumberTheory.Factorial factorial;
    private DigitUtils.Log2 log2 = new DigitUtils.Log2();
    private NumberTheory.Power power = new NumberTheory.Power(mod);
    private int[] stirling;

    public int stirling(int i){
        return stirling[i];
    }

    public SecondStirlingNumber(NumberTheoryTransform ntt, NumberTheory.Factorial factorial, int n) {
        this.ntt = ntt;
        this.mod = factorial.getModular();
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

        ntt.prepareReverse(r, m);
        ntt.dft(r, a, m);
        ntt.dft(r, b, m);
        ntt.dotMul(a, b, a, m);
        ntt.idft(r, a, m);
        return a;
    }
}
