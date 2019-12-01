package on2019_11.on2019_11_30_.UOJ3870;




import graphs.flows.MinCostFlowDijkstra;
import template.io.FastInput;
import template.io.FastOutput;

public class UOJ387 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        int t = in.readInt();

        MinCostFlowDijkstra mcmf = new MinCostFlowDijkstra(n + 1);
        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            int c = in.readInt();
            int cost = in.readInt();
            mcmf.addEdge(a, b, c, cost);
        }

        long[] ans = mcmf.minCostFlow(s, t, (long) 1e18);
        out.append(ans[0]).append(' ').append(ans[1]);
    }


}
