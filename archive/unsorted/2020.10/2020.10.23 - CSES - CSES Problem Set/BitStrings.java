package contest;

import template.io.FastInput;
import template.math.Power;

import java.io.PrintWriter;

public class BitStrings {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = (int)1e9 + 7;
        Power power = new Power(mod);
        int ans = power.pow(2, in.readInt());
        out.println(ans);
    }
}
