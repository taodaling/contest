package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;
import java.util.ListIterator;

public class IntegerDinic implements IntegerMaximumFlow {
    List<IntegerFlowEdge>[] g;
    int s;
    int t;
    IntegerDeque deque;
    int[] dists;
    ListIterator<IntegerFlowEdge>[] iterators;

    public IntegerDinic(int vertexNum) {
        deque = new IntegerDequeImpl(vertexNum);
        dists = new int[vertexNum];
        iterators = new ListIterator[vertexNum];
    }

    public int send(int root, int flow) {
        if (root == t) {
            return flow;
        }
        int snapshot = flow;
        while (iterators[root].hasNext()) {
            IntegerFlowEdge e = iterators[root].next();
            int remain;
            if (dists[e.to] + 1 != dists[root] || (remain = e.rev.flow) == 0) {
                continue;
            }
            int sent = send(e.to, Math.min(flow, remain));
            flow -= sent;
            IntegerFlow.send(e, sent);
            if (flow == 0) {
                iterators[root].previous();
                break;
            }
        }
        return snapshot - flow;
    }

    @Override
    public int apply(List<IntegerFlowEdge>[] g, int s, int t, int send) {
        this.s = s;
        this.t = t;
        this.g = g;
        int flow = 0;
        while (flow < send) {
            IntegerFlow.bfsForFlow(g, t, dists, Integer.MAX_VALUE, deque);
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
