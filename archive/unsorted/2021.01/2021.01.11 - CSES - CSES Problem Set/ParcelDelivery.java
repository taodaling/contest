package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongFlowEdge;

import java.util.List;

public class ParcelDelivery {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        List<LongCostFlowEdge>[] g = Graph.createGraph(n);
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int cap = in.ri();
            int cost = in.ri();
            LongFlow.addCostFlowEdge(g, a, b, cap, cost);
        }
        LongDijkstraMinimumCostFlow mcf = new LongDijkstraMinimumCostFlow();
        long[] res = mcf.apply(g, 0, n - 1,k);
        if(res[0] < k){
            out.println(-1);
        }else{
            out.println(res[1]);
        }
    }
}
