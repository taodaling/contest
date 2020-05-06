package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Nim;

public class SwitchLights {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum ^= Nim.product(in.readInt(), in.readInt());
        }
        if (sum == 0) {
            out.println("Don't waste your time.");
        } else {
            out.println("Have a try, lxhgww.");
        }
    }
}
