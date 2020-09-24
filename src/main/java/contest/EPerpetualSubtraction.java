package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.math.Modular;

public class EPerpetualSubtraction {
    int mod = 998244353;
    Modular modular = new Modular(mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long m = in.readLong();

        int[] p = new int[n + 1];
        in.populate(p);

        Factorial factorial = new Factorial(n + 1, mod);
    }
}
