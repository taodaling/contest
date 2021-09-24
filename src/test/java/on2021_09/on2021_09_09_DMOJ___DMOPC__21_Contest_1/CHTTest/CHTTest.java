package on2021_09.on2021_09_09_DMOJ___DMOPC__21_Contest_1.CHTTest;



import template.datastructure.ConvexHullTrickExt;
import template.io.FastInput;
import template.io.FastOutput;

public class CHTTest {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        ConvexHullTrickExt cht = new ConvexHullTrickExt();
        for (int i = 0; i < n; i++) {
            cht.add(in.ri(), in.ri());
        }
        for (int i = 0; i < m; i++) {
            double ans = cht.query(in.ri(), in.ri());
            out.println(ans);
        }
    }
}
