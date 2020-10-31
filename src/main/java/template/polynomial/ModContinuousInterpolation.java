package template.polynomial;

import template.math.DigitUtils;
import template.math.Factorial;

public class ModContinuousInterpolation {
    private int mod;
    private int[] pre;
    private int[] post;
    private Factorial fact;

    public ModContinuousInterpolation(int maxN, int mod) {
        this(maxN, new Factorial(maxN + 10, mod));
    }

    /**
     * <pre>
     * Given n points, the i-th points located at (x0+i,y[i])
     * </pre>
     * <p>
     * precondition: fact are supposed to support [0, n - 1]
     * </p>
     */
    public ModContinuousInterpolation(int maxN, Factorial fact) {
        this.mod = fact.getMod();
        pre = new int[maxN + 10];
        post = new int[maxN + 10];
        this.fact = fact;
    }

    /**
     * O(n)
     */
    public int interpolate(int x0, int[] y,  int n, int x) {
        x = DigitUtils.modsub(x, x0, mod);
        if (x < n) {
            return y[x];
        }

        for (int i = 0; i < n; i++) {
            pre[i] = post[i] = DigitUtils.modsub(x, i, mod);
        }
        for (int i = 1; i < n; i++) {
            pre[i] = (int) ((long) pre[i - 1] * pre[i] % mod);
            post[n - i - 1] = (int) ((long) post[n - i - 1] * post[n - i] % mod);
        }

        long sum = 0;
        for (int i = 0; i < n; i++) {
            int local = (int) ((long) y[i] * fact.invFact(i) % mod * fact.invFact(n - 1 - i) % mod);
            if (i > 0) {
                local = (int) ((long) local * pre[i - 1] % mod);
            }
            if (i + 1 < n) {
                local = (int) ((long) local * post[i + 1] % mod);
            }
            if (((n - 1 - i) & 1) == 1) {
                local = DigitUtils.negate(local, mod);
            }
            sum += local;
        }

        return (int) (sum % mod);
    }
}
