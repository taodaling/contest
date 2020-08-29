package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongDinic;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;
import template.primitve.generated.graph.LongLRFlowEdge;

import java.util.List;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int src = in.readInt() - 1;
        int sink = in.readInt() - 1;
        List<LongLRFlowEdge>[] g = LongFlow.createLRFlow(n);
        LongLRFlowEdge[] edges = new LongLRFlowEdge[m];
        for (int i = 0; i < m; i++) {
            int s = in.readInt() - 1;
            int t = in.readInt() - 1;
            long low = in.readLong();
            long high = in.readLong();
            edges[i] = LongFlow.addLREdge(g, s, t, high, low);
        }
        LongDinic dinic = new LongDinic();
        boolean feasible = LongFlow.feasibleFlow(g, src, sink, dinic);
        if (!feasible) {
            out.println("please go home to sleep");
            return;
        }
        dinic.apply((List[]) g, sink, src, Long.MAX_VALUE / 4);
        long ans = 0;
        for (LongLRFlowEdge e : g[src]) {
            if (!e.real) {
                continue;
            }
            ans += e.flow + e.low;
        }
        out.println(ans);
    }
}
