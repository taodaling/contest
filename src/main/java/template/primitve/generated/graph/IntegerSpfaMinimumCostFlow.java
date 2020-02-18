package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class IntegerSpfaMinimumCostFlow implements IntegerMinimumCostFlow {
    IntegerDeque deque;
    int[] dists;
    boolean[] inque;
    IntegerCostFlowEdge[] prev;
    List<IntegerCostFlowEdge>[] net;

    public IntegerSpfaMinimumCostFlow(int vertexNum) {
        deque = new IntegerDequeImpl(vertexNum);
        dists = new int[vertexNum];
        inque = new boolean[vertexNum];
        prev = new IntegerCostFlowEdge[vertexNum];
    }

    private void spfa(int s, int inf) {
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
            for (IntegerCostFlowEdge e : net[head]) {
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


    private static final int INF = (int) 2e18;

    @Override
    public int[] apply(List<IntegerCostFlowEdge>[] net, int s, int t) {
        int cost = 0;
        int flow = 0;
        this.net = net;
        while (true) {
            spfa(t, INF);
            if (dists[s] == INF) {
                break;
            }
            int iter = s;
            int sent = Integer.MAX_VALUE;
            while (prev[iter] != null) {
                sent = Math.min(sent, prev[iter].flow);
                iter = prev[iter].rev.to;
            }
            iter = s;
            while (prev[iter] != null) {
                IntegerFlow.send(net, prev[iter], -sent);
                iter = prev[iter].rev.to;
            }
            cost += sent * dists[s];
            flow += sent;
        }
        return new int[]{flow, cost};
    }
}
