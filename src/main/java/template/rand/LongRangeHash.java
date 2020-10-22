package template.rand;

import template.primitve.generated.datastructure.IntToLongFunction;

public class LongRangeHash implements RangeHash {
    LongHashData hd;
    long[] hash;

    public LongRangeHash(LongHashData hd, int n) {
        this.hd = hd;
        hash = new long[n + 10];
    }

    public void populate(IntToLongFunction function, int n) {
        if (n == 0) {
            return;
        }
        hash[0] = hd.mod.valueOf(function.apply(0));
        for (int i = 1; i < n; i++) {
            hash[i] = hd.mod.plus(hash[i - 1], hd.mod.mul(hd.pow[i], function.apply(i)));
        }
    }

    public long hash(int l, int r) {
        if (l > r) {
            return 0;
        }
        long h = hash[r];
        if (l > 0) {
            h = hd.mod.subtract(h, hash[l - 1]);
            h = hd.mod.mul(h, hd.inv[l]);
        }
        return h;
    }

    public long hashV(int l, int r) {
        if (l > r) {
            return 1;
        }
        long h = hd.mod.plus(hash[r], hd.pow[r + 1]);
        if (l > 0) {
            h = hd.mod.subtract(h, hash[l - 1]);
            h = hd.mod.mul(h, hd.inv[l]);
        }
        return h;
    }
}
