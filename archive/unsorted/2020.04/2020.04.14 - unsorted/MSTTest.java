package contest;

import template.graph.ManhattanMST;
import template.io.FastInput;
import template.io.FastOutput;

public class MSTTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] x = new long[n];
        long[] y = new long[n];
        for(int i = 0; i < n; i++){
            x[i] = in.readLong();
            y[i] = in.readLong();
        }
        ManhattanMST mst = new ManhattanMST(n, x, y);
        out.println(mst.getTotal());
    }
}
