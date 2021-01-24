package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.Power;

public class CountingGrids {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.ri();
        if (n == 1) {
            out.println(2);
            return;
        }
        long sum = 0;
        long m = n * n;
        for (int i = 0; i < 4; i++) {
            long cc = m / 4;
            long remain = m % 4;
            int g = GCDs.gcd(4, i);
            long loop = cc * g + remain;
            sum += pow.pow(2, loop);
        }
        sum = sum % mod * pow.inverse(4) % mod;
        out.println(sum);
    }
}
