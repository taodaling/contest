package contest;

import template.graph.graph.LongFlow;
import template.graph.graph.LongFlowEdge;
import template.graph.graph.LongHLPPBeta;
import template.graph.graph.LongMaximumFlow;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        int t = in.readInt();

        List<LongFlowEdge>[] net = LongFlow.createFlow(n + 1);
        for (int i = 1; i <= m; i++) {
            int u = in.readInt();
            int v = in.readInt();
            int c = in.readInt();
            LongFlow.addEdge(net, u, v, c);
        }

        LongMaximumFlow mf = new LongHLPP(n + 1);
        long ans = mf.apply(net, s, t);
        out.println(ans);
    }
}
