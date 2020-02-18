package template.primitve.generated.graph;

import template.graph.DirectedEdge;

import java.util.List;

public class IntegerMaximumCloseSubGraphAdapter implements IntegerMaximumCloseSubGraph {
    private final IntegerMaximumFlow mf;
    private static final int INF = (int) 2e18;

    public IntegerMaximumCloseSubGraphAdapter(IntegerMaximumFlow mf) {
        this.mf = mf;
    }

    @Override
    public int maximumCloseSubGraph(List<DirectedEdge>[] g, int[] weights, boolean[] picked) {
        int n = g.length;
        int s = n;
        int t = n + 1;
        List<IntegerFlowEdge>[] net = IntegerFlow.createFlow(n + 2);
        int sumOfPositive = 0;
        for (int i = 0; i < n; i++) {
            if (weights[i] > 0) {
                IntegerFlow.addEdge(net, s, i, weights[i]);
                sumOfPositive += weights[i];
            }
            if (weights[i] < 0) {
                IntegerFlow.addEdge(net, i, t, -weights[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            for (DirectedEdge e : g[i]) {
                IntegerFlow.addEdge(net, i, e.to, INF);
            }
        }
        int minCut = mf.apply(net, s, t);
        boolean[] beintToS = IntegerFlow.findSetST(net, s);
        System.arraycopy(beintToS, 0, picked, 0, n);
        return sumOfPositive - minCut;
    }
}
