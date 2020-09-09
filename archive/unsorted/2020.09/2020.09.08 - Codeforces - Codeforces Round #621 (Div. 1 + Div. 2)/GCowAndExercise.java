package contest;

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
        mdf.solve(new LongDijkstraV2MinimumCostFlow(n), 0, n - 1);
        int q = in.readInt();
        for(int i = 0; i < q; i++){
            int x = in.readInt();
            double ans = mdf.query(x);
            out.println(ans);
        }
    }
}

