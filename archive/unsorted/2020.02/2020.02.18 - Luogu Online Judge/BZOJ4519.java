package contest;

import template.graph.graph.LongDinic;
import template.graph.graph.LongFlow;
import template.graph.graph.LongFlowEdge;
import template.graph.graph.LongGomoryHuTree;
import template.graph.graph.LongHLPPBeta;
import template.graph.graph.LongISAP;
import template.graph.graph.LongMaximumFlow;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerHashSet;

import java.util.List;

public class BZOJ4519 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        List<LongFlowEdge>[] g = LongFlow.createFlow(n);
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            int w = in.readInt();
            LongFlow.addEdge(g, u, v, w);
            LongFlow.addEdge(g, v, u, w);
        }
        LongMaximumFlow mf = new LongHLPPBeta(n);
        LongGomoryHuTree tree = new LongGomoryHuTree(g, mf);

        IntegerHashSet set = new IntegerHashSet(n * n, false);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                set.add((int) tree.minCut(i, j));
            }
        }

        out.println(set.size());
    }
}
