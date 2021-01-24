package template.math;

import template.primitve.generated.datastructure.LongArrayList;

import java.util.Arrays;

public class LongRadix {
    private long[] pow;
    private long base;

    public LongRadix(long base) {
        if (base <= 1) {
            throw new IllegalArgumentException();
        }
        this.base = base;
        LongArrayList ll = new LongArrayList(64);
        ll.add(1);
        while (!DigitUtils.isMultiplicationOverflow(ll.tail(), base, Long.MAX_VALUE)) {
            ll.add(ll.tail() * base);
        }
        pow = ll.toArray();
    }

    public int floorLog(long x) {
        int ans = 0;
        while (x >= base) {
            x /= base;
            ans++;
        }
        return ans;
    }

    public int ceilLog(long x) {
        int k = floorLog(x);
        if (valueOfBit(k) < x) {
            k++;
        }
        return k;
    }

    public long valueOfBit(int i) {
        return pow[i];
    }

    public int get(long x, int i) {
        if (i >= pow.length) {
            return 0;
        }
        return (int) (x / pow[i] % base);
    }

    public long set(long x, int i, long val) {
        return x + (val - get(x, i)) * pow[i];
    }

    public int bitCount() {
        return pow.length;
    }

    public int[] decompose(long x) {
        return decompose(x, new int[bitCount()]);
    }

    public int[] decompose(long x, int[] ans) {
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (x % base);
            x /= base;
        }
        return ans;
    }

    public long compose(int[] bits) {
        if (bits.length > bitCount()) {
            throw new IllegalArgumentException();
        }
        long ans = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            ans = ans * base + bits[i];
        }
        return ans;
    }

    public String toString(long x) {
        int[] bits = decompose(x);
        return Arrays.toString(bits);
    }
}
