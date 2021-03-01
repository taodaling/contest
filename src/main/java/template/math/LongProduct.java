package template.math;

import java.util.Arrays;

public class LongProduct implements Comparable<LongProduct> {
    long[] data = new long[4];
    int sign;

    public void mul(long a, long b) {
        if (a == 0 || b == 0) {
            sign = 0;
            data[0] = data[1] = data[2] = data[3] = 0;
            return;
        }
        sign = 1;
        if (a < 0) {
            a = -a;
            sign = -sign;
        }
        if (b < 0) {
            b = -b;
            sign = -sign;
        }
        long ha = a >>> 32;
        long la = a & DigitUtils.INT_TO_LONG_MASK;
        long hb = b >>> 32;
        long lb = b & DigitUtils.INT_TO_LONG_MASK;
        long lalb = la * lb;
        long lahb = la * hb;
        long halb = ha * lb;
        long hahb = ha * hb;
        data[0] = lalb & DigitUtils.INT_TO_LONG_MASK;
        data[1] = (lalb >>> 32) + (lahb & DigitUtils.INT_TO_LONG_MASK) +
                (halb & DigitUtils.INT_TO_LONG_MASK);
        data[2] = (lahb >>> 32) + (halb >>> 32) + (hahb & DigitUtils.INT_TO_LONG_MASK);
        data[3] = (hahb >>> 32);
        data[2] += data[1] >>> 32;
        data[1] &= DigitUtils.INT_TO_LONG_MASK;
        data[3] += data[2] >>> 32;
        data[2] &= DigitUtils.INT_TO_LONG_MASK;
    }

    public int getSign() {
        return sign;
    }

    @Override
    public int compareTo(LongProduct o) {
        if (sign != o.sign) {
            return Integer.compare(sign, o.sign);
        }
        for (int i = 3; i >= 0; i--) {
            if (data[i] != o.data[i]) {
                return Long.compare(data[i], o.data[i]) * sign;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return (sign == -1 ? "-" : "") + Arrays.toString(data);
    }
}
