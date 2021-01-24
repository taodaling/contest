package template.math;

import template.polynomial.FastFourierTransform;
import template.utils.Pair;
import template.utils.PrimitiveBuffers;

public class NatureBigInt implements Comparable<NatureBigInt>, Cloneable {
    private static int base = (int) 1e3;
    private static int strLength = 3;
    private int[] data;
    private int len;

    static NatureBigInt[] cache = new NatureBigInt[256];

    public static NatureBigInt valueOf(CharSequence s) {
        int[] data = new int[(s.length() + strLength - 1) / strLength];
        for (int i = 0; i < data.length; i++) {
            int r = s.length() - 1 - i * 3;
            int l = Math.max(r - 2, 0);
            for (int j = l; j <= r; j++) {
                data[i] = data[i] * 10 + s.charAt(j) - '0';
            }
        }
        return new NatureBigInt(data);
    }

    public static NatureBigInt valueOf(long x) {
        assert x >= 0;
        if (x < cache.length) {
            int ix = (int) x;
            if (cache[ix] == null) {
                cache[ix] = new NatureBigInt(x);
            }
            return cache[ix];
        }
        return new NatureBigInt(x);
    }

    public NatureBigInt(long x) {
        assert x >= 0;
        len = log(x);
        data = new int[len];
        for (int i = 0; i < len; i++) {
            data[i] = (int) x % base;
            x /= base;
        }
    }

