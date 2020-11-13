package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;

public class CountingDivisors {
    MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve((int) 1e6,
            false, false, true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        out.println(sieve.factors[n]);
    }
}
