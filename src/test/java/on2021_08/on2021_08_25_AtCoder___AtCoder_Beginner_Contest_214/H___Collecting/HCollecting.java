package on2021_08.on2021_08_25_AtCoder___AtCoder_Beginner_Contest_214.H___Collecting;



import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongDijkstraMinimumCostFlow;
import template.primitve.generated.graph.LongFlow;
import template.utils.Debug;

import java.util.List;

public class HCollecting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        List<DirectedEdge>[] g = Graph.createGraph(n);
        int m = in.ri();
        int k = in.ri();
        for (int i = 0; i < m; i++) {
            Graph.addEdge(g, in.ri() - 1, in.ri() - 1);
        }
        int[] x = in.ri(n);
        DirectedTarjanSCC scc = new DirectedTarjanSCC(n);
        scc.init(g);
        long[] sum = new long[n];
        for (int i = 0; i < n; i++) {
            sum[scc.set[i]] += x[i];
        }
        int inf = (int) 1e8;
        List<LongCostFlowEdge>[] net = Graph.createGraph(dst() + 1);
        for (int i = 0; i < n; i++) {
            IntegerArrayList bs = new IntegerArrayList(g[i].size());
            for (DirectedEdge e : g[i]) {
                int a = scc.set[i];
                int b = scc.set[e.to];
                if (a == b) {
                    continue;
                }
                bs.add(b);
            }
            bs.unique();
            for (int b : bs.toArray()) {
                LongFlow.addCostFlowEdge(net, end(scc.set[i]), start(b), inf, 0);
            }
            if (scc.set[i] == i) {
                LongFlow.addCostFlowEdge(net, start(i), end(i), 1, -sum[i]);
                LongFlow.addCostFlowEdge(net, start(i), end(i), inf, 0);
            }
            if (bs.isEmpty()) {
                LongFlow.addCostFlowEdge(net, end(i), dst(), inf, 0);
            }
        }
        debug.elapse("build and scc");

        LongFlow.addCostFlowEdge(net, src(), start(scc.set[0]), k, 0);
        FlowImpl mcmf = new FlowImpl();
        long[] res = mcmf.apply(net, src(), dst(), k);
        out.println(-res[1]);
        debug.elapse("mcmf");
    }

    Debug debug = new Debug(true);
    int n;

    public int start(int i) {
        return i;
    }

    public int end(int i) {
        return n + i;
    }

    public int src() {
        return n * 2;
    }

    public int dst() {
        return src() + 1;
    }

}

class FlowImpl extends LongDijkstraMinimumCostFlow {
    private long calcDist(int s) {
        if (lastDist[s] == -1) {
            lastDist[s] = INF;
            for (LongCostFlowEdge e : g[s]) {
                if (e.real) {
                    continue;
                }
                lastDist[s] = Math.min(lastDist[s], calcDist(e.to) - e.cost);
            }
        }
        return lastDist[s];
    }

    @Override
    protected void bf(int s) {
        int n = g.length;
        for (int i = 0; i < n; i++) {
            lastDist[i] = -1;
        }
        lastDist[s] = 0;
        for (int i = 0; i < n; i++) {
            calcDist(i);
        }
    }
}