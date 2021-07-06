package template.datastructure;

import template.binary.Bits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

public class BitPreSum {
    private long[] bits;
    private int[] ps;
    private int n;

    public BitPreSum(int n) {
        bits = new long[(n + 63) / 64];
        ps = new int[(n + 63) / 64];
        this.n = n;
    }

    public void set(int i) {
        bits[i >> 6] |= 1L << (i & 63);
        ps[i >> 6]++;
    }

    public void clear(int n) {
        this.n = n;
        Arrays.fill(bits, 0, (n + 63) / 64, 0);
        Arrays.fill(ps, 0, (n + 63) / 64, 0);
    }

    public void build() {
        for (int i = 1, to = (n + 63) / 64; i < to; i++) {
            ps[i] += ps[i - 1];
        }
    }

    public void init(int n, IntPredicate predicate) {
        clear(n);
        for (int i = 0; i < n; i++) {
            if (predicate.test(i)) {
                set(i);
            }
        }
        build();
    }

    public int total() {
        return ps[((n + 63) >> 6) - 1];
    }

    public int prefix(int m) {
        if (m > n) {
            m = n - 1;
        }
        if (m < 0) {
            return 0;
        }
        int head = m >> 6;
        int tail = m & 63;
        return (head == 0 ? 0 : ps[head - 1]) + Long.bitCount(bits[head] & Bits.headLongMask(tail + 1));
    }

    public int interval(int l, int r) {
        return prefix(r) - prefix(l - 1);
    }

    @Override
    public String toString() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(Bits.get(bits[i >> 6], i & 63));
        }
        return list.toString();
    }
}
