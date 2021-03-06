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
    int[] iterators;

    public IntegerDinic() {
    }

    public void ensure(int vertexNum) {
        if (dists != null && dists.length >= vertexNum) {
            return;
        }
        deque = new IntegerDequeImpl(vertexNum);
        dists = new int[vertexNum];
        iterators = new int[vertexNum];
    }

    public int send(int root, int flow) {
        if (root == t) {
            return flow;
        }
        int snapshot = flow;
        while (iterators[root] >= 0 && flow > 0) {
            IntegerFlowEdge e = g[root].get(iterators[root]);
            if (dists[e.to] + 1 == dists[root] && e.rev.flow != 0) {
                int sent = send(e.to, Math.min(flow, e.rev.flow));
                if (sent > 0) {
                    flow -= sent;
                    IntegerFlow.send(e, sent);
                    continue;
                }
            }
            iterators[root]--;
        }
        return snapshot - flow;
    }

    @Override
    public int apply(List<IntegerFlowEdge>[] g, int s, int t, int send) {
        ensure(g.length);
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
                iterators[i] = g[i].size() - 1;
            }
            flow += send(s, send - flow);
        }
        return flow;
    }
}
