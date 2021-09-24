package template.primitve.generated.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class LongDijkstraV2MinimumCostFlow implements LongAugmentMinimumCostFlow {
    protected long[] lastDist;
    protected long[] curDist;
    protected LongCostFlowEdge[] prev;
    protected boolean[] inq;
    protected IntegerDeque dq;
    protected static final long INF = Long.MAX_VALUE / 4;
    protected List<LongCostFlowEdge>[] g;
    protected LongAugmentCallback callback = LongAugmentCallback.NIL;

    public void setCallback(LongAugmentCallback callback) {
        this.callback = callback;
    }

    public LongDijkstraV2MinimumCostFlow() {
    }

    protected void resize(int m){
        lastDist = new long[m];
        curDist = new long[m];
        prev = new LongCostFlowEdge[m];
        inq = new boolean[m];
        dq = new IntegerDequeImpl(m);
    }

    private void prepare(int m) {
        if (inq != null && inq.length >= m) {
            return;
        }
        resize(m);
    }

    protected void bf(int s) {
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
            if (round > (long) n * n) {
                throw new RuntimeException();
            }
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
            for (LongCostFlowEdge e : g[head]) {
                long dist;
                if (e.rev.flow == 0 || curDist[e.to] <= (dist = curDist[head] + e.cost - lastDist[e.to] + lastDist[head])) {
                    continue;
                }
                prev[e.to] = (LongCostFlowEdge) e.rev;
                curDist[e.to] = dist;
            }
        }
    }

    protected void fixDist() {
        for (int i = 0; i < g.length; i++) {
            lastDist[i] = Math.min(curDist[i] + lastDist[i], INF);
        }
    }

    public long[] apply(List<LongCostFlowEdge>[] g, int s, int t, long send) {
        prepare(g.length);
        this.g = g;
        bf(s);
        long remain = send;
        long cost = 0;
        while (remain > 0) {
            dijkstra(s);
            fixDist();
            if (prev[t] == null) {
                break;
            }
            long maxFlow = remain;
            long sumOfCost = 0;
            for (LongCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                maxFlow = Math.min(maxFlow, trace.flow);
                sumOfCost -= trace.cost;
            }
            if (!callback.callback(maxFlow, sumOfCost)) {
                break;
            }
            for (LongCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                LongFlow.send(trace, -maxFlow);
            }
            assert maxFlow > 0;
            cost += sumOfCost * maxFlow;
            remain -= maxFlow;
        }
        return new long[]{send - remain, cost};
    }
}