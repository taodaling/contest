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

import java.util.List;

public class P4897GomoryHuTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt() + 1;
        int m = in.readInt();
        List<LongFlowEdge>[] g = LongFlow.createFlow(n);
        for(int i = 0; i < m; i++){
            int u = in.readInt();
            int v = in.readInt();
            int w = in.readInt();
            LongFlow.addEdge(g, u, v, w);
            LongFlow.addEdge(g, v, u, w);
        }
        LongMaximumFlow mf = new LongHLPPBeta(n);
        LongGomoryHuTree tree = new LongGomoryHuTree(g, mf);
        int q = in.readInt();
        for(int i = 0; i < q; i++){
            int u = in.readInt();
            int v = in.readInt();
            long ans = tree.minCut(u, v);
            out.println(ans);
        }
    }
}
