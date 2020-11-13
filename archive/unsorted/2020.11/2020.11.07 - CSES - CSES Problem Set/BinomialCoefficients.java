package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;

public class BinomialCoefficients {
    int mod = (int)1e9 + 7;
    Combination comb = new Combination((int)1e6, mod);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        out.println(comb.combination(a, b));
    }
}
