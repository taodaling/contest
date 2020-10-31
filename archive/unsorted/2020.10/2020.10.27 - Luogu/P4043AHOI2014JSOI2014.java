package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;
import template.utils.Debug;

import java.util.List;

public class P4043AHOI2014JSOI2014 {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        List<LongCostFlowEdge>[] g = Graph.createGraph(idOfDst() + 1);
        long inf = (int) 1e8;
        int m = 0;
        for (int i = 0; i < n; i++) {
            int k = in.readInt();
            m += k;
            for (int j = 0; j < k; j++) {
                int to = in.readInt() - 1;
                int time = in.readInt();
                LongFlow.addCostEdge(g, idOf(i), idOf(to), inf, time);
                LongFlow.addCostEdge(g, idOf(i), idOf(to), 1, time - inf);
            }
            LongFlow.addCostEdge(g, idOf(i), idOfDst(), inf, 0);
        }
        LongFlow.addCostEdge(g, idOfSrc(), idOf(0), inf, 0);

        LongAugmentMinimumCostFlow mcf = new LongSpfaMinimumCostFlow(g.length);
        mcf.setCallback(new LongAugmentCallback() {
            @Override
            public boolean callback(long flow, long pathCost) {
                return pathCost < 0;
            }
        });
        long[] ans = mcf.apply(g, idOfSrc(), idOfDst(), (long) 1e18);
        //debug.debug("g", () -> LongFlow.costFlowToString(g));
        long cost = ans[1] + m * inf;
        out.println(cost);
    }

    int n;


    int idOf(int i) {
        return i;
    }

    int idOfSrc() {
        return n;
    }

    int idOfDst() {
        return idOfSrc() + 1;
    }
}

