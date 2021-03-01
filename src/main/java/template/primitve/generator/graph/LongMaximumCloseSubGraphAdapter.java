package template.primitve.generated.graph;

import template.graph.DirectedEdge;
import template.graph.Graph;

import java.util.List;

public class LongMaximumCloseSubGraphAdapter implements LongMaximumCloseSubGraph {
    private final LongMaximumFlow mf;
    private static final long INF = Long.MAX_VALUE / 4;

    public LongMaximumCloseSubGraphAdapter(LongMaximumFlow mf) {
        this.mf = mf;
    }

    @Override
    public long maximumCloseSubGraph(List<LongWeightDirectedEdge>[] g, long[] weights, boolean[] picked) {
        int n = weights.length;
        int s = n;
        int t = n + 1;
        List<LongFlowEdge>[] net = Graph.createGraph(n + 2);
        long sumOfPositive = 0;
        for (int i = 0; i < n; i++) {
            if (weights[i] > 0) {
                LongFlow.addFlowEdge(net, s, i, weights[i]);
                sumOfPositive += weights[i];
            }
            if (weights[i] < 0) {
                LongFlow.addFlowEdge(net, i, t, -weights[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            for(LongWeightDirectedEdge e : g[i]) {
                assert e.weight >= 0;
                LongFlow.addFlowEdge(net, i, e.to, e.weight);
            }
        }
        long minCut = mf.apply(net, s, t, INF);
        boolean[] belongToS = LongFlow.findSetST(net, s);
        System.arraycopy(belongToS, 0, picked, 0, n);
        return sumOfPositive - minCut;
    }
}
