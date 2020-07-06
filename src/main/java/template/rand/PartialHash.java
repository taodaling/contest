package template.rand;

import template.primitve.generated.datastructure.IntToIntegerFunction;

public class PartialHash {
    HashData hd;
    int[] hash;

    public PartialHash(HashData hd) {
        this.hd = hd;
        hash = new int[hd.pow.length];
    }

    public void populate(IntToIntegerFunction function, int l, int r) {
        if (l > r) {
            return;
        }
        if (l > 0) {
            hash[l - 1] = 0;
        }
        hash[l] = hd.mod.mul(function.apply(l), hd.pow[l]);
        for (int i = l + 1; i <= r; i++) {
            hash[i] = hd.mod.valueOf(hash[i - 1] + hd.pow[i] * (long) function.apply(i));
        }
    }

    public int hash(int l, int r, boolean verbose) {
        if (l > r) {
            return verbose ? hd.pow[0] : 0;
        }
        long h = hash[r];
        if (l > 0) {
            h -= hash[l - 1];
            h *= hd.inv[l];
        }
        if (verbose) {
            h += hd.pow[r - l + 1];
        }
        return hd.mod.valueOf(h);
    }
}

