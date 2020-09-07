package on2020_08.on2020_08_31_Codeforces___AIM_Tech_Round_3__Div__1_.D__Incorrect_Flow;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongMinimumCostFlow;
import template.utils.Debug;

import java.util.List;

public class DIncorrectFlow {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int src = 0;
        int dst = n - 1;
        int superSrc = n;
        int superDst = n + 1;

        List<LongCostFlowEdge>[] g = LongFlow.createCostFlow(superDst + 1);
        long[] delta = new long[n];
        LongCostFlowEdge[] normalForward = new LongCostFlowEdge[m];
        LongCostFlowEdge[] normalBack = new LongCostFlowEdge[m];
        LongCostFlowEdge[] exceedForward = new LongCostFlowEdge[m];
        LongCostFlowEdge[] exceedBack = new LongCostFlowEdge[m];

        long inf = (long) 1e18;
        long cur = 0;
        LongFlow.addCostEdge(g, dst, src, inf, 0);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int c = in.readInt();
            int f = in.readInt();

            normalForward[i] = LongFlow.addCostEdge(g, a, b, Math.max(0, c - f), 1);
            normalBack[i] = LongFlow.addCostEdge(g, b, a, Math.min(f, c), 1);
            exceedForward[i] = LongFlow.addCostEdge(g, a, b, inf, 2);
            exceedBack[i] = LongFlow.addCostEdge(g, b, a, Math.max(0, f - c), 0);

            if(f > c){
                cur += f - c;
            }

            delta[a] -= f;
            delta[b] += f;
        }

        long sum = 0;
        for (int i = 0; i < n; i++) {
            if (delta[i] > 0) {
                LongFlow.addCostEdge(g, superSrc, i, delta[i], 0);
                sum += delta[i];
            }
            if (delta[i] < 0) {
                LongFlow.addCostEdge(g, i, superDst, -delta[i], 0);
            }
        }

        LongMinimumCostFlow mcf = new LongDijkstraMinimumCostFlow(g.length);
        long[] cf = mcf.apply(g, superSrc, superDst, inf);
        //debug.debug("g", LongFlow.costFlowToString(g));
        if(cf[0] != sum){
            throw new RuntimeException();
        }

        long ans = cf[1] + cur;
        out.println(ans);
    }
}
