package template.string;

import template.math.Modular;
import template.math.Power;
import template.rand.ToHash;

public class Hash {
    public static final Modular MOD = new Modular((int) (1e9 + 7));
    public static final Power POWER = new Power(MOD);
    private int[] inverse;
    private int[] xs;
    private int[] hash;

    public Hash(Hash model) {
        inverse = model.inverse;
        hash = new int[model.hash.length];
        xs = model.xs;
    }

    public Hash(int size, int x) {
        inverse = new int[size + 1];
        hash = new int[size + 1];
        xs = new int[size + 1];
        int invX = POWER.inverse(x);
        inverse[0] = 1;
        xs[0] = 1;
        for (int i = 1; i <= size; i++) {
            this.inverse[i] = MOD.mul(this.inverse[i - 1], invX);
            xs[i] = MOD.mul(xs[i - 1], x);
        }
    }

    public <T> void populate(T[] data, int n, ToHash<T> toHash) {
        hash[0] = toHash.hash(data[0]);
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(toHash.hash(data[i]), xs[i]));
        }
    }

    public void populate(Object[] data, int n) {
        hash[0] = data[0].hashCode();
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(data[i].hashCode(), xs[i]));
        }
    }

    public void populate(int[] data, int n) {
        hash[0] = data[0];
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(data[i], xs[i]));
        }
    }

    public void populate(char[] data, int n) {
        hash[0] = data[0];
        for (int i = 1; i < n; i++) {
            hash[i] = MOD.plus(hash[i - 1], MOD.mul(data[i], xs[i]));
        }
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
