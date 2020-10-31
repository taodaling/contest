package on2020_10.on2020_10_26_Library_Checker.Rectangle_Sum0;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectPointSumProblem;

public class RectangleSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        RectPointSumProblem.Point2D[] pts = new RectPointSumProblem.Point2D[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new RectPointSumProblem.Point2D(in.readInt(), in.readInt(), in.readInt());
        }
        RectPointSumProblem.Query2D[] qs = new RectPointSumProblem.Query2D[m];
        for (int i = 0; i < m; i++) {
            int l = in.readInt();
            int d = in.readInt();
            int r = in.readInt() - 1;
            int u = in.readInt() - 1;
            qs[i] = new RectPointSumProblem.Query2D(l, r, d, u);
        }
        long[] ans = RectPointSumProblem.solve(pts, qs);
        for (long x : ans) {
            out.println(x);
        }
    }
}
