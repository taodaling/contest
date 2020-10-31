package on2020_10.on2020_10_27_Luogu.P4043__AHOI2014_JSOI2014_____1;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;
import template.utils.Debug;

import java.util.List;

public class P4043AHOI2014JSOI2014 {
    int n;

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        List<LongLRCostFlowEdge>[] g = Graph.createGraph(idOfDst() + 1);
        long inf = (int) 1e8;
        int m = 0;
        for (int i = 0; i < n; i++) {
            int k = in.readInt();
            m += k;
            for (int j = 0; j < k; j++) {
                int to = in.readInt() - 1;
                int time = in.readInt();
                LongFlow.addLRCostFlowEdge(g, idOf(i), idOf(to), inf, time, 1);
            }
            LongFlow.addLRCostFlowEdge(g, idOf(i), idOfDst(), inf, 0, 0);
        }
        LongFlow.addLRCostFlowEdge(g, idOfSrc(), idOf(0), inf, 0, 0);

        LongAugmentMinimumCostFlow mcf = new LongSpfaMinimumCostFlow();
        if (!LongFlow.feasibleMinCostFlow(g, idOfSrc(), idOfDst(), mcf)) {
            throw new RuntimeException();
        }
        long ans = 0;
        for (List<LongLRCostFlowEdge> adj : g) {
            for (LongLRCostFlowEdge e : adj) {
                if (e.real) {
                    ans += e.cost * (e.flow + e.low());
                }
            }
        }
        debug.debug("g", () -> LongFlow.costFlowToString(g));
        out.println(ans);
    }

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
