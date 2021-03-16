package contest;

import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

public class Task2 {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        in.rs();
        in.rs();
        int n = in.ri();
        int m = in.ri();
        TwoSatBeta ts = new TwoSatBeta(n, m * 2);
        for (int i = 0; i < m; i++) {
            int a = id(ts, in.ri());
            int b = id(ts, in.ri());
            ts.or(a, b);
            in.ri();
        }

        boolean[] sol = ts.solve();
        if (sol == null) {
            out.println("s UNSATISFIABLE");
            return;
        }
        debug.debug("ts", ts);
        out.println("s SATISFIABLE");
        out.append("v ");
        for (int i = 0; i < n; i++) {
            int sign = sol[ts.elementId(i)] ? 1 : -1;
            out.append(sign * (i + 1)).append(' ');
        }
        out.append(0);
    }

    public int id(TwoSatBeta ts, int i) {
        return i > 0 ? ts.elementId(i - 1) : ts.negateElementId(-i - 1);
    }

}
