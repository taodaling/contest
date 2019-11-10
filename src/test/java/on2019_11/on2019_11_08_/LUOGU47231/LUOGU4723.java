package on2019_11.on2019_11_08_.LUOGU47231;



import template.*;

public class LUOGU4723 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);

        int n = in.readInt();
        int k = in.readInt();
        int[] f = new int[k + 1];
        for (int i = 1; i <= k; i++) {
            f[i] = mod.valueOf(in.readInt());
        }
        int[] a = new int[k];
        for (int i = 0; i < k; i++) {
            a[i] = mod.valueOf(in.readInt());
        }

        NumberTheoryTransform ntt = new NumberTheoryTransform(mod, 3);
        DigitUtils.Log2 log2 = new DigitUtils.Log2();
        int m = log2.ceilLog(k + 1) + 2;
        int[] p = new int[1 << m];
        int[] remainder = new int[1 << m];

        p[k] = 1;
        for (int i = 1; i <= k; i++) {
            int val = mod.valueOf(-f[i]);
            p[k - i] = val;
        }

        ntt.module(n, p, remainder, m);

        int ans = 0;
        for (int i = 1; i <= k; i++) {
            int c = remainder[k - i];
            int x = a[k - i];
            ans = mod.plus(ans, mod.mul(c, x));
        }

        out.println(ans);
    }
}
