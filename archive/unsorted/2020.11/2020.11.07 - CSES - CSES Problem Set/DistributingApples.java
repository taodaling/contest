package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;

public class DistributingApples {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int mod = (int) 1e9 + 7;
        Combination comb = new Combination(n + m, mod);
        out.println(comb.combination(n + m - 1, n - 1));
    }
}
