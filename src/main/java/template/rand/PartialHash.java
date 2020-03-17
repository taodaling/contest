package template.rand;

import java.util.function.IntUnaryOperator;

public class PartialHash {
    HashData hd;
    int[] hash;

    public PartialHash(HashData hd) {
        this.hd = hd;
        hash = new int[hd.pow.length];
    }

    public <T> void populate(IntUnaryOperator function, int l, int r) {
        if (l > 0) {
            hash[l - 1] = 0;
        }
        hash[l] = hd.mod.mul(function.applyAsInt(l), hd.pow[l]);
        for (int i = l + 1; i <= r; i++) {
            hash[i] = hd.mod.valueOf(hash[i - 1] + hd.pow[i] * (long) function.applyAsInt(i));
        }
    }

    public int hash(int l, int r, boolean verbose) {
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

