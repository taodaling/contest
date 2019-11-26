package template.string;

import template.datastructure.ModBIT;
import template.math.Modular;
import template.math.Power;

public class ModifiableHash {
    public static final Modular MOD = new Modular((int) (1e9 + 7));
    public static final Power POWER = new Power(MOD);
    private int[] inverse;
    private int[] xs;
    private ModBIT bit;

    public ModifiableHash(int x, int n) {
        xs = new int[n + 1];
        inverse = new int[n + 1];
        bit = new ModBIT(n, MOD);

        xs[0] = 1;
        inverse[0] = 1;
        int invX = POWER.inverse(x);
        for (int i = 1; i <= n; i++) {
            xs[i] = MOD.mul(xs[i - 1], x);
            inverse[i] = MOD.mul(inverse[i - 1], invX);
        }
    }

    public void clear() {
        bit.clear();
    }

    public void set(int i, int v) {
        bit.update(i + 1, MOD.subtract(MOD.mul(v, xs[i]), bit.query(i + 1)));
    }

    public void modify(int i, int v) {
        bit.update(i + 1, MOD.mul(v, xs[i]));
    }

    public int hash(int l, int r) {
        int h = bit.query(r + 1);
        if (l > 0) {
            h = MOD.subtract(h, bit.query(l));
            h = MOD.mul(h, inverse[l]);
        }
        return h;
    }

    public int hashVerbose(int l, int r) {
        int h = hash(l, r);
        h = MOD.plus(h, xs[r - l + 1]);
        return h;
    }
}
