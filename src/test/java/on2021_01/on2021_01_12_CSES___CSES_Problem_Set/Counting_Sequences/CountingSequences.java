package on2021_01.on2021_01_12_CSES___CSES_Problem_Set.Counting_Sequences;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.MultiplicativeFunctionSieve;
import template.math.Power;

public class CountingSequences {
    int mod = (int) 1e9 + 7;
    int limit = (int) 1e6;
    Combination comb = new Combination(limit, mod);
    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(limit);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] pn = sieve.pow(n, pow);
        long sum = 0;
        for (int i = 0; i <= k; i++) {
            long contrib = pn[k - i];
            contrib = contrib * comb.combination(k, i);
            if ((i & 1) == 1) {
                contrib = -contrib;
            }
            sum += contrib % mod;
        }
        sum = DigitUtils.mod(sum, mod);
        out.println(sum);
    }
}
