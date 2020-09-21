package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.EulerSieve;

public class BSherlockAndHisGirlfriend {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        if (n + 1 < 4) {
            out.println(1);
            for (int i = 0; i < n; i++) {
                out.append(1).append(' ');
            }
            return;
        }
        out.println(2);
        EulerSieve sieve = new EulerSieve(n + 1);
        for (int i = 2; i <= n + 1; i++) {
            out.append(sieve.isPrime(i) ? 1 : 2).append(' ');
        }
    }
}
