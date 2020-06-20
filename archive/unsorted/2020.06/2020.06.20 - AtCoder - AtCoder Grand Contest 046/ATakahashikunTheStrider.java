package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class ATakahashikunTheStrider {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.readInt();
        //kx = 360t
        //k = 360t/x
        //360 / g * t / (x / g)
        //t = x / g
        int g = GCDs.gcd(x, 360);
        int ans = 360 / g;
        out.println(ans);
    }
}
