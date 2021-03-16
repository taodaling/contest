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

    private int hash1(int l, int r) {
        if (l > r) {
            return 0;
        }
        int ans1 = h1[r];
        if (l > 0) {
            ans1 = (int) (((long)ans1 - h1[l - 1] + hd1.mod) * hd1.inv[l] % hd1.mod);
        }
        return ans1;
    }

    private int hash1(int l1, int r1, int l2, int r2) {
        int left = hash1(l1, r1);
        int right = hash1(l2, r2);
        return (int) ((left + (long) right * hd1.pow[r1 - l1 + 1]) % hd1.mod);
    }

    private int hash2(int l1, int r1, int l2, int r2) {
        int left = hash2(l1, r1);
        int right = hash2(l2, r2);
        return (int) ((left + (long) right * hd2.pow[r1 - l1 + 1]) % hd2.mod);
    }

    private int hash2(int l, int r) {
        if (l > r) {
            return 0;
        }
        int ans2 = h2[r];
        if (l > 0) {
            ans2 = (int) (((long)ans2 - h2[l - 1] + hd2.mod) * hd2.inv[l] % hd2.mod);
        }
        return ans2;
    }

    public static boolean equal(IntRangeHash rh1, int l, int r, IntRangeHash rh2, int L, int R) {
        return (r - l) == (R - L) && rh1.hash1(l, r) == rh2.hash1(L, R) &&
                rh1.hash2(l, r) == rh2.hash2(L, R);
    }

    public long hash(int l, int r) {
        return DigitUtils.asLong(hash1(l, r), hash2(l, r));
    }

    public long hash(int l1, int r1, int l2, int r2) {
        return DigitUtils.asLong(hash1(l1, r1, l2, r2),
                hash2(l1, r1, l2, r2));
    }

    public long hashV(int l1, int r1, int l2, int r2) {
        int ans1 = hash1(l1, r1, l2, r2);
        int ans2 = hash2(l1, r1, l2, r2);
        int len = r1 - l1 + 1 + r2 - l2 + 1;
        ans1 = DigitUtils.modplus(ans1, hd1.pow[len], hd1.mod);
        ans2 = DigitUtils.modplus(ans2, hd2.pow[len], hd2.mod);
        return DigitUtils.asLong(ans1, ans2);
    }

    public long hashV(int l, int r) {
        int ans1 = hash1(l, r);
        int ans2 = hash2(l, r);
        int len = r - l + 1;
        ans1 = DigitUtils.modplus(ans1, hd1.pow[len], hd1.mod);
        ans2 = DigitUtils.modplus(ans2, hd2.pow[len], hd2.mod);
        return DigitUtils.asLong(ans1, ans2);
    }


}

