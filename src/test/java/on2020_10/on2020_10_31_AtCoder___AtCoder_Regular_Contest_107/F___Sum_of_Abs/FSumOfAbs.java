package on2020_10.on2020_10_31_AtCoder___AtCoder_Regular_Contest_107.F___Sum_of_Abs;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongDinic;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;
import template.primitve.generated.graph.LongMaximumFlow;

import java.util.List;

public class FSumOfAbs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);
        long inf = (long) 1e18;
        List<LongFlowEdge>[] g = Graph.createGraph(dst() + 1);
        for (int i = 0; i < n; i++) {
            if (b[i] >= 0) {
                LongFlow.addFlowEdge(g, src(), inner(i), 2 * Math.abs(b[i]));
            } else {
                LongFlow.addFlowEdge(g, outter(i), dst(), 2 * Math.abs(b[i]));
            }
            LongFlow.addFlowEdge(g, inner(i), outter(i), a[i] + Math.abs(b[i]));
            LongFlow.addFlowEdge(g, outter(i), inner(i), a[i] + Math.abs(b[i]));
        }
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            LongFlow.addFlowEdge(g, outter(u), inner(v), inf);
            LongFlow.addFlowEdge(g, outter(v), inner(u), inf);
        }

        LongMaximumFlow lmf = new LongDinic();
        long minCut = lmf.apply(g, src(), dst(), inf);
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.abs(b[i]);
        }

        long ans = sum - minCut;
        out.println(ans);
    }

    public int sign(int x) {
        return x >= 0 ? 1 : -1;
    }

    int n;

    int inner(int i) {
        return i;
    }

    int outter(int i) {
        return i + n;
    }

    int src() {
        return n + n;
    }

    int dst() {
        return src() + 1;
    }
}
