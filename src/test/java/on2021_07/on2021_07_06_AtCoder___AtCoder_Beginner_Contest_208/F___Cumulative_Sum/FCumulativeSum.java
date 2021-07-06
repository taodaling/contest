package on2021_07.on2021_07_06_AtCoder___AtCoder_Beginner_Contest_208.F___Cumulative_Sum;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;
import template.math.Power;
import template.polynomial.ModContinuousInterpolation;

public class FCumulativeSum {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.rl();
        int m = in.ri();
        int k = in.ri();
        int L = 3000000;
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(L);
        int[] powers = sieve.pow(k, pow);
        for (int i = 0; i < m; i++) {
            for(int j = 1; j < L; j++){
                powers[j] += powers[j - 1];
                if(powers[j] >= mod){
                    powers[j] -= mod;
                }
            }
        }
        ModContinuousInterpolation interpolation = new ModContinuousInterpolation(L, mod);
        long ans = interpolation.interpolate(0, powers, L, n);
        out.println(ans);
    }
}
