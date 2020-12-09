package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class ManhattanMST {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] x = new long[n];
        long[] y = new long[n];
        for(int i = 0; i < n; i++){
            x[i] = in.ri();
            y[i] = in.ri();
        }
        debug.debug("n", n);
        debug.debugArray("x", x);
        debug.debugArray("y", y);

        template.graph.ManhattanMST mst = new template.graph.ManhattanMST(n, x, y);
        debug.debugMatrix("mst", mst.getMst());
        out.println(mst.getTotal());
        for(int i = 0; i < n - 1; i++){
            int a = mst.getMst()[0][i];
            int b = mst.getMst()[1][i];
            out.append(a).append(' ').append(b).println();
        }
    }
}
