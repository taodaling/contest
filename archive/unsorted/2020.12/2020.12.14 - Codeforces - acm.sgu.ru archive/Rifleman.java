package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.MultiplicativeFunctionSieve;

public class Rifleman {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        MultiplicativeFunctionSieve sieve = new MultiplicativeFunctionSieve(n);
        int[] mobius = sieve.getMobius();
        long ans = 0;
        for (int i = 1; i <= n - 1; i++) {
            ans += (long) mobius[i] * ((n - 1) / i) * ((m - 1) / i);
        }
        if(m > 1){
            ans++;
        }
        if(n > 1){
            ans++;
        }
        out.println(ans);
    }
}
