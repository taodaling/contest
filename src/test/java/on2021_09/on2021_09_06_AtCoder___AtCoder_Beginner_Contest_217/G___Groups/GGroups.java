package on2021_09.on2021_09_06_AtCoder___AtCoder_Beginner_Contest_217.G___Groups;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Factorial;
import template.math.Power;

public class GGroups {
    int mod = 998244353;
    Factorial fact = new Factorial((int) 1e5, mod);
    Combination comb = new Combination(fact);
    Power pow = new Power(mod);

    public int comb(int[] pf, int[] pinv, int n, int m) {
        if (n < m) {
            return 0;
        }
        return (int) ((long) pf[n] * pinv[n - m] % mod);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int a = DigitUtils.ceilDiv(n, m);
        int x = (n - 1) % m + 1;
        int y = m - x;

        int[] pfx = fact.fact.clone();
        int[] pfy = fact.fact.clone();
        int[] pinvx = fact.inv.clone();
        int[] pinvy = fact.inv.clone();
        for (int i = 0; i < pfx.length; i++) {
            pfx[i] = pow.pow(pfx[i], x);
            pfy[i] = pow.pow(pfy[i], y);
            pinvx[i] = pow.pow(pinvx[i], x);
            pinvy[i] = pow.pow(pinvy[i], y);
        }

        for (int k = 1; k <= n; k++) {
            long ans = 0;
            for (int i = 0; i <= k; i++) {
                long contrib = (long) comb.combination(k, i)
                        * comb(pfx, pinvx, k - i, a) % mod
                        * comb(pfy, pinvy, k - i, a - 1) % mod;
                if (i % 2 == 1) {
                    contrib = -contrib;
                }
                ans += contrib;
            }
            ans = ans % mod * fact.invFact(k);
            ans = DigitUtils.mod(ans, mod);
            out.println(ans);
        }
    }
}
