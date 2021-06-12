package template.polynomial;

import template.binary.Log2;

import java.util.Arrays;

public class FastSubsetTransform {
    /**
     * res[k] = \sum_{i&j=0 and i|j=k} a[i]b[j]
     *
     * O(n (log_2n)^2)
     * @param a
     * @param b
     * @return
     */
    public static int[] mul(int[] a, int[] b, int mod) {
        assert (1 << Log2.ceilLog(a.length)) == a.length;
        assert a.length == b.length;
        int log = Log2.ceilLog(a.length);
        int n = a.length;
        int[][] fwta = new int[log + 1][a.length];
        int[][] fwtb = new int[log + 1][b.length];
        int[] c = new int[a.length];
        int[] T = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            int bitcount = Integer.bitCount(i);
            fwta[bitcount][i] = a[i];
            fwtb[bitcount][i] = b[i];
        }
        for (int i = 0; i <= log; i++) {
            FastWalshHadamardTransform.orFWT(fwta[i], 0, n - 1, mod);
            FastWalshHadamardTransform.orFWT(fwtb[i], 0, n - 1, mod);
        }
        for (int i = 0; i <= log; i++) {
            Arrays.fill(T, 0);
            for (int j = 0; j <= i; j++) {
                int k = i - j;
                FastWalshHadamardTransform.dotMulPlus(fwta[j], fwtb[k], T, 0, n - 1, mod);
            }
            FastWalshHadamardTransform.orIFWT(T, 0, n - 1, mod);
            for (int j = 0; j < n; j++) {
                if (Integer.bitCount(j) == i) {
                    c[j] = T[j];
                }
            }
        }
        return c;
    }
}
