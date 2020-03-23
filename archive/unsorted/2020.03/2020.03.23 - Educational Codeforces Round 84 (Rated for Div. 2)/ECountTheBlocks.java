package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;
import template.math.Power;

public class ECountTheBlocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        Modular mod = new Modular(998244353);
        Power power = new Power(mod);
        int n = in.readInt();
        for (int i = 1; i < n; i++) {
            int time = n - i - 1;
            int cnt1 = mod.mul(20, mod.mul(9, power.pow(10, n - i - 1)));
            int cnt2 = time == 0 ? 0 : mod.mul(time, mod.mul(810, power.pow(10, n - i - 2)));
            out.println(mod.plus(cnt1, cnt2));
        }
        out.println(10);
    }


}
