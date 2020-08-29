package template.primitve.generated.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class IntegerDijkstraV2MinimumCostFlow implements IntegerMinimumCostFlow {
    private int m;
    private int[] lastDist;
    private int[] curDist;
    private IntegerCostFlowEdge[] prev;
    private boolean[] inq;
    private IntegerDeque dq;
    private static final int INF = Integer.MAX_VALUE / 4;
    List<IntegerCostFlowEdge>[] g;

    public IntegerDijkstraV2MinimumCostFlow(int m) {
        this.m = m - 1;
        lastDist = new int[m];
        curDist = new int[m];
        prev = new IntegerCostFlowEdge[m];
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
            for (IntegerCostFlowEdge e : g[head]) {
                int dist;
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

    public int[] apply(List<IntegerCostFlowEdge>[] net, int s, int t, int send) {
        this.g = net;
        bf(s);
        int flow = 0;
        int cost = 0;
        while (flow < send) {
            dijkstra(s);
            if (prev[t] == null) {
                break;
            }
            int remain = send - flow;
            for (IntegerCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                remain = Math.min(remain, trace.flow);
            }
            for (IntegerCostFlowEdge trace = prev[t]; trace != null; trace = prev[trace.to]) {
                cost += trace.cost * -remain;
                IntegerFlow.send(trace, -remain);
            }
            flow += remain;
        }
        return new int[]{flow, cost};
    }
}