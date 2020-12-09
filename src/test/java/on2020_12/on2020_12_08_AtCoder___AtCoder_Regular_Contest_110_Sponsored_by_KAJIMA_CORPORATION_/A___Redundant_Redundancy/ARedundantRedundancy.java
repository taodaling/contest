package on2020_12.on2020_12_08_AtCoder___AtCoder_Regular_Contest_110_Sponsored_by_KAJIMA_CORPORATION_.A___Redundant_Redundancy;



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
