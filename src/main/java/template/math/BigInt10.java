package template.math;

import template.polynomial.FastFourierTransform;
import template.polynomial.Polynomials;
import template.utils.PrimitiveBuffers;

public class BigInt10 extends Number implements Cloneable, Comparable<BigInt10> {
    private static String LONG_MIN_VALUE_STR = Long.toString(Long.MIN_VALUE);
    private static final int BASE = 1000;
    private static final int STR_LEN = 3;
    private int[] data;
    private int sign;
    private int rank;

    private BigInt10(int[] data, int sign) {
        this.data = data;
        afterInit(sign);
    }

    private boolean valid() {
        return data != null;
    }

    private void assertValid() {
        if (!valid()) {
            throw new IllegalStateException();
        }
    }

    public void release() {
        release(this);
    }

    public static void release(BigInt10 a) {
        a.assertValid();
        PrimitiveBuffers.release(a.data);
        a.data = null;
    }

    public static void release(BigInt10 a, BigInt10 b) {
        release(a);
        release(b);
    }

    public static BigInt10 replace(BigInt10 x, BigInt10 a) {
        release(a);
        return x;
    }

    public static BigInt10 replace(BigInt10 x, BigInt10 a, BigInt10 b) {
        release(a);
        release(b);
        return x;
    }

    public BigInt10(CharSequence s) {
        init(s);
    }

    private void init(CharSequence s) {
        int len = s.length();
        if (s.length() == 0) {
            throw new IllegalArgumentException();
        }
        sign = 1;
        int begin = 0;
        if (s.charAt(begin) == '+' || s.charAt(begin) == '-') {
            if (s.charAt(begin) == '-') {
                sign = -1;
            } else {
                sign = 1;
            }
            begin++;
        }
        data = PrimitiveBuffers.allocIntPow2((len - begin + STR_LEN - 1) / STR_LEN);
        for (int i = len - 1, wpos = 0; i >= begin; i -= STR_LEN, wpos++) {
            for (int j = Math.max(begin, i - STR_LEN + 1); j <= i; j++) {
                char c = s.charAt(j);
                if (!Character.isDigit(c)) {
                    throw new IllegalArgumentException();
                }
                data[wpos] = data[wpos] * 10 + c - '0';
            }
        }
        afterInit(sign);
    }

    private void afterInit(int sign) {
        this.sign = sign;
        int pushUp = 0;
        for (int i = 0; i < data.length; i++) {
            data[i] += pushUp;
            pushUp = DigitUtils.floorDiv(data[i], BASE);
            data[i] -= pushUp * BASE;
        }
        if (pushUp > 0) {
            throw new IllegalStateException();
        }
        rank = Polynomials.rankOf(data);
        if (rank == 0 && data[0] == 0) {
            sign = 1;
        }
    }

    private int lengthReq(long x) {
        int ans = 1;
        while (x >= BASE) {
            x /= BASE;
        }
        return ans;
    }

    public BigInt10(long x) {
        if (x == Long.MIN_VALUE) {
            init(LONG_MIN_VALUE_STR);
            return;
        }
        sign = 1;
        if (x < 0) {
            sign = -1;
        }
        x *= sign;
        data = PrimitiveBuffers.allocIntPow2(lengthReq(x));
        int wpos = 0;
        while (x >= BASE) {
            data[wpos++] = (int) (x % BASE);
        }
        data[wpos] = (int) x;
        afterInit(sign);
    }

    public BigInt10 negate() {
        return negate(this);
    }

    public static BigInt10 negate(BigInt10 a) {
        a.assertValid();
        BigInt10 res = a.clone();
        res.sign = -res.sign;
        return res;
    }

    public static BigInt10 add(BigInt10 a, BigInt10 b) {
        if (a.sign == b.sign) {
            return rawAdd(a, b, a.sign);
        } else {
            if (rawCompare(a, b) < 0) {
                BigInt10 tmp = a;
                a = b;
                b = tmp;
            }
            return rawSub(a, b, a.sign);
        }
    }

    public BigInt10 add(BigInt10 b) {
        return add(this, b);
    }

