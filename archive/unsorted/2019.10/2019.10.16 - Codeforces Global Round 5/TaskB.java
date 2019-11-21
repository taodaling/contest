package contest;

import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);

        int ans = power.pow(2, m);
        ans = mod.subtract(ans, 1);
        ans = power.pow(ans, n);

        out.println(ans);
    }
}
