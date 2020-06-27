package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BigFraction;
import template.math.Modular;
import template.problem.BigFactorial;

public class P5282 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        Modular mod = new Modular(p);
        BigFactorial fact = new BigFactorial(mod);
        int ans = fact.solve(n);
        out.println(ans);
    }
}
