package template.polynomial;

import template.binary.Log2;
import template.math.DigitUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModGenericFastWalshHadamardTransform {
    int[][] addC;
    int[][] iaddC;
    int[][] addCWithRoot;
    int[][] iaddCWithRoot;
    int[] phi;
    int k;
    int invK;
    int mod;
    int[][] buf;
    int one;

    static Map<Long, Integer> rootCache = new HashMap<>();

    static {
        rootCache.put(4287426849551679490L, 998244352);
        rootCache.put(4287426849551679492L, 86583718);
        rootCache.put(4287426849551679495L, 14553391);
        rootCache.put(4287426849551679496L, 372528824);
        rootCache.put(4287426849551679502L, 219186804);
        rootCache.put(4287426849551679504L, 69212480);
        rootCache.put(4287426849551679505L, 503044);
        rootCache.put(4294967326064771074L, 1000000006);
        rootCache.put(4294967334654705666L, 1000000008);
        rootCache.put(4294967334654705667L, 115381398);
        rootCache.put(4294967334654705668L, 430477711);
        rootCache.put(4294967334654705670L, 115381399);
        rootCache.put(4294967334654705671L, 95932470);
        rootCache.put(4294967334654705672L, 118835338);
        rootCache.put(4294967334654705673L, 246325263);
        rootCache.put(4294967334654705676L, 86475609);
        rootCache.put(4294967334654705678L, 9196980);
        rootCache.put(4294967334654705682L, 4138593);
    }

    public static void main(String[] args) {
        System.out.println(currentSupport());
    }

    public static String currentSupport() {
        StringBuilder in = new StringBuilder();
        for (long key : rootCache.keySet()) {
            in.append(DigitUtils.highBit(key)).append(':').append(DigitUtils.lowBit(key));
            in.append('\n');
        }
        return in.toString();
    }

    /**
     * Find x satisfied x^1,...,x^{k-1} != 1 and x^k = 1
     */
    public static int searchRoot(int k, int mod) {
        if ((mod - 1) % k != 0) {
            return -1;
        }
        for (int i = 1; i < mod; i++) {
            int cur = i;
            int p = 1;
            while (p < k) {
                if (cur == 1) {
                    break;
                }
                cur = (int) ((long) cur * i % mod);
                p++;
            }
            if (p == k && cur == 1) {
                return i;
            }
        }
        return -1;
    }

    public ModGenericFastWalshHadamardTransform(int k, int mod, boolean searchRoot) {
        this(k, mod, searchRoot ? searchRoot(k, mod) : -1);
    }

    public ModGenericFastWalshHadamardTransform(int k, int mod) {
        this(k, mod, -1);
    }

    /**
     * radix k
     * g should satisfied g^1,...,g^{k-1} != 1 and g^k = 1
     */
    public ModGenericFastWalshHadamardTransform(int k, int mod, int g) {
        one = 1 % k;
        this.g = g;
        this.mod = mod;
        this.k = k;
        addC = new int[k][k];
        iaddC = new int[k][k];
        buf = new int[k][k];
        accum = new long[k * 2];
        phi = CyclotomicPolynomialBF.get(k).clone();
        inf = Long.MAX_VALUE - (long) (mod - 1) * (mod - 1);
        invK = (int) DigitUtils.modInverse(k, mod);
        for (int i = 0; i < phi.length; i++) {
            phi[i] = DigitUtils.mod(phi[i], mod);
        }
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                addC[i][j] = i * j % k;
                iaddC[i][j] = (k - addC[i][j]) % k;
            }
        }

        if (g == -1) {
            g = rootCache.getOrDefault(DigitUtils.asLong(mod, k), -1);
        }
        if (g != -1) {
            int[] powSerial = new int[k];
            powSerial[0] = 1;
            for (int i = 1; i < k; i++) {
                powSerial[i] = (int) ((long) powSerial[i - 1] * g % mod);
            }
            addCWithRoot = new int[k][k];
            iaddCWithRoot = new int[k][k];

            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    addCWithRoot[i][j] = powSerial[addC[i][j]];
                    iaddCWithRoot[i][j] = powSerial[iaddC[i][j]];
                    assert (long) addCWithRoot[i][j] * iaddCWithRoot[i][j] % mod == 1;
                }
            }
        }
    }

    /**
     * O((r-l+1)\log_k(r-l+1))
     *
     * @param a
     * @param l
     * @param r
     */
    public void maxFWT(int[] a, int l, int r) {
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
            if (a[i] >= mod) {
                a[i] -= mod;
            }
        }
    }

    /**
     * O((r-l+1)\log_k(r-l+1))
     *
     * @param a
     * @param l
     * @param r
     */
    public void maxIFWT(int[] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        int step = len / k;
        for (int i = r; i >= l + step; i--) {
            a[i] -= a[i - step];
            if (a[i] < 0) {
                a[i] += mod;
            }
        }
        for (int i = 0; i < len; i += step) {
            maxIFWT(a, l + i, l + i + step - 1);
        }
    }

    public void minFWT(int[] a, int l, int r) {
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
            if (a[i] >= mod) {
                a[i] -= mod;
            }
        }
    }

    public void minIFWT(int[] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;
        for (int i = l; i <= r - step; i++) {
            a[i] -= a[i + step];
            if (a[i] < 0) {
                a[i] += mod;
            }
        }
        for (int i = 0; i < len; i += step) {
            minIFWT(a, l + i, l + i + step - 1);
        }
    }

    public void dotMul(int[] a, int[] b, int[] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            c[i] = (int) ((long) a[i] * b[i] % mod);
        }
    }

    public void pow(int[] x, int[] res, int l, int r, long n) {
        if (n == 0) {
            Arrays.fill(res, one);
            return;
        }
        pow(x, res, l, r, n / 2);
        dotMul(res, res, res, l, r);
        if (n % 2 == 1) {
            dotMul(res, x, res, l, r);
        }
    }

    /**
     * O(k^2(r-l+1))
     */
    public void dotMul(int[][] a, int[][] b, int[][] c, int l, int r) {
        for (int i = l; i <= r; i++) {
            mulAddTo(a, i, b, i);
            for (int j = 0; j < k; j++) {
                c[j][i] = (int) accum[j];
                accum[j] = 0;
            }
        }
    }

    public void pow(int[][] x, int[][] res, int l, int r, long n) {
        if (n == 0) {
            for (int i = 0; i < k; i++) {
                if (i == 0) {
                    Arrays.fill(res[i], one);
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
    private void mulAddTo(int[][] a, int offset, int t, int[] dest) {
        for (int i = 0, to = t; i < k; i++, to = to + 1 == k ? 0 : to + 1) {
            dest[to] += a[i][offset];
            if (dest[to] >= mod) {
                dest[to] -= mod;
            }
        }
    }

    /**
     * b[?][bi] += a[?][ai] * (x^t + 1)
     */
    private void mulAddTo(int[][] a, int ai, int[][] b, int bi, int t) {
        for (int i = 0, to0 = 0, to1 = t; i < k; i++) {
            b[to0][bi] += a[i][ai];
            if (b[to0][bi] >= mod) {
                b[to0][bi] -= mod;
            }
            b[to1][bi] += a[i][ai];
            if (b[to1][bi] >= mod) {
                b[to1][bi] -= mod;
            }
            to0 = to0 + 1 == k ? 0 : to0 + 1;
            to1 = to1 + 1 == k ? 0 : to1 + 1;
        }
    }

    long[] accum;
    long inf;

    private void mulAddTo(int[][] a, int ai, int[][] b, int bi) {
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                int ij = i + j;
                accum[ij] += (long) a[i][ai] * b[j][bi];
                if (accum[ij] >= inf) {
                    accum[ij] %= mod;
                }
            }
        }
        for (int i = 0; i < k; i++) {
            accum[i] += accum[i + k] % mod;
            accum[i + k] = 0;
            accum[i] %= mod;
        }
    }

    int g;

    public void addFWTWithRoot(int[] a, int l, int r) {
        addFWTWithRoot(a, l, r, addCWithRoot);
    }

    /**
     * a[k][r + 1]
     * <p>
     * O(k(r-l+1)\log_2(r-l+1))
     */
    public void addFWTWithRoot(int[] a, int l, int r, int[][] addCWithRoot) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;
        for (int i = 0; i < len; i += step) {
            addFWTWithRoot(a, l + i, l + i + step - 1, addCWithRoot);
        }
        for (int i = 0; i < step; i++) {
            int first = l + i;
            for (int j = 0; j < k; j++) {
                long sum = 0;
                for (int t = 0; t < k; t++) {
                    sum += (long) addCWithRoot[j][t] * a[first + t * step];
                    if (sum >= inf) {
                        sum %= mod;
                    }
                }
                buf[0][j] = (int) (sum % mod);
            }
            //copy back
            for (int j = 0; j < k; j++) {
                int index = j * step + first;
                a[index] = buf[0][j];
                buf[0][j] = 0;
            }
        }
    }

    public void addIFWTWithRoot(int[] a, int l, int r) {
        addIFWTWithRoot0(a, l, r);
        long invN = DigitUtils.modInverse(r - l + 1, mod);
        for (int i = l; i <= r; i++) {
            a[i] = (int) (invN * a[i] % mod);
        }
    }

    /**
     * a[k][r + 1]
     * <p>
     * O(k(r-l+1)\log_2(r-l+1))
     */
    public void addIFWTWithRoot0(int[] a, int l, int r) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;

        for (int i = 0; i < step; i++) {
            int first = l + i;
            for (int j = 0; j < k; j++) {
                long sum = 0;
                for (int t = 0; t < k; t++) {
                    sum += (long) iaddCWithRoot[j][t] * a[first + t * step];
                    if (sum >= inf) {
                        sum %= mod;
                    }
                }
                buf[0][j] = (int) (sum % mod);
            }
            //copy back
            for (int j = 0; j < k; j++) {
                int index = j * step + first;
                a[index] = buf[0][j];
                buf[0][j] = 0;
            }
        }

        for (int i = 0; i < len; i += step) {
            addIFWTWithRoot0(a, l + i, l + i + step - 1);
        }
    }

    public void addFWT(int[][] a, int l, int r) {
        addFWT(a, l, r, addC);
    }

    /**
     * a[k][r + 1]
     * <p>
     * O(k^2(r-l+1)\log_2(r-l+1))
     */
    public void addFWT(int[][] a, int l, int r, int[][] addC) {
        if (l == r) {
            return;
        }
        int len = r - l + 1;
        assert len % k == 0;
        int step = len / k;
        for (int i = 0; i < len; i += step) {
            addFWT(a, l + i, l + i + step - 1, addC);
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


    public void normalize(int[][] a, int l, int r) {
        for (int i = l; i <= r; i++) {
            moduleInPlace(a, i, phi);
        }
    }

    private void moduleInPlace(int[][] a, int offset, int[] b) {
        int ra = k - 1;
        int rb = Polynomials.rankOf(b);
        if (ra < rb) {
            return;
        }
        assert b[rb] == 1;
        for (int i = ra; i >= rb; i--) {
            long d = a[i][offset];
            if (d == 0) {
                continue;
            }
            for (int j = rb; j >= 0; j--) {
                int ij = i + j - rb;
                a[ij][offset] -= d * b[j] % mod;
                if (a[ij][offset] < 0) {
                    a[ij][offset] += mod;
                }
            }
        }
    }

    private void moduleInPlace(int[] a, int[] b) {
        int ra = k - 1;
        int rb = Polynomials.rankOf(b);
        if (ra < rb) {
            return;
        }
        assert b[rb] == 1;
        for (int i = ra; i >= rb; i--) {
            long d = a[i];
            if (d == 0) {
                continue;
            }
            for (int j = rb; j >= 0; j--) {
                int ij = i + j - rb;
                a[ij] -= d * b[j] % mod;
                if (a[ij] < 0) {
                    a[ij] += mod;
                }
            }
        }
    }


    /**
     * O(k^2(r-l+1)\log_2(r-l+1))
     *
     * @param a
     * @param l
     * @param r
     */
    public void addIFWT(int[][] a, int l, int r) {
        int n = r - l + 1;
        long inv = DigitUtils.modInverse(n, mod);
        addIFWT0(a, l, r);
        normalize(a, l, r);
        for (int i = l; i <= r; i++) {
//            assert a[0][i] % n == 0;
            a[0][i] = (int) (a[0][i] * inv % mod);
        }
    }

    private void addIFWT0(int[][] a, int l, int r) {
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

    public boolean constant(int[] x) {
        for (int i = 1; i < k; i++) {
            if (x[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean constant(int[][] x, int offset) {
        for (int i = 1; i < k; i++) {
            if (x[i][offset] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * C[i] means how many instances of i.
     * <p>
     * return[i] means how many subsets sum i
     * <p>
     * O(nk^{n+3}+\sqrt{m} k^3) where m = C[0] + C[1] + ...
     */
    public int[] countSubset(int[] C) {
        if (g != -1) {
            return countSubsetWithG(C);
        }

        int[][] c = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                c[i][j] = 0;
            }
        }
        int n = C.length;
        int[][][] Fr = new int[k][k][n];
        for (int i = 0; i < k; i++) {
            System.arraycopy(C, 0, Fr[i][0], 0, n);
            addFWT(Fr[i], 0, n - 1, c);
            for (int t = 0; t < k; t++) {
                for (int j = 0; j < k; j++) {
                    c[t][j] += addC[t][j];
                    if (c[t][j] >= k) {
                        c[t][j] -= k;
                    }
                }
            }
        }
        int[][] x = new int[k][n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                for (int t = 0; t < k; t++) {
                    //c^{-1}(i,t) * Fr_{t,j}
                    mulAddTo(Fr[t], j, iaddC[i][t], buf[0]);
                }
                moduleInPlace(buf[0], phi);
                assert constant(buf[0]);
                x[i][j] = (int) ((long) buf[0][0] * invK % mod);
                buf[0][0] = 0;
            }
        }
        int sum = Arrays.stream(C).sum();
        int bits = Log2.floorLog(sum) + 1;
        int highBits = bits / 2;
        int lowBits = bits - highBits;
        int[][] fastPowLowBit = new int[k][1 << lowBits];
        int[][] fastPowHighBit = new int[k][1 << highBits];
        int[][] F = new int[k][n];
        Arrays.fill(F[0], one);
        for (int i = 0; i < k; i++) {
            for (int t = 0; t < k; t++) {
                Arrays.fill(fastPowLowBit[t], 0);
                Arrays.fill(fastPowHighBit[t], 0);
            }
            fastPowLowBit[0][0] = one;
            for (int j = 1; j < 1 << lowBits; j++) {
                mulAddTo(fastPowLowBit, j - 1, fastPowLowBit, j, i);
            }
            fastPowHighBit[0][0] = one;
            if (highBits > 0) {
                mulAddTo(fastPowLowBit, (1 << lowBits) - 1, fastPowHighBit, 1, i);
                for (int j = 2; j < 1 << highBits; j++) {
                    mulAddTo(fastPowHighBit, j - 1, fastPowHighBit, 1);
                    for (int t = 0; t < k; t++) {
                        fastPowHighBit[t][j] = (int) accum[t];
                        accum[t] = 0;
                    }
                }
            }

            for (int j = 0; j < n; j++) {
                int p = x[i][j];
                int l = p & ((1 << lowBits) - 1);
                int h = p >>> lowBits;
                if (l != 0) {
                    mulAddTo(fastPowLowBit, l, F, j);
                    for (int t = 0; t < k; t++) {
                        F[t][j] = (int) accum[t];
                        accum[t] = 0;
                    }
                }
                if (h != 0) {
                    mulAddTo(fastPowHighBit, h, F, j);
                    for (int t = 0; t < k; t++) {
                        F[t][j] = (int) accum[t];
                        accum[t] = 0;
                    }
                }
            }
        }

        addIFWT(F, 0, n - 1);
        return F[0];
    }

    /**
     * C[i] means how many instances of i.
     * <p>
     * return[i] means how many subsets sum i
     * <p>
     * O(nk^{n+2}+\sqrt{m} k) where m = C[0] + C[1] + ...
     */
    private int[] countSubsetWithG(int[] C) {
        int[][] c = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                c[i][j] = 1;
            }
        }
        int n = C.length;
        int[][] Fr = new int[k][n];
        for (int i = 0; i < k; i++) {
            System.arraycopy(C, 0, Fr[i], 0, n);
            addFWTWithRoot(Fr[i], 0, n - 1, c);
            for (int t = 0; t < k; t++) {
                for (int j = 0; j < k; j++) {
                    c[t][j] = (int) ((long) c[t][j] * addCWithRoot[t][j] % mod);
                }
            }
        }
        int[][] x = new int[k][n];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                long sum = 0;
                for (int t = 0; t < k; t++) {
                    //c^{-1}(i,t) * Fr_{t,j}
                    sum += (long) Fr[t][j] * iaddC[i][t];
                    if (sum >= inf) {
                        sum %= mod;
                    }
                }
                x[i][j] = (int) (sum % mod * invK % mod);
            }
        }
        int sum = Arrays.stream(C).sum();
        int bits = Log2.floorLog(sum) + 1;
        int highBits = bits / 2;
        int lowBits = bits - highBits;
        int[] fastPowLowBit = new int[1 << lowBits];
        int[] fastPowHighBit = new int[1 << highBits];
        int[] F = new int[n];
        Arrays.fill(F, one);
        for (int i = 0; i < k; i++) {
            fastPowLowBit[0] = one;
            for (int j = 1; j < 1 << lowBits; j++) {
                fastPowLowBit[j] = (int) ((long) fastPowLowBit[j - 1] * addCWithRoot[1][i] % mod);
            }
            fastPowHighBit[0] = one;
            if (highBits > 0) {
                fastPowHighBit[1] = (int) ((long) fastPowLowBit[fastPowLowBit.length - 1] * addCWithRoot[1][i] % mod);
                for (int j = 2; j < 1 << highBits; j++) {
                    fastPowHighBit[j] = (int) ((long) fastPowHighBit[j - 1] * fastPowHighBit[1] % mod);
                }
            }

            for (int j = 0; j < n; j++) {
                int p = x[i][j];
                int l = p & ((1 << lowBits) - 1);
                int h = p >>> lowBits;
                if (l != 0) {
                    F[j] = (int) ((long) F[j] * fastPowLowBit[l] % mod);
                }
                if (h != 0) {
                    F[j] = (int) ((long) F[j] * fastPowHighBit[h] % mod);
                }
            }
        }

        addIFWTWithRoot(F, 0, n - 1);
        return F;
    }

    public void disableG() {
        this.g = -1;
    }
}
