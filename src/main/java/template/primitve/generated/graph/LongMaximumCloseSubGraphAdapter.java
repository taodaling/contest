package template.primitve.generated.graph;

import template.graph.DirectedEdge;

import java.util.List;

public class LongMaximumCloseSubGraphAdapter implements LongMaximumCloseSubGraph {
    private final LongMaximumFlow mf;
    private static final long INF = Long.MAX_VALUE / 4;

    public LongMaximumCloseSubGraphAdapter(LongMaximumFlow mf) {
        this.mf = mf;
    }

    @Override
    public long maximumCloseSubGraph(List<DirectedEdge>[] g, long[] weights, boolean[] picked) {
        int n = g.length;
        int s = n;
        int t = n + 1;
        List<LongFlowEdge>[] net = LongFlow.createFlow(n + 2);
        long sumOfPositive = 0;
        for (int i = 0; i < n; i++) {
            if (weights[i] > 0) {
                LongFlow.addEdge(net, s, i, weights[i]);
                sumOfPositive += weights[i];
            }
            if (weights[i] < 0) {
                LongFlow.addEdge(net, i, t, -weights[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            for (DirectedEdge e : g[i]) {
                LongFlow.addEdge(net, i, e.to, INF);
            }
        }
        long minCut = mf.apply(net, s, t, INF);
        boolean[] belongToS = LongFlow.findSetST(net, s);
        System.arraycopy(belongToS, 0, picked, 0, n);
        return sumOfPositive - minCut;
    }
}