    public static BigInt10 sub(BigInt10 a, BigInt10 b) {
        a.assertValid();
        b.assertValid();
        if (a.sign != b.sign) {
            return rawAdd(a, b, a.sign);
        }
        if (rawCompare(a, b) >= 0) {
            return rawSub(a, b, a.sign);
        } else {
            return rawSub(b, a, -b.sign);
        }
    }

    public BigInt10 sub(BigInt10 b) {
        return sub(this, b);
    }

    public BigInt10 div(BigInt10 b) {
        return div(this, b);
    }

    public BigInt10 mod(BigInt10 b) {
        return sub(div(b).mul(b));
    }

    public BigInt10 abs() {
        if (signum() < 0) {
            return negate();
        }
        return clone();
    }

    //safe for abs(a), abs(b) <= 1000^{10^8}
    //based on fast fourier transform
    public static BigInt10 mul(BigInt10 a, BigInt10 b) {
        a.assertValid();
        b.assertValid();
        int newRank = a.rank + b.rank;
        double[][] da = PrimitiveBuffers.allocDoublePow2Array(newRank + 1, 2);
        double[][] db = PrimitiveBuffers.allocDoublePow2Array(newRank + 1, 2);
        for (int i = 0; i <= a.rank; i++) {
            da[0][i] = a.data[i];
        }
        for (int i = 0; i <= b.rank; i++) {
            db[0][i] = b.data[i];
        }
        FastFourierTransform.fft(da, false);
        FastFourierTransform.fft(db, false);
        FastFourierTransform.dotMulInplace(da, db);
        FastFourierTransform.fft(da, true);
        int[] prod = PrimitiveBuffers.allocIntPow2((a.rank + 1) + (b.rank + 1));
        copyFromDouble(da[0], prod);
        PrimitiveBuffers.release(da);
        PrimitiveBuffers.release(db);
        return new BigInt10(prod, a.sign * b.sign);
    }

    //return p where abs(this) = x * 10^p and 0.1 <= x < 1
    //if this == 0 then 0 will be returned
    public int ceilLog10() {
        int ans = rank * STR_LEN;
        if (data[rank] > 0) {
            ans++;
        }
        if (data[rank] > 10) {
            ans++;
        }
        if (data[rank] > 100) {
            ans++;
        }
        return ans;
    }

    public BigInt10 mul(BigInt10 a) {
        return mul(this, a);
    }

    /**
     * There are some precision issues, so it's buggy and don't invoke it
     */
    public static BigInt10 div(BigInt10 a, BigInt10 b) {
        a.assertValid();
        b.assertValid();
        if (b.signum() == 0) {
            throw new IllegalArgumentException();
        }
        if (rawCompare(a, b) < 0) {
            return new BigInt10(0);
        }
        int expLen = a.rank - b.rank;
        int extraConsider = 10;
        int modLen = expLen + extraConsider;
        double[][] da = PrimitiveBuffers.allocDoublePow2Array(modLen, 2);
        double[] bp = PrimitiveBuffers.allocDoublePow2(modLen);
        for (int i = 0; i < modLen && i <= b.rank; i++) {
            bp[i] = b.data[b.rank - i];
        }
        for (int i = 0; i < modLen && i <= a.rank; i++) {
            da[0][i] = a.data[a.rank - i];
        }
        double[] invDB = FastFourierTransform.inverse(bp, modLen);
        PrimitiveBuffers.release(bp);
        double[][] db = new double[][]{
                PrimitiveBuffers.resize(invDB, modLen),
                PrimitiveBuffers.allocDoublePow2(modLen),
        };
        FastFourierTransform.fft(da, false);
        FastFourierTransform.fft(db, false);
        FastFourierTransform.dotMulInplace(da, db);
        FastFourierTransform.fft(da, true);

        int[] data = PrimitiveBuffers.allocIntPow2(expLen + 1);
        int len = da[0].length;
        double pushDown = 0;
        for(int i = 0; i < len; i++){
            double total = da[0][i] + pushDown * BASE;
            double round = Math.floor(total);
            pushDown = total - round;
            da[0][i] = round;
        }
        long pushUp = 0;
        for (int i = len - 1; i > expLen; i--) {
            pushUp += Math.round(da[0][i] + pushUp);
            pushUp = DigitUtils.floorDiv(pushUp, BASE);
        }
        data[expLen] += pushUp;
        for (int i = expLen; i >= 0; i--) {
            data[expLen - i] += Math.round(da[0][i]);
        }


        PrimitiveBuffers.release(da);
        PrimitiveBuffers.release(db);
        return new BigInt10(data, a.sign * b.sign);
    }

