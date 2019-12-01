package on2019_11.on2019_11_30_Educational_Codeforces_Round_72__Rated_for_Div__2_.F___Forced_Online_Queries_Problem;



import template.graph.DynamicConnectiveChecker;
import template.io.FastInput;
import template.io.FastOutput;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        DynamicConnectiveChecker dcc = new DynamicConnectiveChecker(n + 1, m);
        int last = 0;
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            int x = (in.readInt() + last - 1) % n + 1;
            int y = (in.readInt() + last - 1) % n + 1;
            if (t == 1) {
                if (dcc.containEdge(x, y)) {
                    dcc.deleteEdge(x, y);
                } else {
                    dcc.addEdge(x, y);
                }
            } else {
                boolean ans = dcc.check(x, y);
                if (ans) {
                    out.append('1');
                    last = 1;
                } else {
                    out.append('0');
                    last = 0;
                }
            }
        }
    }
}
