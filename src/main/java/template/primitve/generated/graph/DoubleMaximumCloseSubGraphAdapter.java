package template.primitve.generated.graph;

import template.graph.DirectedEdge;
import template.graph.Graph;

import java.util.List;

public class DoubleMaximumCloseSubGraphAdapter implements DoubleMaximumCloseSubGraph {
    private final DoubleMaximumFlow mf;
    private static final double INF = Double.MAX_VALUE / 4;

    public DoubleMaximumCloseSubGraphAdapter(DoubleMaximumFlow mf) {
        this.mf = mf;
    }

    @Override
    public double maximumCloseSubGraph(List<DirectedEdge>[] g, double[] weights, boolean[] picked) {
        int n = g.length;
        int s = n;
        int t = n + 1;
        List<DoubleFlowEdge>[] net = Graph.createGraph(n + 2);
        double sumOfPositive = 0;
        for (int i = 0; i < n; i++) {
            if (weights[i] > 0) {
                DoubleFlow.addFlowEdge(net, s, i, weights[i]);
                sumOfPositive += weights[i];
            }
            if (weights[i] < 0) {
                DoubleFlow.addFlowEdge(net, i, t, -weights[i]);
            }
        }
        for (int i = 0; i < n; i++) {
            for (DirectedEdge e : g[i]) {
                DoubleFlow.addFlowEdge(net, i, e.to, INF);
            }
        }
        double minCut = mf.apply(net, s, t, INF);
        boolean[] bedoubleToS = DoubleFlow.findSetST(net, s);
        System.arraycopy(bedoubleToS, 0, picked, 0, n);
        return sumOfPositive - minCut;
    }
}
