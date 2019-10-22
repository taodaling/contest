package template;

import java.util.Arrays;

/**
 * For all i, prepare c(n, i) in O(nlog2n)
 */
public class FirstStirlingNumber {
    private NumberTheory.Modular mod;
    private NumberTheoryTransform ntt;
    private DigitUtils.Log2 log2 = new DigitUtils.Log2();
    private NumberTheory.Factorial factorial;
    private int[] stirling;


    public FirstStirlingNumber(NumberTheoryTransform ntt, NumberTheory.Factorial factorial, int n) {
        this.ntt = ntt;
        this.factorial = factorial;
        stirling = getStirling(n);
    }

    private int[] getStirling(int n) {
        int proper = log2.ceilLog(n + 1);
        int[][] ans = new int[4][1 << proper];
        calcStirling(ans[0], ans[1], ans[2], ans[3], n);
        return ans[0];
    }

    private void calcStirling(int[] ans, int[] buf, int[] buf2, int[] r, int n) {
        if (n == 0) {
            ans[0] = 1;
            return;
        }
        if (n % 2 == 1) {
            calcStirling(ans, buf, buf2, r, n - 1);
            for (int i = n; i >= 0; i--) {
                ans[i] = mod.mul(n - 1, ans[i]);
                if (i >= 1) {
                    ans[i] = mod.plus(ans[i], ans[i - 1]);
                }
            }
            return;
        }
        int half = n / 2;
        calcStirling(ans, buf, buf2, r, half);
        int proper = log2.ceilLog((half + 1) * 2 - 1);
        ntt.prepareReverse(r, proper);
        Arrays.fill(buf, 0, 1 << proper, 0);
        Arrays.fill(buf2, 0, 1 << proper, 0);
        int ni = 1;
        for (int i = 0; i <= half; i++) {
            buf[i] = mod.mul(ans[i], factorial.fact[i]);
            buf2[i] = mod.mul(ni, factorial.inv[i]);
            ni = mod.mul(ni, half);
        }
        reverse(buf, 0, half);
        ntt.dft(r, buf, proper);
        ntt.dft(r, buf2, proper);
        ntt.dotMul(buf, buf2, buf, proper);
        ntt.idft(r, buf, proper);
        reverse(buf, 0, half);
        for (int i = 0; i <= half; i++) {
            buf[i] = mod.mul(buf[i], factorial.inv[i]);
        }
        Arrays.fill(buf, half + 1, 1 << proper, 0);
        ntt.dft(r, buf, proper);
        ntt.dft(r, ans, proper);
        ntt.dotMul(ans, buf, ans, proper);
        ntt.idft(r, ans, proper);
    }

    private void reverse(int[] data, int l, int r) {
        while (l < r) {
            int tmp = data[l];
            data[l] = data[r];
            data[r] = tmp;
            l++;
            r--;
        }

    }
}
