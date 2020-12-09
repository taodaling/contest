package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LCMs;

public class ARedundantRedundancy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = 30;
        long lcm = 1;
        for(int i = 1; i <= n; i++){
            lcm = LCMs.lcm(lcm, i);
        }
        out.println(lcm + 1);
    }
}
