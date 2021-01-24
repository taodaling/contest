package template.problem;

import template.math.DigitUtils;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

/**
 * 1 * x1 + 2 * x2 + ... + n * xn = m while m <= n and xi >= 0
 * <br>
 * n^1.5
 */
public class WayToSplitNumber {
    public static void main(String[] args) {
        int mod = (int)1e9 + 7;
        System.out.println(new WayToSplitNumber(19, mod, new IntPolyFFT(mod)).wayOf(80));
    }

    int[] ways;
    int[] f;
    int[] g;
    int n;
    int mod;

    private void prepare(int n, int mod) {
        this.n = n;
        this.mod = mod;

        int k = (int) Math.ceil(Math.sqrt(n));
        int[] a = new int[n + 1];
        int[] b = new int[n + 1];
        f = a;
        g = new int[n + 1];

        a[0] = 1;
        for (int i = 1; i < k; i++) {
            for (int j = 0; j <= n; j++) {
                if (j - i >= 0) {
                    a[j] = DigitUtils.modplus(a[j], a[j - i], mod);
                }
            }
        }

        b[0] = 1;
        g[0] = 1;
        for (int i = 1; i <= n / k; i++) {
            for (int j = n; j >= 0; j--) {
                b[j] = j >= k ? b[j - k] : 0;
            }
            for (int j = i; j <= n; j++) {
                b[j] = DigitUtils.modplus(b[j], b[j - i], mod);
            }
            for (int j = 0; j <= n; j++) {
                g[j] = DigitUtils.modplus(g[j], b[j], mod);
            }
        }
    }

    /**
     * 1 * x1 + 2 * x2 + ... + n * xn = m while m <= n and xi >= 0
     */
    public WayToSplitNumber(int n, int mod, IntPoly poly) {
        prepare(n, mod);

        int[] fp = PrimitiveBuffers.allocIntPow2(f);
        int[] gp = PrimitiveBuffers.allocIntPow2(g);
        int[] wp = poly.convolution(fp, gp);

        ways = Arrays.copyOf(wp, n + 1);
    }

    public int wayOf(int i) {
        return ways[i];
    }
}
