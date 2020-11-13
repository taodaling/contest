package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

public class ExponentiationII {
    int mod = (int) 1e9 + 7;
    int phi = mod - 1;
    Power pow = new Power(mod);
    Power phiPow = new Power(phi);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int c = in.readInt();
        int ans = pow.pow(a, phiPow.pow(b, c));
        out.println(ans);
    }
}
