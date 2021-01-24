package on2021_01.on2021_01_15_CSES___CSES_Problem_Set.Functional_Graph_Distribution;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Combination;
import template.math.Factorial;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class FunctionalGraphDistribution {
    int mod = (int) 1e9 + 7;
    Factorial fact = new Factorial((int) 1e4, mod);
    Combination comb = new Combination(fact);
    long[][] dp;
    long[] f;

    Debug debug = new Debug(true);
    int time = 0;
    public long dp(int n, int k) {
        time++;
        if(n == 0 || k == 0){
            return n == 0 && k == 0 ? 1 : 0;
        }
        if (dp[n][k] == -1) {
            long sum = 0;
            for (int i = 0; i < n; i++) {
                sum += dp(i, k - 1) * f[n - i] % mod * comb.combination(n - 1, n - i - 1) % mod;
            }
            dp[n][k] = sum % mod;
        }
        return dp[n][k];
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] powN = new long[n + 1];
        f = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            long sum = 0;
            powN[0] = 1;
            for (int j = 1; j <= i; j++) {
                powN[j] = powN[j - 1] * i % mod;
            }
            for (int j = 1; j <= i; j++) {
                if (i == j) {
                    sum += (long) comb.combination(i, j) * fact.fact(j - 1) % mod;
                } else {
                    sum += (long) comb.combination(i, j) * fact.fact(j) % mod * powN[i - j - 1] % mod;
                }
            }
            f[i] = sum % mod;
        }
        debug.elapse("f");
        dp = new long[n + 1][n + 1];
        SequenceUtils.deepFill(dp, (long) -1);

        debug.elapse("start dp");
        for(int i = 1; i <= n; i++){
            debug.debug("i", i);
            debug.debug("time", time);
            out.println(dp(n, i));
        }
        debug.debug("time", time);
    }
}
