package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.Power;

public class CountingNecklaces {
    int mod = (int) 1e9 + 7;
    Power power = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            int g = GCDs.gcd(i, n);
            sum += power.pow(m, g);
        }
        sum = sum % mod * power.inverse(n) % mod;
        out.println(sum);
    }
}
