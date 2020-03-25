package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;
import template.primitve.generated.graph.IntegerISAP;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.List;

public class Task {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int src = n;
        int dst = n + 1;
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

        IntegerISAP isap = new IntegerISAP(n + 2);
        isap.apply(g, src, dst, (int) 1e9);
        boolean valid = true;
        for (IntegerFlowEdge e : g[src]) {
            if (e.rev.flow > 0) {
                valid = false;
                debug.debug("e", e);
            }
        }
        for (IntegerFlowEdge e : g[dst]) {
            if (e.flow > 0) {
                valid = false;
                debug.debug("e", e);
            }
        }

        if (!valid) {
            out.println("NO");
            return;
        }
        out.println("YES");
        for (int i = 0; i < m; i++) {
            out.append(edges[i].flow + fix[i]).println();
        }
    }
}
