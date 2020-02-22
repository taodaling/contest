package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class DoubleSpfaMinimumCostFlow implements DoubleMinimumCostFlow {
    IntegerDeque deque;
    double[] dists;
    boolean[] inque;
    DoubleCostFlowEdge[] prev;
    List<DoubleCostFlowEdge>[] net;

    public DoubleSpfaMinimumCostFlow(int vertexNum) {
        deque = new IntegerDequeImpl(vertexNum);
        dists = new double[vertexNum];
        inque = new boolean[vertexNum];
        prev = new DoubleCostFlowEdge[vertexNum];
    }

    private void spfa(int s, double inf) {
        deque.clear();
        for (int i = 0; i < net.length; i++) {
            dists[i] = inf;
            inque[i] = false;
        }
        dists[s] = 0;
        prev[s] = null;
        deque.addLast(s);
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            inque[head] = false;
            for (DoubleCostFlowEdge e : net[head]) {
                if (e.flow > 0 && dists[e.to] > dists[head] - e.cost) {
                    dists[e.to] = dists[head] - e.cost;
                    prev[e.to] = e;
                    if (!inque[e.to]) {
                        inque[e.to] = true;
                        deque.addLast(e.to);
                    }
                }
            }
        }
    }


    private static final double INF = (double) 2e18;

    @Override
    public double[] apply(List<DoubleCostFlowEdge>[] net, int s, int t, double send) {
        double cost = 0;
        double flow = 0;
        this.net = net;
        while (flow < send) {
            spfa(t, INF);
            if (dists[s] == INF) {
                break;
            }
            int iter = s;
            double sent = send - flow;
            while (prev[iter] != null) {
                sent = Math.min(sent, prev[iter].flow);
                iter = prev[iter].rev.to;
            }
            iter = s;
            while (prev[iter] != null) {
                DoubleFlow.send(prev[iter], -sent);
                iter = prev[iter].rev.to;
            }
            cost += sent * dists[s];
            flow += sent;
        }
        return new double[]{flow, cost};
    }
}
