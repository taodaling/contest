package template.primitve.generated.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class DoubleDijkstraV2MinimumCostFlow implements DoubleAugmentMinimumCostFlow {
    protected double[] lastDist;
    protected double[] curDist;
    protected DoubleCostFlowEdge[] prev;
    protected boolean[] inq;
    protected IntegerDeque dq;
    protected static final double INF = Double.MAX_VALUE / 4;
    protected List<DoubleCostFlowEdge>[] g;
    protected DoubleAugmentCallback callback = DoubleAugmentCallback.NIL;

    public void setCallback(DoubleAugmentCallback callback) {
        this.callback = callback;
    }

    public DoubleDijkstraV2MinimumCostFlow() {
    }

    protected void resize(int m){
        lastDist = new double[m];
        curDist = new double[m];
        prev = new DoubleCostFlowEdge[m];
        inq = new boolean[m];
        dq = new IntegerDequeImpl(m);
    }

    private void prepare(int m) {
        if (inq != null && inq.length >= m) {
            return;
        }
        resize(m);
    }

    private void bf(int s) {
        int n = g.length;
        dq.clear();
        for (int i = 0; i < n; i++) {
            lastDist[i] = INF;
            inq[i] = false;
        }
        lastDist[s] = 0;
        inq[s] = true;
        dq.addLast(s);
        int round = 0;
        while (!dq.isEmpty()) {
            round++;
            int head = dq.removeFirst();
            inq[head] = false;
            if (round > (double) n * n) {
                throw new RuntimeException();
            }
            for (DoubleCostFlowEdge e : g[head]) {
                if (DigitUtils.equal(e.rev.flow, 0) || lastDist[e.to] <= lastDist[head] + e.cost) {
                    continue;
                }
                lastDist[e.to] = lastDist[head] + e.cost;
                if (!inq[e.to]) {
                    inq[e.to] = true;
                    dq.addLast(e.to);
                }
            }
        }
    }

    protected void dijkstra(int s) {
        int n = g.length;
        for (int i = 0; i < n; i++) {
            curDist[i] = INF;
            prev[i] = null;
            inq[i] = false;
        }
        curDist[s] = 0;

        for (int i = 0; i < n; i++) {
            int head = -1;
            for (int j = 0; j < n; j++) {
                if (!inq[j] && (head == -1 || curDist[j] < curDist[head])) {
                    head = j;
                }
            }
            if (curDist[head] >= INF) {
                break;
            }
            inq[head] = true;
            for (DoubleCostFlowEdge e : g[head]) {
                double dist;
                if (e.rev.flow == 0 || curDist[e.to] <= (dist = curDist[head] + e.cost - lastDist[e.to] + lastDist[head])) {
                    continue;
                }
                prev[e.to] = (DoubleCostFlowEdge) e.rev;
                curDist[e.to] = dist;
            }
        }
    }

    protected void fixDist() {
        for (int i = 0; i < g.length; i++) {
            lastDist[i] = Math.min(curDist[i] + lastDist[i], INF);
        }
    }

    public double[] apply(List<DoubleCostFlowEdge>[] g, int s, int t, double send) {
        prepare(g.length);
        this.g = g;
        bf(s);
        double remain = send;
        double cost = 0;
        while (remain > 0) {
            dijkstra(s);
            fixDist();
            if (prev[t] == null) {
                break;
            }
            double maxFlow = remain;
            double sumOfCost = 0;
            for (DoubleCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                maxFlow = Math.min(maxFlow, trace.flow);
                sumOfCost -= trace.cost;
            }
            if (!callback.callback(maxFlow, sumOfCost)) {
                break;
            }
            for (DoubleCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                DoubleFlow.send(trace, -maxFlow);
            }
            assert maxFlow > 0;
            cost += sumOfCost * maxFlow;
            remain -= maxFlow;
        }
        return new double[]{send - remain, cost};
    }
}