package template.rand;

import template.math.Modular;

public class ModifiableWholeHash {
    public static final Modular MOD = new Modular((int) (1e9 + 7));
    private int[] xs;
    private int hash;

    public ModifiableWholeHash(int x, int n) {
        xs = new int[n + 1];
        xs[0] = 1;
        for (int i = 1; i <= n; i++) {
            xs[i] = MOD.mul(xs[i - 1], x);
        }
        clear();
    }

    public ModifiableWholeHash(ModifiableWholeHash hash) {
        xs = hash.xs;
        clear();
    }

    public void clear() {
        hash = 0;
    }

    public void modify(int i, int v) {
        hash = MOD.valueOf(hash + (long) v * xs[i]);
    }

    public int hash() {
        return hash;
    }

    public int hashVerbose() {
        return MOD.plus(hash, xs[xs.length - 1]);
    }
}
