package template.math;

import template.primitve.generated.datastructure.IntegerList;

import java.util.Arrays;

public class IntRadix {
    private int[] pow;
    private int base;

    public IntRadix(int base) {
        if (base <= 1) {
            throw new IllegalArgumentException();
        }
        this.base = base;
        IntegerList ll = new IntegerList(32);
        ll.add(1);
        while (!DigitUtils.isMultiplicationOverflow(ll.tail(), base, Integer.MAX_VALUE)) {
            ll.add(ll.tail() * base);
        }
        pow = ll.toArray();
    }

    public int floorLog(int x) {
        int ans = 0;
        while (x >= base) {
            x /= base;
            ans++;
        }
        return ans;
    }

    public int ceilLog(int x) {
        int k = floorLog(x);
        if (valueOfBit(k) < x) {
            k++;
        }
        return k;
    }

    public int valueOfBit(int i) {
        return pow[i];
    }

    public int get(int x, int i) {
        return (x / pow[i] % base);
    }

    public int set(int x, int i, int val) {
        return x + (val - get(x, i)) * pow[i];
    }

    public int bitCount() {
        return pow.length;
    }

    public int[] decompose(int x) {
        return decompose(x, new int[bitCount()]);
    }

    public int[] decompose(int x, int[] ans) {
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (x % base);
            x /= base;
        }
        return ans;
    }

    public int compose(int[] bits) {
        if (bits.length > bitCount()) {
            throw new IllegalArgumentException();
        }
        int ans = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            ans = ans * base + bits[i];
        }
        return ans;
    }

    public String toString(int x) {
        int[] bits = decompose(x);
        return Arrays.toString(bits);
    }
}
