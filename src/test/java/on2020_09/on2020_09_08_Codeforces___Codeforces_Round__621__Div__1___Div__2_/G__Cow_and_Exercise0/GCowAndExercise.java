package on2020_09.on2020_09_08_Codeforces___Codeforces_Round__621__Div__1___Div__2_.G__Cow_and_Exercise0;





import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongDijkstraV2MinimumCostFlow;
import template.problem.MakeDistanceFarthest;

public class GCowAndExercise {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        MakeDistanceFarthest mdf = new MakeDistanceFarthest(n);
        for (int i = 0; i < m; i++) {
            mdf.addEdge(in.readInt() - 1, in.readInt() - 1, in.readInt(), 1);
        }
        mdf.solve(new LongDijkstraV2MinimumCostFlow(n), 0, n - 1, (int)1e5, (long)1e18, (int)1e9);
        int q = in.readInt();
        for(int i = 0; i < q; i++){
            int x = in.readInt();
            double ans = mdf.queryByExpense(x);
            out.println(ans);
        }
    }
}

