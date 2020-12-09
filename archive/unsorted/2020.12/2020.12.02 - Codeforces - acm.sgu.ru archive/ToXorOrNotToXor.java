package contest;

import template.datastructure.LinearBasis;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class ToXorOrNotToXor {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        LinearBasis lb = new LinearBasis();
        for(int i = 0; i < n; i++){
            int index = lb.add(in.rl());
            debug.debug("index", index);
        }
        long ans = lb.theMaximumNumberXor(0);
        out.println(ans);
    }
}
