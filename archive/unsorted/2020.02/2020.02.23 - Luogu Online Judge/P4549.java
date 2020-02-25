package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;

public class P4549 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int g = 0;
        for(int i = 0; i < n; i++){
            int x = Math.abs(in.readInt());
            g = GCDs.gcd(g, x);
        }
        out.println(g);
    }
}
