package template.problem;

import template.binary.CachedLog2;
import template.math.Modular;
import template.polynomial.FastFourierTransform;
import template.polynomial.NumberTheoryTransform;

import java.util.Arrays;

/**
 * 1 * x1 + 2 * x2 + ... + n * xn = m while m <= n and xi >= 0
 */
public class WayToSplitNumber {
    public static void main(String[] args){
        System.out.println(new WayToSplitNumber(20, new Modular(1e9 + 7)).wayOf(20));
    }

    int[] ways;
    int[] f;
    int[] g;
    int n;
    Modular mod;

    private void prepare(int n, Modular mod) {
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
                    a[j] = mod.plus(a[j], a[j - i]);
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
                b[j] = mod.plus(b[j], b[j - i]);
            }
            for (int j = 0; j <= n; j++) {
                g[j] = mod.plus(g[j], b[j]);
            }
        }
    }

    /**
     * 1 * x1 + 2 * x2 + ... + n * xn = m while m <= n and xi >= 0
     */
    public WayToSplitNumber(int n, Modular mod, NumberTheoryTransform ntt) {
        prepare(n, mod);

        int m = CachedLog2.ceilLog(n * 2 + 1);
        ways = new int[1 << m];
        f = Arrays.copyOf(f, 1 << m);
        g = Arrays.copyOf(g, 1 << m);
        ntt.dft(f, m);
        ntt.dft(g, m);
        ntt.dotMul(f, g, ways, m);
        ntt.idft(ways, m);
    }

    public WayToSplitNumber(int n, Modular mod) {
        prepare(n, mod);

        ways = FastFourierTransform.multiplyMod(f, g, mod.getMod());
    }

    public int wayOf(int i) {
        return ways[i];
    }
}
