package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitCount;
import template.math.DigitUtils;
import template.math.Power;
import template.primitve.generated.datastructure.DoublePreSum;
import template.utils.Debug;

import java.util.Arrays;

public class FDoYouLikeQueryProblems {
    Debug debug = new Debug(false);
    int mod = 998244353;
    Power pow = new Power(mod);
    Combination comb = new Combination((int) 1e6, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        long sum = 0;
        long totalProb = m * 2 + 1;
        long invProb = pow.inverse((int)totalProb);
        for (int j = 1; j <= n; j++) {
            long p2 = (long) j * (n - j + 1) % mod * comb.invCombination(n + 1, 2) % mod * invProb % mod;
            long p1 = (long) j * (n - j + 1) % mod * comb.invCombination(n + 1, 2) % mod;
            long x = p1 * m % mod * invProb % mod;
            long y = DigitUtils.mod(1 - x, mod);
            long s;
            if (y == 1) {
                s = 0;
            } else {
                //x1+...+xq
                s = q - ((long)pow.pow((int) y, q) - 1) * pow.inverse((int) y - 1) % mod;
            }
            s = s * p2 % mod;
            sum += s;
            debug.debug("j", j);
            debug.debug("s", s);
        }
        sum %= mod;
        sum = sum * (m - 1) % mod * pow.inverse(2) % mod;
        sum = DigitUtils.mod(sum, mod);
        long total = (long) (n + 1) * n / 2 % mod * (2 * m + 1) % mod;
        total = pow.pow((int) total, q);
        long ans = total * sum % mod;
        out.println(ans);
    }
}
