package template.rand;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongBIT;

public class ModifiableIntRangeHash implements RangeHash {
    HashData hd1;
    HashData hd2;
    LongBIT bit1;
    LongBIT bit2;

    /**
     * O(n)
     */
    public ModifiableIntRangeHash(HashData hd1, HashData hd2, int n) {
        this.hd1 = hd1;
        this.hd2 = hd2;
        bit1 = new LongBIT(n);
        bit2 = new LongBIT(n);
    }

    public void clear(){
        bit1.clear();
        bit2.clear();
    }

    /**
     * O(log n)
     */
    public void set(int i, int x) {
        bit1.set(i + 1, (long) x * hd1.pow[i] % hd1.mod);
        bit2.set(i + 1, (long) x * hd2.pow[i] % hd2.mod);
    }
    /**
     * O(log n)
     */
    public void modify(int i, int x) {
        bit1.update(i + 1, (long) x * hd1.pow[i] % hd1.mod);
        bit2.update(i + 1, (long) x * hd2.pow[i] % hd2.mod);
    }
    /**
     * O(log n)
     */
    private long hash1(int l, int r) {
        return DigitUtils.mod(bit1.query(l + 1, r + 1) % hd1.mod * hd1.inv[l], hd1.mod);
    }
    /**
     * O(log n)
     */
    private long hash2(int l, int r) {
        return DigitUtils.mod(bit2.query(l + 1, r + 1) % hd2.mod * hd2.inv[l], hd2.mod);
    }
    /**
     * O(log n)
     */
    @Override
    public long hash(int l, int r) {
        long h1 = hash1(l, r);
        long h2 = hash2(l, r);
        return DigitUtils.asLong((int) h1, (int) h2);
    }
    /**
     * O(log n)
     */
    @Override
    public long hashV(int l, int r) {
        long h1 = DigitUtils.modplus(hash1(l, r), hd1.pow[r - l + 1], hd1.mod);
        long h2 = DigitUtils.modplus(hash2(l, r), hd2.pow[r - l + 1], hd2.mod);
        return DigitUtils.asLong((int) h1, (int) h2);
    }
}
