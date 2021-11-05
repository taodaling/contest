package on2021_10.on2021_10_28_Library_Checker.Task0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.*;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int r = in.ri();
        int d = in.ri();
        long n = in.rl();
        int mod = 998244353;

        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(d);
        int[] f = sieve.pow(d, new Power(mod));
        int ans = IntMath.sumOfExponentialTimesPolynomial(r, f, mod, new Factorial(d + 1, mod), n);
        out.println(ans);
    }
}
