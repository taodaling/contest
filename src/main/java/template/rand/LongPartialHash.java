package template.rand;

import template.primitve.generated.datastructure.IntToLongFunction;

public class LongPartialHash {
    LongHashData hd;
    long[] hash;

    public LongPartialHash(LongHashData hd) {
        this.hd = hd;
        hash = new long[hd.pow.length];
    }

    public void populate(IntToLongFunction function, int l, int r) {
        if (l > r) {
            return;
        }
        if (l > 0) {
            hash[l - 1] = 0;
        }
        hash[l] = hd.mod.mul(function.apply(l), hd.pow[l]);
        for (int i = l + 1; i <= r; i++) {
            hash[i] = hd.mod.plus(hash[i - 1], hd.mod.mul(hd.pow[i], function.apply(i)));
        }
    }

    public long hash(int l, int r, boolean verbose) {
        if (l > r) {
            return verbose ? hd.pow[0] : 0;
        }
        long h = hash[r];
        if (l > 0) {
            h = hd.mod.subtract(h, hash[l - 1]);
            h = hd.mod.mul(h, hd.inv[l]);
        }
        if (verbose) {
            h = hd.mod.plus(h, hd.pow[r - l + 1]);
        }
        return h;
    }
}
