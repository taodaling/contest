package contest;

import template.DynamicConnectionChecker;
import template.FastInput;
import template.FastOutput;

public class LUOGU5247 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int last = 0;
        DynamicConnectionChecker dcc = new DynamicConnectionChecker(n, m);
        for (int i = 0; i < m; i++) {
            int op = in.readInt();
            int x = in.readInt() ^ last;
            int y = in.readInt() ^ last;
            if (op == 0) {
                dcc.addEdge(x - 1, y - 1);
            }
            if (op == 1) {
                dcc.deleteEdge(x - 1, y - 1);
            }
            if (op == 2) {
                boolean c = dcc.check(x - 1, y - 1);
                out.println(c ? 'Y' : 'N');
                last = c ? x : y;
            }
        }
    }
}
