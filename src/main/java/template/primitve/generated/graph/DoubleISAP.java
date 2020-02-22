package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.Arrays;
import java.util.List;

public class DoubleISAP implements DoubleMaximumFlow {
    private List<DoubleFlowEdge>[] net;
    private int s;
    private int t;
    private int[] dists;
    private int[] cnts;
    private int n;
    private boolean exit;
    private IntegerDeque deque;

    public DoubleISAP(int vertexNum) {
        dists = new int[vertexNum];
        cnts = new int[vertexNum + 2];
        deque = new IntegerDequeImpl(n);
    }

    private double send(int root, double flow) {
        if (root == t) {
            return flow;
        }
        double snapshot = flow;
        for (DoubleFlowEdge e : net[root]) {
            double remain;
            if (dists[e.to] + 1 != dists[root] || (remain = e.rev.flow) == 0) {
                continue;
            }
            double sent = send(e.to, Math.min(flow, remain));
            flow -= sent;
            DoubleFlow.send(e, sent);
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
    public double apply(List<DoubleFlowEdge>[] g, int s, int t, double send) {
        this.net = g;
        this.s = s;
        this.t = t;
        this.exit = false;
        n = g.length;
        DoubleFlow.bfsForFlow(g, t, dists, n + 1, deque);
        Arrays.fill(cnts, 0, n + 2, 0);
        for (int d : dists) {
            cnts[d]++;
        }
        double flow = 0;
        while (flow < send && !exit && dists[s] < n) {
            flow += send(s, send - flow);
        }
        return flow;
    }
}
