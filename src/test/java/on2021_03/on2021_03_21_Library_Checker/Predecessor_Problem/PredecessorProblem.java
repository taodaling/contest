package on2021_03.on2021_03_21_Library_Checker.Predecessor_Problem;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class PredecessorProblem {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        RangeTree rt = new RangeTree(n);
        for (int i = 0; i < n; i++) {
            if (in.rc() == '1') {
                rt.add(i);
            }
            //debug.debug("rt", rt);
        }
        for (int i = 0; i < q; i++) {
            debug.debug("rt", rt);
            int t = in.ri();
            int k = in.ri();
            if (t == 0) {
                rt.add(k);
            } else if (t == 1) {
                rt.remove(k);
            } else if (t == 2) {
                out.println(rt.contains(k) ? 1 : 0);
            } else if (t == 3) {
                out.println(rt.ceil(k));
            } else if (t == 4) {
                out.println(rt.floor(k));
            }
        }
    }
}
