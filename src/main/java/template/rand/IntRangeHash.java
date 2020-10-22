package template.rand;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class IntRangeHash implements RangeHash {
    HashData hd1;
    HashData hd2;
    int[] h1;
    int[] h2;

    public IntRangeHash(HashData hd1, HashData hd2, int n) {
        this.hd1 = hd1;
        this.hd2 = hd2;
        h1 = new int[n + 10];
        h2 = new int[n + 10];
    }

    private void normalize(int i) {
        h1[i] = DigitUtils.mod(h1[i], hd1.mod);
        h2[i] = DigitUtils.mod(h2[i], hd1.mod);
    }

    public void populate(IntToIntegerFunction function, int n) {
        if (n == 0) {
            return;
        }
        h1[0] = h2[0] = function.apply(0);
        normalize(0);
        for (int i = 1; i < n; i++) {
            h1[i] = h2[i] = function.apply(i);
            normalize(i);
            h1[i] = (int) (((long) h1[i] * hd1.pow[i] + h1[i - 1]) % hd1.mod);
            h2[i] = (int) (((long) h2[i] * hd2.pow[i] + h2[i - 1]) % hd2.mod);
        }
    }

    public long hash(int l, int r) {
        if (r < l) {
            return 0;
        }
        int ans1 = h1[r];
        int ans2 = h2[r];
        if (l > 0) {
            ans1 = DigitUtils.modsub(ans1, h1[l - 1], hd1.mod);
            ans2 = DigitUtils.modsub(ans2, h2[l - 1], hd2.mod);
            ans1 = (int) ((long) ans1 * hd1.inv[l] % hd1.mod);
            ans2 = (int) ((long) ans2 * hd2.inv[l] % hd2.mod);
        }
        return DigitUtils.asLong(ans1, ans2);
    }

    public long hashV(int l, int r) {
        if (r < l) {
            return DigitUtils.asLong(1, 1);
        }
        int ans1 = DigitUtils.modplus(h1[r], hd1.pow[r + 1], hd1.mod);
        int ans2 = DigitUtils.modplus(h2[r], hd2.pow[r + 1], hd2.mod);
        if (l > 0) {
            ans1 = DigitUtils.modsub(ans1, h1[l - 1], hd1.mod);
            ans2 = DigitUtils.modsub(ans2, h2[l - 1], hd2.mod);
            ans1 = (int) ((long) ans1 * hd1.inv[l] % hd1.mod);
            ans2 = (int) ((long) ans2 * hd2.inv[l] % hd2.mod);
        }
        return DigitUtils.asLong(ans1, ans2);
    }
}

