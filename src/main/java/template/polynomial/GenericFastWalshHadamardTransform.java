package template.polynomial;

import java.util.Arrays;

public class GenericFastWalshHadamardTransform {
    int[][] addC;
    int[][] iaddC;
    int k;
    long[] accum;
    long[][] buf;
    int[] phi;

    /**
     * radix k
     */
    public GenericFastWalshHadamardTransform(int k) {
        this.k = k;
        accum = new long[k * 2];
        addC = new int[k][k];
        iaddC = new int[k][k];
        buf = new long[k][k];
        phi = CyclotomicPolynomialBF.get(k).clone();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                addC[i][j] = i * j % k;
                iaddC[i][j] = (k - addC[i][j]) % k;
            }
        }
    }

    public void maxFWT(long[] a, int l, int r) {
        if (l == r) {
            return;
        }
        //presum
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;
        for (int i = 0; i < len; i += step) {
            maxFWT(a, l + i, l + i + step - 1);
        }
        for (int i = l + step; i <= r; i++) {
            a[i] += a[i - step];
        }
    }

    public void maxIFWT(long[] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        int step = len / k;
        for (int i = r; i >= l + step; i--) {
            a[i] -= a[i - step];
        }
        for (int i = 0; i < len; i += step) {
            maxIFWT(a, l + i, l + i + step - 1);
        }
    }

    public void minFWT(long[] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;
        for (int i = 0; i < len; i += step) {
            minFWT(a, l + i, l + i + step - 1);
        }
        for (int i = r - step; i >= l; i--) {
            a[i] += a[i + step];
        }
    }

    public void minIFWT(long[] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;
        for (int i = l; i <= r - step; i++) {
            a[i] -= a[i + step];
        }
        for (int i = 0; i < len; i += step) {
            minIFWT(a, l + i, l + i + step - 1);
        }
    }

    public void dotMul(long[] a, long[] b, long[] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            c[i] = a[i] * b[i];
        }
    }

    /**
     * O(k^2(r-l+1))
     */
    public void dotMul(long[][] a, long[][] b, long[][] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            mulAddTo(a, i, b, i);
            for (int j = 0; j < k; j++) {
                c[j][i] = accum[j];
            }
        }
    }

    public void pow(long[][] x, long[][] res, int l, int r, long n) {
        if (n == 0) {
            for (int i = 0; i < k; i++) {
                if (i == 0) {
                    Arrays.fill(res[i], 1);
                } else {
                    Arrays.fill(res[i], 0);
                }
            }
            return;
        }
        pow(x, res, l, r, n / 2);
        dotMul(res, res, res, l, r);
        if (n % 2 == 1) {
            dotMul(res, x, res, l, r);
        }
    }

    /**
     * dest += a[offset] * x^t (mod x^k - 1)
     */
    private void mulAddTo(long[][] a, int offset, int t, long[] dest) {
        for (int i = 0, to = t; i < k; i++, to = to + 1 == k ? 0 : to + 1) {
            dest[to] += a[i][offset];
        }
    }

    private void mulAddTo(long[][] a, int ai, long[][] b, int bi) {
        for (int i = 0; i < k; i++) {
            accum[i] = 0;
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                int ij = i + j;
                accum[ij] += a[i][ai] * b[j][bi];
            }
        }
        for (int i = 0; i < k; i++) {
            accum[i] += accum[i + k];
            accum[i + k] = 0;
        }
    }

    /**
     * a[k][r + 1]
     * <p>
     * O(k^2(r-l+1)\log_2(r-l+1))
     */
    public void addFWT(long[][] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;
        for (int i = 0; i < len; i += step) {
            addFWT(a, l + i, l + i + step - 1);
        }
        for (int i = 0; i < step; i++) {
            int first = l + i;
            for (int j = 0; j < k; j++) {
                for (int t = 0; t < k; t++) {
                    mulAddTo(a, t * step + first, addC[j][t], buf[j]);
                }
            }
            //copy back
            for (int j = 0; j < k; j++) {
                int index = j * step + first;
                for (int t = 0; t < k; t++) {
                    a[t][index] = buf[j][t];
                    buf[j][t] = 0;
                }
            }
        }
    }


    public void normalize(long[][] a, int l, int r) {
        for (int i = l; i <= r; i++) {
            moduleInPlace(a, i, phi);
        }
    }

    private void moduleInPlace(long[][] a, int offset, int[] b) {
        int ra = k - 1;
        int rb = Polynomials.rankOf(b);
        if (ra < rb) {
            return;
        }
        for (int i = ra; i >= rb; i--) {
            assert a[i][offset] % b[rb] == 0;
            long d = a[i][offset] / b[rb];
            if (d == 0) {
                continue;
            }
            for (int j = rb; j >= 0; j--) {
                int ij = i + j - rb;
                a[ij][offset] -= d * b[j];
            }
        }
    }

    public void addIFWT(long[][] a, int l, int r) {
        addIFWT(a, l, r, true);
    }

    /**
     * O(k^2(r-l+1)\log_2(r-l+1))
     *
     * @param a
     * @param l
     * @param r
     */
    public void addIFWT(long[][] a, int l, int r, boolean divAuto) {
        int n = r - l + 1;
        addIFWT0(a, l, r);
        normalize(a, l, r);

        if (divAuto) {
            for (int i = l; i <= r; i++) {
//            assert a[0][i] % n == 0;
                a[0][i] = a[0][i] / n;
            }
        }
    }

    private void addIFWT0(long[][] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;

        for (int i = 0; i < step; i++) {
            int first = l + i;
            for (int j = 0; j < k; j++) {
                for (int t = 0; t < k; t++) {
                    mulAddTo(a, t * step + first, iaddC[j][t], buf[j]);
                }
            }
            //copy back
            for (int j = 0; j < k; j++) {
                int index = j * step + first;
                for (int t = 0; t < k; t++) {
                    a[t][index] = buf[j][t];
                    buf[j][t] = 0;
                }
            }
        }

        for (int i = 0; i < len; i += step) {
            addIFWT0(a, l + i, l + i + step - 1);
        }
    }
}
