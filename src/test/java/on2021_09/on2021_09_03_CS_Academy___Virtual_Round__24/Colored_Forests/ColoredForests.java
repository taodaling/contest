package on2021_09.on2021_09_03_CS_Academy___Virtual_Round__24.Colored_Forests;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.utils.Debug;
import template.utils.PrimitiveBuffers;

public class ColoredForests {
    int mod = 924844033;
    int[] T;
    Power pow = new Power(mod);
    IntPoly poly = new IntPolyFFT(mod);
    FastPow2[] fps;
    Factorial fact = new Factorial((int) 2e5, mod);
    Combination comb = new Combination(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        fps = new FastPow2[m + 1];
        for (int i = 0; i <= m; i++) {
            fps[i] = new FastPow2(i, mod, n, CachedEulerFunction.get(mod));
        }
        int[] c = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            c[i] = DigitUtils.mod(color(i, m), mod);
        }
        T = new int[n + 1];
        T[1] = 1 * c[1];
        for (int i = 2; i <= n; i++) {
            T[i] = (int) (pow.pow(i, i - 2) * (long) c[i] % mod * fact.invFact(i - 1) % mod);
        }
        int[] f = new int[n + 1];
        f[0] = 1;
        dac(f, 0, n);
        debug.debug("f", f);
        debug.debug("c", c);
        for (int i = 1; i <= n; i++) {
            long way = f[i];
            way = DigitUtils.mod(way, mod);
            out.println(way);
        }
    }

    Debug debug = new Debug(false);

    public void dac(int[] f, int l, int r) {
        if (l == r) {
            if (l > 0) {
                f[l] = (int) (f[l] * (long) fact.fact(l - 1) % mod);
            }
            return;
        }
        int m = (l + r) / 2;
        dac(f, l, m);
        int[] left = PrimitiveBuffers.allocIntPow2(m - l + 1);
        for (int i = l; i <= m; i++) {
            left[i - l] = (int) (f[i] * (long) fact.invFact(i) % mod);
        }
        int[] right = PrimitiveBuffers.allocIntPow2(T, r - l + 1);
        int[] conv = poly.convolution(left, right);
        for (int i = m + 1; i <= r && i - l < conv.length; i++) {
            f[i] += conv[i - l];
            if (f[i] >= mod) {
                f[i] -= mod;
            }
        }
        PrimitiveBuffers.release(left, right, conv);
        dac(f, m + 1, r);
    }

    public long color(int n, int m) {
        long sum = 0;
        for (int i = 0; i <= m; i++) {
            long contrib = comb.combination(m, i) * (long) fps[m - i].pow(n) % mod;
            if ((i & 1) == 1) {
                contrib = -contrib;
            }
            sum += contrib;
        }
        return sum % mod;
    }
}
