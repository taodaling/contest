package on2021_07.on2021_07_05_Library_Checker.Point_Add_Rectangle_Sum;



import template.io.FastInput;
import template.io.FastOutput;
import template.problem.RectPointSumProblem;

import java.util.ArrayList;
import java.util.List;

public class PointAddRectangleSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<RectPointSumProblem.Point3D> pts = new ArrayList<>(n + m);
        for (int i = 0; i < n; i++) {
            pts.add(new RectPointSumProblem.Point3D(in.readInt(), in.readInt(), -1, in.readInt()));
        }
        List<RectPointSumProblem.Query3D> qs = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                int l = in.readInt();
                int d = in.readInt();
                int r = in.readInt() - 1;
                int u = in.readInt() - 1;
                qs.add(new RectPointSumProblem.Query3D(l, r, d, u, -1, i));
            } else {
                pts.add(new RectPointSumProblem.Point3D(in.readInt(), in.readInt(), i, in.readInt()));
            }
        }
        long[] ans = RectPointSumProblem.solve(pts.toArray(new RectPointSumProblem.Point3D[0]), qs.toArray(new RectPointSumProblem.Query3D[0]));
        for (long x : ans) {
            out.println(x);
        }
    }
}
