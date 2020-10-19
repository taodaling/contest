package contest;

import template.io.FastInput;
import template.utils.SequenceUtils;

import java.io.PrintWriter;

public class P6577 {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        long[][] mat = new long[n][n];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(mat, -inf);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            int w = in.readInt();
            mat[u][v] = w;
        }
        LongKMAlgo km = new LongKMAlgo(mat);
        long ans = km.solve();
        out.println(ans);
        for(int i = 0; i < n; i++){
            out.print(km.getRightPartner(i) + 1);
            out.append(' ');
        }
    }
}
