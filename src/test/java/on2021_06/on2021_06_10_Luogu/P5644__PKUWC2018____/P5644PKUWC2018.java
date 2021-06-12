package on2021_06.on2021_06_10_Luogu.P5644__PKUWC2018____;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.math.Power;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.problem.CountSubsetSum;
import template.utils.Debug;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

public class P5644PKUWC2018 {
    int mod = 998244353;
    InverseNumber inv = new ModPrimeInverseNumber((int) 1e5, mod);
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] w = in.ri(n);
        IntPoly poly = new IntPolyNTT(mod);
        int[] other = Arrays.copyOfRange(w, 1, n);
        int s = Arrays.stream(other).sum();
        CountSubsetSum css = new CountSubsetSum(Arrays.copyOfRange(w, 1, n),
                s, mod, poly, i -> -1);

        debug.debug("css", css);
        long fail = 0;
        for (int i = 1; i <= s; i++) {
            int prod = css.query(i);
            if (prod == 0) {
                continue;
            }
            long prob = (long) w[0] * inv.inverse(w[0] + i) % mod;
            long contrib = prob * -prod % mod;
            fail += contrib;
        }

        long success = 1 - fail;
        success = DigitUtils.mod(success, mod);
        out.println(success);
    }

}
