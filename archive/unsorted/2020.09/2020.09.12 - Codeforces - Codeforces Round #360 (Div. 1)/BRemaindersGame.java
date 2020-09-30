package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.math.LCMs;

public class BRemaindersGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        long gg = 1;
        for (int i = 0; i < n; i++) {
            int c = in.readInt();
            int g = GCDs.gcd(c, k);
            gg = LCMs.lcm(g, gg);
        }
        out.println(k == gg ? "Yes" : "No");
    }
}
