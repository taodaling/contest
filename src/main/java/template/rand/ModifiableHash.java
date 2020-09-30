package template.rand;

import java.util.Arrays;

public class ModifiableHash {
    HashData hd;
    int h;
    int[] vals;

    public ModifiableHash(HashData hd) {
        this.hd = hd;
        this.vals = new int[hd.pow.length];
    }

    public void reset() {
        h = 0;
        Arrays.fill(vals, 0);
    }

    public int hash() {
        return h;
    }

    public void modify(int i, int x) {
        h = hd.modular.plus(h, ((long) x - vals[i]) * hd.pow[i]);
        vals[i] = x;
    }
}
