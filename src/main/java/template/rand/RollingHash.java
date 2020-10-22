package template.rand;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDequeImpl;

public class RollingHash {
    HashData hd1;
    HashData hd2;
    IntegerDequeImpl dq;
    int h1;
    int h2;

    public RollingHash(HashData hd1, HashData hd2, int n) {
        this.hd1 = hd1;
        this.hd2 = hd2;
        dq = new IntegerDequeImpl(n + 10);
    }

    public void reset() {
        h1 = h2 = 0;
        dq.clear();
    }

    public void addLast(int x) {
        h1 = DigitUtils.mod((h1 + (long) hd1.pow[dq.size()] * x), hd1.mod);
        h2 = DigitUtils.mod((h2 + (long) hd2.pow[dq.size()] * x), hd2.mod);
        dq.addLast(x);
    }

    public void removeFirst() {
        int x = dq.removeFirst();
        h1 = DigitUtils.mod((long) (h1 - x) * hd1.inv[1], hd1.mod);
        h2 = DigitUtils.mod((long) (h2 - x) * hd2.inv[1], hd2.mod);
    }

    public long hash() {
        return DigitUtils.asLong(h1, h2);
    }

    public long hashV() {
        int ans1 = DigitUtils.modplus(h1, hd1.pow[dq.size() + 1], hd1.mod);
        int ans2 = DigitUtils.modplus(h2, hd2.pow[dq.size() + 1], hd2.mod);
        return DigitUtils.asLong(ans1, ans2);
    }
}
