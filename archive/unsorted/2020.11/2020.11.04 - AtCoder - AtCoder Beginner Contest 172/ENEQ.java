package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;

public class ENEQ {

    int mod = (int) (1e9 + 7);
    Factorial fact = new Factorial((int) 1e6, mod);
    Combination comb = new Combination(fact);


    public long pow2(long x) {
        return x * x % mod;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        long ans = 0;
        for (int i = 0; i <= n; i++) {
            long contrib = (long) comb.combination(n, i) * comb.combination(m, i) % mod * fact.fact(i) % mod *
                    pow2((long) comb.combination(m - i, n - i) * fact.fact(n - i) % mod) % mod;
            if (i % 2 == 1) {
                contrib = -contrib;
            }
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
