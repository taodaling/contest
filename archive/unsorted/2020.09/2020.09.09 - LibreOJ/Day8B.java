package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongDijkstraV2MinimumCostFlow;
import template.problem.MakeDistanceFarthest;

public class Day8B {
    int n;

    int idOfIn(int i) {
        return i;
    }

    int idOfOut(int i) {
        return n + i;
    }

    int idOfSrc() {
        return 2 * n;
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        int w = in.readInt();
        int[] time = new int[n];
        int[] cost = new int[n];

        for (int i = 0; i < n; i++) {
            time[i] = in.readInt();
        }
        for (int i = 0; i < n; i++) {
            cost[i] = in.readInt();
        }
        MakeDistanceFarthest mdf = new MakeDistanceFarthest(idOfDst() + 1);
        for (int i = 0; i < n; i++) {
            mdf.addLimitedEdge(idOfSrc(), idOfIn(i), 0, 0, 0);
            mdf.addLimitedEdge(idOfOut(i), idOfDst(), 0, 0, 0);
            mdf.addLimitedEdge(idOfIn(i), idOfOut(i), -time[i], cost[i], time[i]);
        }
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            mdf.addLimitedEdge(idOfOut(v), idOfIn(u), 0, 0, 0);
        }

        mdf.solve(new LongDijkstraV2MinimumCostFlow(idOfDst() + 1), idOfSrc(), idOfDst(),
                w, (long) 1e18, (int) 1e9);


        long ans = (long) Math.floor(mdf.queryByExpense(w));
        out.println(-ans);
    }
}

