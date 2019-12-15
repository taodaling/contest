package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

public class ETheTopScorer {
    Modular mod = new Modular(998244353);
    Composite comp = new Composite(10000, mod);
    Power power = new Power(mod);
    InverseNumber inverseNumber = new InverseNumber(10000, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int p = in.readInt();
        int s = in.readInt();
        int r = in.readInt();

        int total = query(p, s + 1, s - r);

        int ans = 0;
        for (int i = r; i <= s; i++) {
            for (int j = 1; j <= p && j * i <= s; j++) {
                int contrib = query(p - j, i, s - j * i);
                contrib = mod.mul(contrib, comp.composite(p - 1, j - 1));
                ans = mod.plus(ans, mod.mul(contrib, inverseNumber.inverse(j)));
            }
        }

        ans = mod.mul(ans, power.inverse(total));
        out.println(ans);
    }

    public int query(int n, int k, int m) {
        if (n == 0) {
            return m == 0 ? 1 : 0;
        }
        if (k == 0) {
            return 0;
        }

        int ans = 0;
        for (int i = 0; i <= n && m >= i * k; i++) {
            int contrib = comp.composite(m - i * k + n - 1, n - 1);
           contrib = mod.mul(contrib, comp.composite(n, i));
            if (i % 2 == 1) {
                contrib = mod.valueOf(-contrib);
            }
            ans = mod.plus(ans, contrib);
        }
        return ans;
    }
}
