package contest;

import template.FastInput;
import template.FastOutput;
import template.ModBIT;
import template.NumberTheory;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] cnts = new int[n + 1];
        int l = n;
        int[] leftUntil = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            while (l - 1 >= 0 && cnts[a[l - 1]] == 0) {
                cnts[a[l - 1]]++;
                l--;
            }
            leftUntil[i] = l;
            cnts[a[i]]--;
        }

        ModBIT bit = new ModBIT(n + 1, mod);
        bit.update(1, 1);
        for (int i = 2; i <= n + 1; i++) {
            int lTo = Math.max(2 + cnts[i - 2], i - k + 1);
            bit.update(i, mod.subtract(bit.query(i - 1),
                    bit.query(lTo - 2)));
        }

        int ans = mod.subtract(bit.query(n + 1), bit.query(n));
        out.println(ans);
    }
}
