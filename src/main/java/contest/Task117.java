package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;
import template.primitve.generated.graph.IntegerISAP;

import java.util.List;

public class Task117 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int src = n;
        int dst = n + 1;
        int a = in.readInt() - 1;
        int b = in.readInt() - 1;
        List<IntegerFlowEdge>[] g = IntegerFlow.createFlow(n + 2);
        IntegerFlowEdge[] edges = new IntegerFlowEdge[m];
        int[] fix = new int[m];
        for (int i = 0; i < m; i++) {
            int s = in.readInt() - 1;
            int t = in.readInt() - 1;
            int l = in.readInt();
            int r = in.readInt();

            fix[i] = l;
            edges[i] = IntegerFlow.addEdge(g, s, t, r - l);
            IntegerFlow.addEdge(g, s, dst, l);
            IntegerFlow.addEdge(g, src, t, l);
        }

        IntegerFlowEdge extra = IntegerFlow.addEdge(g, b, a, (int) 1e9);

        IntegerDinic isap = new IntegerDinic(n + 2);
        isap.apply(g, src, dst, (int) 1e9);
        boolean valid = true;
        for (IntegerFlowEdge e : g[src]) {
            if (e.rev.flow > 0) {
                valid = false;
            }
        }
        for (IntegerFlowEdge e : g[dst]) {
            if (e.flow > 0) {
                valid = false;
            }
        }

        if (!valid) {
            out.println("please go home to sleep");
            return;
        }

        int flow = extra.flow;
        extra.flow = extra.rev.flow = 0;
        flow -= isap.apply(g, b, a, (int) 1e9);
        out.println(flow);
    }
}
