package on2021_06.on2021_06_30_Library_Checker.Rectangle_Sum;



import template.datastructure.RectSum;
import template.io.FastInput;
import template.io.FastOutput;

public class RectangleSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        long[] x = new long[n];
        long[] y = new long[n];
        long[] w = new long[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.rl();
            y[i] = in.rl();
            w[i] = in.rl();
        }
        RectSum rs = new RectSum(x, y, w);
        for (int i = 0; i < q; i++) {
            long l = in.rl();
            long d = in.rl();
            long r = in.rl() - 1;
            long u = in.rl() - 1;
            long ans = rs.query(l, r, d, u);
            out.println(ans);
        }
    }
}
