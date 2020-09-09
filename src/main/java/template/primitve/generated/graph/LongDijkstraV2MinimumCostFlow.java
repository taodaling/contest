package template.primitve.generated.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class LongDijkstraV2MinimumCostFlow implements LongAugmentMinimumCostFlow {
    private int m;
    private long[] lastDist;
    private long[] curDist;
    private LongCostFlowEdge[] prev;
    private boolean[] inq;
    private IntegerDeque dq;
    private static final long INF = Long.MAX_VALUE / 4;
    private List<LongCostFlowEdge>[] g;
    private LongAugmentCallback callback = LongAugmentCallback.NIL;

    public void setCallback(LongAugmentCallback callback) {
        this.callback = callback;
    }

    public LongDijkstraV2MinimumCostFlow(int m) {
        this.m = m - 1;
        lastDist = new long[m];
        curDist = new long[m];
        prev = new LongCostFlowEdge[m];
        inq = new boolean[m];
        dq = new IntegerDequeImpl(m);
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
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            inq[head] = false;
            for (LongCostFlowEdge e : g[head]) {
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

    private void dijkstra(int s) {
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
            for (LongCostFlowEdge e : g[head]) {
                long dist;
                if (e.rev.flow == 0 || curDist[e.to] <= (dist = curDist[head] + e.cost - lastDist[e.to] + lastDist[head])) {
                    continue;
                }
                prev[e.to] = e.rev;
                curDist[e.to] = dist;
            }
        }

        for (int i = 0; i < n; i++) {
            lastDist[i] = Math.min(curDist[i] + lastDist[i], INF);
        }
    }

    public long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send) {
        this.g = net;
        bf(s);
        long flow = 0;
        long cost = 0;
        while (flow < send) {
            dijkstra(s);
            if (prev[t] == null) {
                break;
            }
            long remain = send - flow;
            for (LongCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                remain = Math.min(remain, trace.flow);
            }
            long sumOfCost = 0;
            for (LongCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                sumOfCost -= trace.cost;
                LongFlow.send(trace, -remain);
            }
            cost += sumOfCost * -remain;
            flow += remain;
            if (!callback.callback(remain, sumOfCost)) {
                break;
            }
        }
        return new long[]{flow, cost};
    }
}