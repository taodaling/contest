package contest;

import template.graph.LongMinCostMaxFlow;
import template.graph.MinCostMaxFlow;
import template.io.FastInput;
import template.io.FastOutput;

public class UOJ387 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        int t = in.readInt();

        LongMinCostMaxFlow mcmf = new LongMinCostMaxFlow(n + 1, s, t);
        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            int c = in.readInt();
            int cost = in.readInt();
            mcmf.getChannel(a, b, cost).modify(c, 0);
        }

        long[] ans = mcmf.send((long) 1e18);
        out.append(ans[0]).append(' ').append(ans[1]);
    }


}
