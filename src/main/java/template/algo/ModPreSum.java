package template.algo;

import template.math.Modular;

public class ModPreSum {
    private int[] pre;
    private Modular mod;

    public ModPreSum(int n, Modular mod) {
        pre = new int[n];
        this.mod = mod;
    }

    public void populate(int[] a){
        int n = a.length;
        pre[0] = a[0];
        for (int i = 1; i < n; i++) {
            pre[i] = mod.plus(pre[i - 1], a[i]);
        }
    }

    public ModPreSum(int[] a, Modular mod) {
        this(a.length, mod);
        populate(a);
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public int intervalSum(int l, int r) {
        if (l == 0) {
            return pre[r];
        }
        return mod.subtract(pre[r], pre[l - 1]);
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public int prefix(int i) {
        return pre[i];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public int post(int i) {
        if (i == 0) {
            return pre[pre.length - 1];
        }
        return mod.subtract(pre[pre.length - 1], pre[i - 1]);
    }
}
