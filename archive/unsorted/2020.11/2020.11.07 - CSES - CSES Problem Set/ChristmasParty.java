package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Factorial;

public class ChristmasParty {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int mod = (int) 1e9 + 7;
        Factorial fact = new Factorial(n, mod);
        long ans = 0;
        for (int i = 0; i <= n; i++) {
            long contrib = (long) fact.fact(n) * fact.invFact(i) % mod;
            if (i % 2 == 1) {
                contrib = -contrib;
            }
            ans += contrib;
        }
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