    public long longValue() {
        long ans = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            ans = ans * base + data[i];
        }
        return ans;
    }

    public NatureBigInt add(NatureBigInt x) {
        return add(this, x);
    }

    public NatureBigInt subtract(NatureBigInt x) {
        return subtract(this, x);
    }

    private static int log(long x) {
        int len = 0;
        while (x != 0) {
            x /= base;
            len++;
        }
        return len;
    }

    public NatureBigInt mul(long x) {
        return mul(this, x);
    }

    public NatureBigInt mul(NatureBigInt x) {
        return mul(this, x);
    }

    private NatureBigInt(int[] data, boolean reqNormalize) {
        this.data = data;
        len = data.length;
        if (reqNormalize) {
            normalize(data);
        } else {
            assert !normalize(data);
        }
        while (len > 0 && data[len - 1] == 0) {
            len--;
        }
    }

    private NatureBigInt(int[] data) {
        this(data, true);
    }

    private static NatureBigInt add(NatureBigInt a, NatureBigInt b) {
        int len = Math.max(a.len, b.len) + 1;
        int[] data = new int[len];
        for (int i = 0; i < a.len; i++) {
            data[i] += a.data[i];
        }
        for (int i = 0; i < b.len; i++) {
            data[i] += b.data[i];
        }
        return new NatureBigInt(data);
    }

    private static NatureBigInt subtract(NatureBigInt a, NatureBigInt b) {
        int len = Math.max(a.len, b.len) + 1;
        int[] data = new int[len];
        for (int i = 0; i < a.len; i++) {
            data[i] += a.data[i];
        }
        for (int i = 0; i < b.len; i++) {
            data[i] -= b.data[i];
        }
        return new NatureBigInt(data);
    }

    private static boolean normalize(int[] data) {
        int overflow = 0;
        boolean effect = false;
        for (int i = 0; i < data.length; i++) {
            int old = data[i];
            overflow += old;
            data[i] = overflow % base;
            overflow /= base;
            if (data[i] < 0) {
                data[i] += base;
                overflow--;
            }
            effect = effect || old != data[i];
        }
        assert overflow == 0;
        return effect;
    }

    private static NatureBigInt mul(NatureBigInt a, long b) {
        if (b == 0 || a.len == 0) {
            return valueOf(0);
        }
        int logB = log(b);
        int len = a.len + logB;
        int[] data = new int[len];
        for (int i = 0; i < logB; i++) {
            int tail = (int) (b % base);
            b /= base;
            for (int j = 0; j < a.len; j++) {
                int v = tail * a.data[j];
                data[j + i] += v % base;
                data[j + i + 1] += v / base;
            }
        }
        return new NatureBigInt(data);
    }

    private static final int FFT_THRESHOLD = 50;

    private static NatureBigInt mulBF(NatureBigInt a, NatureBigInt b) {
        int[] ans = new int[a.len + b.len];
        for (int i = 0; i < a.len; i++) {
            for (int j = 0; j < b.len; j++) {
                int v = a.data[i] * b.data[j];
                ans[i + j] += v % base;
                ans[i + j + 1] += v / base;
            }
        }
        return new NatureBigInt(ans);
    }

    private static NatureBigInt mulFFT(NatureBigInt a, NatureBigInt b) {
        int clen = a.len + b.len;
        int[] ans = new int[clen];
        //fft
        double[][] ap = PrimitiveBuffers.allocDoublePow2Array(clen, 2);
        double[][] bp = PrimitiveBuffers.allocDoublePow2Array(clen, 2);
        for (int i = 0; i < a.len; i++) {
            ap[0][i] = a.data[i];
        }
        for (int i = 0; i < b.len; i++) {
            bp[0][i] = b.data[i];
        }
        FastFourierTransform.fft(ap, false);
        FastFourierTransform.fft(bp, false);
        double[][] res = FastFourierTransform.dotMul(ap, bp);
        FastFourierTransform.fft(res, true);
        long overflow = 0;
        for (int i = 0; i < clen; i++) {
            overflow += Math.round(res[0][i]);
            ans[i] = (int) (overflow % base);
            overflow /= base;
        }
        PrimitiveBuffers.release(ap);
        PrimitiveBuffers.release(bp);
        PrimitiveBuffers.release(res);
        return new NatureBigInt(ans, false);
    }

    private static NatureBigInt mul(NatureBigInt a, NatureBigInt b) {
        if (a.len == 0 || b.len == 0) {
            return valueOf(0);
        }
        if (a.len <= FFT_THRESHOLD || b.len <= FFT_THRESHOLD) {
            return mulBF(a, b);
        }
        return mulFFT(a, b);
    }

    @Override
    public int compareTo(NatureBigInt b) {
        NatureBigInt a = this;
        int ans = Integer.compare(a.len, b.len);
        if (ans != 0) {
            return ans;
        }
        for (int i = 0; i < a.len; i++) {
            ans = Integer.compare(a.data[i], b.data[i]);
            if (ans != 0) {
                return ans;
            }
        }
        return ans;
    }

    @Override
    public int hashCode() {
        int ans = 0;
        for (int i = 0; i < len; i++) {
            ans = ans * 31 + data[i];
        }
        return ans;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NatureBigInt) && compareTo((NatureBigInt) obj) == 0;
    }

    @Override
    public NatureBigInt clone() {
        return this;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder(3 * strLength);
        toString(ans);
        return ans.toString();
    }

    public void toString(StringBuilder sb) {
        if (len == 0) {
            sb.append('0');
            return;
        }
        sb.append(data[len - 1]);
        for (int i = len - 2; i >= 0; i--) {
            if (data[i] < 100) {
                sb.append('0');
            }
            if (data[i] < 10) {
                sb.append(0);
            }
            sb.append(data[i]);
        }
    }

    /**
     * start with 0
     */
    public int getDigit(int i) {
        int pos = i / 3;
        int relativePos = i % 3;
        if (pos >= len) {
            return 0;
        }
        int v = data[pos];
        if (relativePos == 0) {
            return v % 10;
        } else if (relativePos == 1) {
            return v / 10 % 10;
        }
        return v / 100;
    }

    /**
     * x <= 1e15
     *
     * @param x
     * @return
     */
    public NatureBigInt div(long x) {
        assert x <= 1e15;
        return divide(this, x).a;
    }


    /**
     * x <= 1e15
     *
     * @param x
     * @return
     */
    public Pair<NatureBigInt, Long> divAndRemain(long x) {
        assert x <= 1e15;
        return divide(this, x);
    }


    private static Pair<NatureBigInt, Long> divide(NatureBigInt a, long x) {
        assert x > 0;
        if (a.len == 0) {
            return new Pair<>(a, 0L);
        }
        int[] ans = new int[a.len];
        long overflow = 0;
        for (int i = a.len - 1; i >= 0; i--) {
            overflow = overflow * base + a.data[i];
            ans[i] = (int) (overflow / x);
            overflow %= x;
        }
        return new Pair<>(new NatureBigInt(ans, false), overflow);
    }
}
