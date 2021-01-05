package on2020_12.on2020_12_27_AtCoder___AtCoder_Grand_Contest_051__Good_Bye_rng_58_Day_2_.A___Dodecagon;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;

public class ADodecagon {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int d = in.ri();
        int mod = 998244353;
        Combination comb = new Combination(d * 2, mod);
        int ans = comb.combination(d + d - 1, d);
        out.println(ans);
    }
}
