package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Combination;
import template.math.Power;

public class DRadioTowers {
    int mod = 998244353;
    Combination comb = new Combination((int) 1e6, mod);
    CachedPow pow = new CachedPow(2, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long prob = 0;
        for (int i = 1; i <= n; i++) {
            if ((n - i) % 2 != 0) {
                continue;
            }
            int m = (n - i) / 2;
            long way = comb.combination(m + i - 1, i - 1);
            prob += way * pow.inverse(n) % mod;
        }
        prob %= mod;
        out.println(prob);
    }
}
