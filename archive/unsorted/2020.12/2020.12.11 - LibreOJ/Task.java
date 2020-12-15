package contest;

import template.graph.DynamicConnectiveChecker;
import template.io.FastInput;
import template.io.FastOutput;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        DynamicConnectiveChecker dcc = new DynamicConnectiveChecker(n, m);
        int lastans = 0;
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int a = (lastans ^ in.ri()) - 1;
            int b = (lastans ^ in.ri()) - 1;
            if (t == 0) {
                dcc.addEdge(a, b);
            } else if (t == 1) {
                dcc.deleteEdge(a, b);
            } else {
                boolean ans = dcc.check(a, b);
                lastans = ans ? a : b;
                lastans++;
                out.println(ans ? 'Y' : 'N');
            }
        }
    }
}
