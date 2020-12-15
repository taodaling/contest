package template.problem;

import template.math.DigitUtils;
import template.math.MultiplicativeFunctionSieve;
import template.math.Power;
import template.polynomial.ModContinuousInterpolation;

public class ExpKPrefixSum {
    private MultiplicativeFunctionSieve sieve;
    private ModContinuousInterpolation interpolation;
    private int[] y;
    private int mod;
    private Power power;

    public ExpKPrefixSum(int maxK, int mod) {
        sieve = new MultiplicativeFunctionSieve(maxK);
        interpolation = new ModContinuousInterpolation(maxK + 2, mod);
        this.mod = mod;
        this.power = new Power(mod);
        y = new int[maxK + 10];
    }

    /**
     * return \sum_{i=1}^n i^k in O(k)
     */
    public int solve(int k, int n) {
        if (k == 0) {
            return n % mod;
        }
        //0,1,...,k+1
        y[0] = 0;
        y[1] = 1;
        for (int i = 2; i <= k + 1; i++) {
            if (sieve.isComp[i]) {
                int factor = sieve.smallestPrimeFactor[i];
                y[i] = (int) ((long) y[factor] * y[i / factor] % mod);
            } else {
                y[i] = power.pow(i, k);
            }
        }
        for (int i = 1; i <= k + 1; i++) {
            y[i] = DigitUtils.modplus(y[i - 1], y[i], mod);
        }
        int ans = interpolation.interpolate(0, y, k + 2, n);
        return ans;
    }
}
