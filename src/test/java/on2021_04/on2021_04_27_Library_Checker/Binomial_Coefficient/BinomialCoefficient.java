package on2021_04.on2021_04_27_Library_Checker.Binomial_Coefficient;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.ExtLucas;
import template.math.Lucas;
import template.utils.Debug;

public class BinomialCoefficient {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = in.ri();
        int m = in.ri();
        ExtLucas lucas = new ExtLucas(m, (long) 1e18);
        for (int i = 0; i < t; i++) {
            debug.debug("i", i);
            long n = in.rl();
            long k = in.rl();
            out.println(lucas.combination(n, k));
        }
    }
}
