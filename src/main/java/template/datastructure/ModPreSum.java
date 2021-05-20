package template.datastructure;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class ModPreSum {
    private int[] pre;
    private int mod;
    private int n;

    public ModPreSum(int n, int mod) {
        pre = new int[n];
        this.mod = mod;
    }

    public void populate(IntToIntegerFunction a, int n) {
        this.n = n;
        if (n <= 0) {
            return;
        }
        pre[0] = a.apply(0);
        for (int i = 1; i < n; i++) {
            pre[i] = (pre[i - 1] + a.apply(i)) % mod;
        }
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public int intervalSum(int l, int r) {
        return DigitUtils.modsub(prefix(r), prefix(l - 1), mod);
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public int prefix(int i) {
        i = Math.min(i, n - 1);
        if (i < 0) {
            return 0;
        }
        return pre[i];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public int post(int i) {
        return DigitUtils.modsub(prefix(n), prefix(i - 1), mod);
    }
}
