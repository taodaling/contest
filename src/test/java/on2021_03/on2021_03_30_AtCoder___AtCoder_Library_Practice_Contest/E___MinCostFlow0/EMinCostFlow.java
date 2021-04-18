package on2021_03.on2021_03_30_AtCoder___AtCoder_Library_Practice_Contest.E___MinCostFlow0;





import template.graph.CapacityScalingMinimumCostFlowBySpfa;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.primitve.generated.graph.LongDijkstraV2MinimumCostFlow;
import template.primitve.generated.graph.LongFlow;

import java.util.List;

public class EMinCostFlow {
    private int n;

    public int idOfLeft(int i) {
        return  i;
    }

    public int idOfRight(int i) {
        return n + i;
    }

    public int idOfSrc() {
        return n * 2;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        int k = in.ri();
        List<LongCostFlowEdge>[] g = Graph.createGraph(idOfDst() + 1);
        LongCostFlowEdge[][] mat = new LongCostFlowEdge[n][n];
        for (int i = 0; i < n; i++) {
            LongFlow.addCostFlowEdge(g, idOfSrc(), idOfLeft(i), k, 0);
            LongFlow.addCostFlowEdge(g, idOfRight(i), idOfDst(), k, 0);
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = LongFlow.addCostFlowEdge(g, idOfLeft(i), idOfRight(j), 1, -in.ri());
            }
        }
        CapacityScalingMinimumCostFlowBySpfa mcmf = new CapacityScalingMinimumCostFlowBySpfa();
       // mcmf.setCallback((f, c) -> c < 0);
        long[] res = mcmf.apply(g, idOfSrc(), idOfDst(), false);
        out.println(-res[1]);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j].flow > 0) {
                    out.append('X');
                }else{
                    out.append('.');
                }
            }
            out.println();
        }
    }
}
