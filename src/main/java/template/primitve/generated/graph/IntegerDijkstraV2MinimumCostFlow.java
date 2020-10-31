package template.primitve.generated.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class IntegerDijkstraV2MinimumCostFlow implements IntegerAugmentMinimumCostFlow {
    protected int[] lastDist;
    protected int[] curDist;
    protected IntegerCostFlowEdge[] prev;
    protected boolean[] inq;
    protected IntegerDeque dq;
    protected static final int INF = Integer.MAX_VALUE / 4;
    protected List<IntegerCostFlowEdge>[] g;
    protected IntegerAugmentCallback callback = IntegerAugmentCallback.NIL;

    public void setCallback(IntegerAugmentCallback callback) {
        this.callback = callback;
    }

    public IntegerDijkstraV2MinimumCostFlow() {
    }

    protected void resize(int m){
        lastDist = new int[m];
        curDist = new int[m];
        prev = new IntegerCostFlowEdge[m];
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
            if (round > (int) n * n) {
                throw new RuntimeException();
            }
            for (IntegerCostFlowEdge e : g[head]) {
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
            for (IntegerCostFlowEdge e : g[head]) {
                int dist;
                if (e.rev.flow == 0 || curDist[e.to] <= (dist = curDist[head] + e.cost - lastDist[e.to] + lastDist[head])) {
                    continue;
                }
                prev[e.to] = (IntegerCostFlowEdge) e.rev;
                curDist[e.to] = dist;
            }
        }
    }

    protected void fixDist() {
        for (int i = 0; i < g.length; i++) {
            lastDist[i] = Math.min(curDist[i] + lastDist[i], INF);
        }
    }

    public int[] apply(List<IntegerCostFlowEdge>[] g, int s, int t, int send) {
        prepare(g.length);
        this.g = g;
        bf(s);
        int remain = send;
        int cost = 0;
        while (remain > 0) {
            dijkstra(s);
            fixDist();
            if (prev[t] == null) {
                break;
            }
            int maxFlow = remain;
            int sumOfCost = 0;
            for (IntegerCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                maxFlow = Math.min(maxFlow, trace.flow);
                sumOfCost -= trace.cost;
            }
            if (!callback.callback(maxFlow, sumOfCost)) {
                break;
            }
            for (IntegerCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                IntegerFlow.send(trace, -maxFlow);
            }
            assert maxFlow > 0;
            cost += sumOfCost * maxFlow;
            remain -= maxFlow;
        }
        return new int[]{send - remain, cost};
    }
}