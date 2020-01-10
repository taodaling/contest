package contest;

import template.graph.LongMinCostMaxFlow;
import template.graph.MinCostMaxFlow;
import template.io.FastInput;
import template.io.FastOutput;

public class LOJ102 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        LongMinCostMaxFlow mcmf = new LongMinCostMaxFlow(n + 1, 1, n);
        for (int i = 0; i < m; i++) {
            int s = in.readInt();
            int t = in.readInt();
            int c = in.readInt();
            int w = in.readInt();
            mcmf.getChannel(s, t, w).modify(c, 0);
        }
        long[] ans = mcmf.send((long) 1e18);
        out.append(ans[0]).append(' ').append(ans[1]).println();
    }
}
