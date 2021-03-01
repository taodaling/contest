package template.primitve.generated.graph;

import template.graph.DirectedEdge;
import template.graph.Graph;

import java.util.List;

public class IntegerMaximumCloseSubGraphAdapter implements IntegerMaximumCloseSubGraph {
    private final IntegerMaximumFlow mf;
    private static final int INF = Integer.MAX_VALUE / 4;

    public IntegerMaximumCloseSubGraphAdapter(IntegerMaximumFlow mf) {
        this.mf = mf;
    }

    @Override
    public int maximumCloseSubGraph(List<IntegerWeightDirectedEdge>[] g, int[] weights, boolean[] picked) {
        int n = weights.length;
        int s = n;
        int t = n + 1;
        List<IntegerFlowEdge>[] net = Graph.createGraph(n + 2);
        int sumOfPositive = 0;
        for (int i = 0; i < n; i++) {
            if (weights[i] > 0) {
                IntegerFlow.addFlowEdge(net, s, i, weights[i]);
                sumOfPositive += weights[i];
            }
            if (weights[i] < 0) {
                IntegerFlow.addFlowEdge(net, i, t, -weights[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            for(IntegerWeightDirectedEdge e : g[i]) {
                assert e.weight >= 0;
                IntegerFlow.addFlowEdge(net, i, e.to, e.weight);
            }
        }
        int minCut = mf.apply(net, s, t, INF);
        boolean[] beintToS = IntegerFlow.findSetST(net, s);
        System.arraycopy(beintToS, 0, picked, 0, n);
        return sumOfPositive - minCut;
    }
}
