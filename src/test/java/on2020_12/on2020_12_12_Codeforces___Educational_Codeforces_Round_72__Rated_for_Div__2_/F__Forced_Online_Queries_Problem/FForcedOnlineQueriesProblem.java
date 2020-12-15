package on2020_12.on2020_12_12_Codeforces___Educational_Codeforces_Round_72__Rated_for_Div__2_.F__Forced_Online_Queries_Problem;



import template.graph.DynamicConnectivityChecker;
import template.io.FastInput;
import template.io.FastOutput;

public class FForcedOnlineQueriesProblem {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        DynamicConnectivityChecker dcc = new DynamicConnectivityChecker(n + 1, m);
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
