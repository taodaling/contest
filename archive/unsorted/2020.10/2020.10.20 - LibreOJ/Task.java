package contest;

import template.io.FastInput;
import template.primitve.generated.graph.LongDinic;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;

import java.io.PrintWriter;
import java.util.List;

public class Task {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt() - 1;
        int t = in.readInt() - 1;
        List<LongFlowEdge>[] g = LongFlow.createFlow(n);
        for(int i = 0; i < m; i++){
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            int c = in.readInt();
            LongFlow.addEdge(g, u, v, c);
        }

        LongDinic dinic = new LongDinic();
        long ans = dinic.apply(g, s, t, (long)1e18);
        out.println(ans);
    }
}
