package on2021_06.on2021_06_20_Codeforces___Codeforces_LATOKEN_Round_1__Div__1___Div__2_.Biological_Software_Utilities;



import org.apache.commons.math3.analysis.function.Pow;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.math.Power;

public class BiologicalSoftwareUtilities {
    int mod = 998244353;

    Factorial fact = new Factorial((int) 2e6, mod);
    Combination comb = new Combination(fact);
    Power pow = new Power(mod);

    public long tree(int n) {
        if (n <= 1) {
            return 1;
        }
        return (long) pow.pow(n, n - 2) * pow.pow(2, n - 1) % mod;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        if (n % 2 == 1) {
            out.println(0);
            return;
        }

        int halfN = n / 2;
        long ans = (long) comb.combination(n - 1, n / 2) * fact.fact(n / 2) % mod *
                tree(halfN) % mod;
        out.println(ans);
    }
}
