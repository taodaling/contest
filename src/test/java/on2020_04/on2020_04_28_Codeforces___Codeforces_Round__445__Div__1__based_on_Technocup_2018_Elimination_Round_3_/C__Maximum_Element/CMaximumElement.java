package on2020_04.on2020_04_28_Codeforces___Codeforces_Round__445__Div__1__based_on_Technocup_2018_Elimination_Round_3_.C__Maximum_Element;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.math.Modular;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class CMaximumElement {
    Modular mod = new Modular(1e9 + 7);
    Factorial factorial = new Factorial(1000000, mod);
    Combination comb = new Combination(factorial);

    int[] g;
    int[] h;
    int k;

    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();

        g = new int[n + 1];
        h = new int[n + 1];
        SequenceUtils.deepFill(g, -1);
        SequenceUtils.deepFill(h, -1);

        int ans = mod.mul(factorial.fact(n - 1), h(n - 1));
        int total = factorial.fact(n);

        debug.debug("g", g);
        debug.debug("h", h);

        out.println(mod.subtract(total, ans));
    }

    public int h(int n) {
        if (n < 0) {
            return 0;
        }
        if (h[n] == -1) {
            h[n] = mod.plus(h(n - 1), mod.mul(g(n), factorial.invFact(n)));
        }
        return h[n];
    }

    public int g(int n) {
        if (g[n] == -1) {
            if (n == 0) {
                return g[n] = 1;
            }
            g[n] = mod.mul(factorial.fact(n - 1), mod.subtract(h(n - 1), h(n - k - 1)));
        }
        return g[n];
    }
}
