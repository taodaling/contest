package on2021_01.on2021_01_23_Luogu.P2740__USACO4_2_____Drainage_Ditches;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;
import template.primitve.generated.graph.IntegerISAP;
import template.primitve.generated.graph.IntegerMaximumFlow;

import java.util.List;

public class P2740USACO42DrainageDitches {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<IntegerFlowEdge>[] g = Graph.createGraph(m);
        for(int i = 0; i < n; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int c = in.ri();
            IntegerFlow.addFlowEdge(g, a, b, c);
        }
        IntegerMaximumFlow mf = new IntegerISAP();
        int ans = mf.apply(g, 0, m - 1, (int)2e9);
        out.println(ans);
    }
}
