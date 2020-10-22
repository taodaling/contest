package template.rand;

import template.math.DigitUtils;

import java.util.Arrays;

public class ModifiableHash {
    HashData hd1;
    HashData hd2;
    int h1;
    int h2;
    int[] data;

    public ModifiableHash(HashData hd1, HashData hd2, int n) {
        this.hd1 = hd1;
        this.hd2 = hd2;
        this.data = new int[n + 10];
        this.data = new int[n + 10];
    }

    public void reset() {
        h1 = h2 = 0;
        Arrays.fill(data, 0);
    }

    public long hash() {
        return DigitUtils.asLong(h1, h2);
    }

    public void modify(int i, int x) {
        h1 = DigitUtils.mod(h1 + (long) (x - data[i]) * hd1.pow[i], hd1.mod);
        h2 = DigitUtils.mod(h2 + (long) (x - data[i]) * hd2.pow[i], hd2.mod);
        data[i] = x;
    }
}
