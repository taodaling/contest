package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.List;

public class IntegerISAP implements IntegerMaximumFlow {
    private List<IntegerFlowEdge>[] net;
    private int s;
    private int t;
    private int[] dists;
    private int[] cnts;
    private int n;
    private boolean exit;
    private IntegerDeque deque;

    public IntegerISAP() {
    }

    public void ensure(int vertexNum) {
        if (dists != null && dists.length >= vertexNum) {
            return;
        }
        dists = new int[vertexNum];
        cnts = new int[vertexNum + 2];
        deque = new IntegerDequeImpl(n);
    }

    private int send(int root, int flow) {
        if (root == t) {
            return flow;
        }
        int snapshot = flow;
        for (IntegerFlowEdge e : net[root]) {
            int remain;
            if (dists[e.to] + 1 != dists[root] || (remain = e.rev.flow) == 0) {
                continue;
            }
            int sent = send(e.to, Math.min(flow, remain));
            flow -= sent;
            IntegerFlow.send(e, sent);
            if (flow == 0 || exit) {
                break;
            }
        }
        if (flow == snapshot) {
            cnts[dists[root]]--;
            dists[root]++;
            cnts[dists[root]]++;
            if (cnts[dists[root] - 1] == 0) {
                exit = true;
            }
        }
        return snapshot - flow;
    }

    @Override
    public int apply(List<IntegerFlowEdge>[] g, int s, int t, int send) {
        ensure(g.length);
        this.net = g;
        this.s = s;
        this.t = t;
        this.exit = false;
        n = g.length;
        IntegerFlow.bfsForFlow(g, t, dists, n + 1, deque);
        Arrays.fill(cnts, 0, n + 2, 0);
        for (int d : dists) {
            cnts[d]++;
        }
        int flow = 0;
        while (flow < send && !exit && dists[s] < n) {
            flow += send(s, send - flow);
        }
        return flow;
    }
}
