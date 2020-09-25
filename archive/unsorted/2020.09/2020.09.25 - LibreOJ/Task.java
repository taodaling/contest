package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.ModGravityLagrangeInterpolation;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        ModGravityLagrangeInterpolation interpolation = new ModGravityLagrangeInterpolation(998244353, n);
        for(int i = 0; i < n; i++){
            int t = in.readInt();
            if(t == 1){
                interpolation.addPoint(in.readInt(), in.readInt());
            }else{
                int ans = interpolation.getYByInterpolation(in.readInt());
                out.println(ans);
            }
        }
    }
}
