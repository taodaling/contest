package contest;

import template.datastructure.VanEmdeBoasTreeBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class PredecessorProblem {
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        VanEmdeBoasTreeBeta vebt = VanEmdeBoasTreeBeta.newInstance(n);
        debug.elapse("build tree");
        for (int i = 0; i < n; i++) {
            if (in.rc() == '1') {
                vebt.insert(i);
            }
        }
        debug.elapse("insert");
        for (int i = 0; i < q; i++) {
            debug.debug("vebt", vebt);
            int c = in.ri();
            int k = in.ri();
            if (c == 0) {
                if (vebt.member(k)) {
                    continue;
                }
                vebt.insert(k);
            } else if (c == 1) {
                if (vebt.member(k)) {
                    vebt.delete(k);
                }
            } else if (c == 2) {
                if (!vebt.member(k)) {
                    out.println(0);
                } else {
                    out.println(1);
                }
            } else if (c == 3) {
                if (vebt.member(k)) {
                    out.println(k);
                } else {
                    int ceil = vebt.successor(Math.max(0, k - 1));
                    out.println(ceil);
                }
            } else if (c == 4) {
                if (vebt.member(k)) {
                    out.println(k);
                } else {
                    int floor = vebt.predecessor(Math.min(k + 1, n - 1));
                    out.println(floor);
                }
            }
        }

        debug.elapse("query");
        debug.summary();
        //debug.debug("out", out);
        //debug.debug("mem", VanEmdeBoasTree.memory_used);
        return;
    }
}
