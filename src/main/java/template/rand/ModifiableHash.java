package template.rand;

import template.math.DigitUtils;

import java.util.Arrays;

public class ModifiableHash {
    HashData hd1;
    HashData hd2;
    int h1;
    int h2;
    int[] data;
    int n;

    public ModifiableHash(HashData hd1, HashData hd2, int n) {
        this.hd1 = hd1;
        this.hd2 = hd2;
        this.data = new int[n + 10];
        this.data = new int[n + 10];
    }

    public void init() {
        init(data.length);
    }

    public void init(int n) {
        h1 = h2 = 0;
        this.n = n;
        Arrays.fill(data, 0, n, 0);
    }

    public int get(int i) {
        return data[i];
    }

    public long hash() {
        return DigitUtils.asLong(h1, h2);
    }

    public long hashV() {
        long high = DigitUtils.modplus(h1, hd1.pow[n], hd1.mod);
        long low = DigitUtils.modplus(h2, hd2.pow[n], hd2.mod);
        return DigitUtils.asLong(high, low);
    }

    public void set(int i, int x) {
        h1 = DigitUtils.mod(h1 + (long) (x - data[i]) * hd1.pow[i], hd1.mod);
        h2 = DigitUtils.mod(h2 + (long) (x - data[i]) * hd2.pow[i], hd2.mod);
        data[i] = x;
    }

    public void modify(int i, int x) {
        h1 = DigitUtils.mod(h1 + (long) x * hd1.pow[i], hd1.mod);
        h2 = DigitUtils.mod(h2 + (long) x * hd2.pow[i], hd2.mod);
        data[i] += x;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}
