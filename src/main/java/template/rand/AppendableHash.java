package template.rand;

import template.math.Modular;
import template.math.Power;

public class AppendableHash {
    public static final Modular MOD = new Modular((int) (1e9 + 7));
    public static final Power POWER = new Power(MOD);
    private int[] inverse;
    private int[] xs;
    private int[] hash;
    int size;

    public AppendableHash(AppendableHash hash, int size) {
        this.inverse = hash.inverse;
        this.xs = hash.xs;
        this.hash = new int[size + 1];
    }

    public AppendableHash(int size, int x) {
        inverse = new int[size + 1];
        hash = new int[size + 1];
        xs = new int[size + 1];
        int invX = POWER.inverseByFermat(x);
        inverse[0] = 1;
        xs[0] = 1;
        for (int i = 1; i <= size; i++) {
            this.inverse[i] = MOD.mul(this.inverse[i - 1], invX);
            xs[i] = MOD.mul(xs[i - 1], x);
        }
    }

    public void append(int v) {
        hash[size] = MOD.mul(xs[size], v);
        if (size > 0) {
            hash[size] = MOD.plus(hash[size], hash[size - 1]);
        }
        size++;
    }

    public int size() {
        return size;
    }

    public void clear() {
        size = 0;
    }

    public int hashVerbose(int l, int r) {
        int h = hash(l, r);
        h = MOD.plus(h, xs[r - l + 1]);
        return h;
    }

    public int hash(int l, int r) {
        long h = hash[r];
        if (l > 0) {
            h -= hash[l - 1];
            h *= inverse[l];
        }
        return MOD.valueOf(h);
    }
}
