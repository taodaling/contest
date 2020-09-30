package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;
import java.util.ListIterator;

public class LongDinic implements LongMaximumFlow {
    List<LongFlowEdge>[] g;
    int s;
    int t;
    IntegerDeque deque;
    int[] dists;
    ListIterator<LongFlowEdge>[] iterators;

    public LongDinic() {
    }

    public void ensure(int vertexNum) {
        if (dists != null && dists.length >= vertexNum) {
            return;
        }
        deque = new IntegerDequeImpl(vertexNum);
        dists = new int[vertexNum];
        iterators = new ListIterator[vertexNum];
    }

    public long send(int root, long flow) {
        if (root == t) {
            return flow;
        }
        long snapshot = flow;
        while (iterators[root].hasNext()) {
            LongFlowEdge e = iterators[root].next();
            long remain;
            if (dists[e.to] + 1 != dists[root] || (remain = e.rev.flow) == 0) {
                continue;
            }
            long sent = send(e.to, Math.min(flow, remain));
            flow -= sent;
            LongFlow.send(e, sent);
            if (flow == 0) {
                iterators[root].previous();
                break;
            }
        }
        return snapshot - flow;
    }

    @Override
    public long apply(List<LongFlowEdge>[] g, int s, int t, long send) {
        ensure(g.length);
        this.s = s;
        this.t = t;
        this.g = g;
        long flow = 0;
        while (flow < send) {
            LongFlow.bfsForFlow(g, t, dists, Integer.MAX_VALUE, deque);
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
