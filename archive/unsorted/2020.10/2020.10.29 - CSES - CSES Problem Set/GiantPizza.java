package contest;

import template.graph.TwoSatBeta;
import template.io.FastInput;
import template.io.FastOutput;

public class GiantPizza {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        TwoSatBeta ts = new TwoSatBeta(m, n * 2);
        for (int i = 0; i < n; i++) {
            char c1 = in.readChar();
            int id1 = in.readInt() - 1;
            char c2 = in.readChar();
            int id2 = in.readInt() - 1;
            if (c1 == '+') {
                id1 = ts.elementId(id1);
            } else {
                id1 = ts.negateElementId(id1);
            }
            if (c2 == '+') {
                id2 = ts.elementId(id2);
            } else {
                id2 = ts.negateElementId(id2);
            }
            ts.or(id1, id2);
        }
        if (!ts.solve(true)) {
            out.println("IMPOSSIBLE");
            return;
        }
        for (int i = 0; i < m; i++) {
            out.append(ts.valueOf(i) ? '+' : '-').append(' ');
        }
    }
}
