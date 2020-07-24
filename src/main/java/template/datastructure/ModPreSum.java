package template.datastructure;

import template.math.Modular;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class ModPreSum {
    private int[] pre;
    private Modular mod;

    public ModPreSum(int n, Modular mod) {
        pre = new int[n];
        this.mod = mod;
    }

    public void populate(IntToIntegerFunction a, int n) {
        if(n <= 0){
            return;
        }
        pre[0] = a.apply(0);
        for (int i = 1; i < n; i++) {
            pre[i] = mod.plus(pre[i - 1], a.apply(i));
        }
    }

    /**
     * get a[l] + a[l + 1] + ... + a[r]
     */
    public int intervalSum(int l, int r) {
        return mod.subtract(prefix(r), prefix(l - 1));
    }

    /**
     * get a[0] + a[1] + ... + a[i]
     */
    public int prefix(int i) {
        if (i < 0) {
            return 0;
        }
        return pre[Math.min(i, pre.length - 1)];
    }

    /**
     * get a[i] + a[i + 1] + ... + a[n - 1]
     */
    public int suffix(int i) {
        return mod.subtract(pre[pre.length - 1], prefix(i - 1));
    }
}
