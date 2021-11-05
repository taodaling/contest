package on2021_10.on2021_10_28_Library_Checker.Task;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.IntMath;
import template.math.MultiplicativeFunctionSieve;
import template.math.Power;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int r = in.ri();
        int d = in.ri();
        int mod = 998244353;

        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(d);
        int[] f = sieve.pow(d, new Power(mod));
        Combination comb = new Combination(d + 1, mod);
        int ans = IntMath.sumOfExponentialTimesPolynomial(r, f, mod, comb);
        out.println(ans);
    }
}
