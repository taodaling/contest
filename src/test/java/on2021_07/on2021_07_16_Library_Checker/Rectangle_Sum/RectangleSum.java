package on2021_07.on2021_07_16_Library_Checker.Rectangle_Sum;



import template.io.FastInput;
import template.io.FastOutput;

public class RectangleSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        template.datastructure.PointAddRectangleSum st = new template.datastructure.PointAddRectangleSum(n + q, q);
        for (int i = 0; i < n; i++) {
            st.addPoint(in.ri(), in.ri(), in.ri());
        }
        for (int i = 0; i < q; i++) {
            int l = in.ri();
            int d = in.ri();
            int r = in.ri() - 1;
            int u = in.ri() - 1;
            st.query(l, r, d, u);
        }
        long[] ans = st.solve();
        for (long x : ans) {
            out.println(x);
        }
    }
}
