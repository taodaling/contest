package template.primitve.generated.graph;

import template.primitve.generated.IntegerDeque;
import template.primitve.generated.IntegerDequeImpl;

import java.util.List;
import java.util.ListIterator;

public class LongDinic implements LongMaximumFlow {
    List<LongFlowEdge>[] g;
    int s;
    int t;
    IntegerDeque deque;
    int[] dists;
    ListIterator<LongFlowEdge>[] iterators;

    public LongDinic(int vertexNum) {
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
            LongFlow.send(g, e, sent);
            if (flow == 0) {
                iterators[root].previous();
                break;
            }
        }
        return snapshot - flow;
    }

    @Override
    public long apply(List<LongFlowEdge>[] g, int s, int t) {
        this.s = s;
        this.t = t;
        this.g = g;
        long flow = 0;
        while (true) {
            LongFlow.bfsForFlow(g, t, dists, Integer.MAX_VALUE, deque);
            if (dists[s] == Integer.MAX_VALUE) {
                break;
            }
            for (int i = 0; i < g.length; i++) {
                iterators[i] = g[i].listIterator();
            }
            flow += send(s, Long.MAX_VALUE);
        }
        return flow;
    }
}
