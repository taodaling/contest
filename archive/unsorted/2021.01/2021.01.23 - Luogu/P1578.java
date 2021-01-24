package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.problem.MaximumArea;
import template.utils.Debug;
import template.utils.Pair;

public class P1578 {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int L = in.ri();
        int W = in.ri();
        int n = in.ri();
        long[] x = new long[n];
        long[] y = new long[n];
        for(int i = 0; i < n; i++){
            x[i] = in.ri();
            y[i] = in.ri();
        }
        Pair<long[], Long> pair = MaximumArea.rectCover(0, L, 0, W, x, y);
        out.println(pair.b);
        debug.debug("x", pair.a);
    }
}