    private static void copyFromDouble(double[] data, int[] res) {
        long pushUp = 0;
        for (int i = 0; i < res.length; i++) {
            if (i < data.length) {
                pushUp += Math.round(data[i]);
            }
            long next = DigitUtils.floorDiv(pushUp, BASE);
            res[i] = (int) (pushUp - next * BASE);
            pushUp = next;
        }
        if (pushUp != 0) {
            throw new IllegalStateException();
        }
    }

    @Override
    public int compareTo(BigInt10 o) {
        assertValid();
        o.assertValid();
        if (sign != o.sign) {
            return Integer.compare(sign, o.sign);
        }
        return sign * rawCompare(this, o);
    }

    private static BigInt10 rawAdd(BigInt10 a, BigInt10 b, int sign) {
        a.assertValid();
        b.assertValid();
        int[] ans = PrimitiveBuffers.allocIntPow2(Math.max(a.rank + 1, b.rank + 1) + 1);
        for (int i = 0; i <= a.rank; i++) {
            ans[i] += a.data[i];
        }
        for (int i = 0; i <= b.rank; i++) {
            ans[i] += b.data[i];
        }
        return new BigInt10(ans, sign);
    }

    private static BigInt10 rawSub(BigInt10 a, BigInt10 b, int sign) {
        a.assertValid();
        b.assertValid();
        int[] ans = PrimitiveBuffers.allocIntPow2(a.rank + 1);
        for (int i = 0; i <= a.rank; i++) {
            ans[i] += a.data[i];
        }
        for (int i = 0; i <= b.rank; i++) {
            ans[i] -= b.data[i];
        }
        return new BigInt10(ans, sign);
    }

    private static int rawCompare(BigInt10 a, BigInt10 b) {
        a.assertValid();
        b.assertValid();

        if (a.ceilLog10() != b.ceilLog10()) {
            return Integer.compare(a.ceilLog10(), b.ceilLog10());
        }
        for (int i = a.rank; i >= 0; i--) {
            if (a.data[i] != b.data[i]) {
                return Integer.compare(a.data[i], b.data[i]);
            }
        }
        return 0;
    }


    @Override
    public BigInt10 clone() {
        try {
            assertValid();
            BigInt10 ans = (BigInt10) super.clone();
            ans.data = PrimitiveBuffers.allocIntPow2(ans.data);
            return ans;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    public int signum() {
        assertValid();
        if (rank == 0 && data[0] == 0) {
            return 0;
        }
        return sign;
    }

    @Override
    public int intValue() {
        assertValid();
        return (int) longValue();
    }

    @Override
    public long longValue() {
        assertValid();
        long ans = 0;
        for (int i = rank; i >= 0; i--) {
            ans = ans * BASE + data[i];
        }
        return ans;
    }

    @Override
    public float floatValue() {
        assertValid();
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        assertValid();
        double ans = 0;
        for (int i = rank; i >= 0; i--) {
            ans = ans * BASE + data[i];
        }
        return ans;
    }

    @Override
    public String toString() {
        assertValid();
        StringBuilder sb = new StringBuilder((rank + 1) * 3);
        if (sign < 0) {
            sb.append("-");
        }
        sb.append(data[rank]);
        for (int i = rank - 1; i >= 0; i--) {
            int v = data[i];
            toString(v, 0, sb);
        }
        return sb.toString();
    }

    public int digitAt(int i) {
        int index = i / 3;
        if (index > rank) {
            return 0;
        }
        int offset = i % 3;
        if (offset == 0) {
            return data[index] % 10;
        }
        if (offset == 1) {
            return data[index] / 10 % 10;
        }
        return data[index] / 100;
    }

    private void toString(int v, int cur, StringBuilder sb) {
        if (cur == STR_LEN) {
            return;
        }
        toString(v / 10, cur + 1, sb);
        sb.append(v % 10);
    }

    @Override
    public int hashCode() {
        assertValid();
        int hashCode = sign;
        for (int i = rank; i >= 0; i--) {
            hashCode = hashCode * 31 + data[i];
        }
        return hashCode;
    }
}
