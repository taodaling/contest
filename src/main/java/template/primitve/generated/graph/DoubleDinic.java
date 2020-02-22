package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;
import java.util.ListIterator;

public class DoubleDinic implements DoubleMaximumFlow {
    List<DoubleFlowEdge>[] g;
    int s;
    int t;
    IntegerDeque deque;
    int[] dists;
    ListIterator<DoubleFlowEdge>[] iterators;

    public DoubleDinic(int vertexNum) {
        deque = new IntegerDequeImpl(vertexNum);
        dists = new int[vertexNum];
        iterators = new ListIterator[vertexNum];
    }

    public double send(int root, double flow) {
        if (root == t) {
            return flow;
        }
        double snapshot = flow;
        while (iterators[root].hasNext()) {
            DoubleFlowEdge e = iterators[root].next();
            double remain;
            if (dists[e.to] + 1 != dists[root] || (remain = e.rev.flow) == 0) {
                continue;
            }
            double sent = send(e.to, Math.min(flow, remain));
            flow -= sent;
            DoubleFlow.send(e, sent);
            if (flow == 0) {
                iterators[root].previous();
                break;
            }
        }
        return snapshot - flow;
    }

    @Override
    public double apply(List<DoubleFlowEdge>[] g, int s, int t, double send) {
        this.s = s;
        this.t = t;
        this.g = g;
        double flow = 0;
        while (flow < send) {
            DoubleFlow.bfsForFlow(g, t, dists, Integer.MAX_VALUE, deque);
            if (dists[s] == Integer.MAX_VALUE) {
                break;
            }
            for (int i = 0; i < g.length; i++) {
                iterators[i] = g[i].listIterator();
            }
            flow += send(s, send - flow);
        }
        return flow;
    }
}
