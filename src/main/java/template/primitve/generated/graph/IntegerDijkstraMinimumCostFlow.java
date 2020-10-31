package template.primitve.generated.graph;

import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.List;

public class IntegerDijkstraMinimumCostFlow extends IntegerDijkstraV2MinimumCostFlow {
    protected IntegerPriorityQueueBasedOnSegment segment;
    protected int m;

    public void setCallback(IntegerAugmentCallback callback) {
        this.callback = callback;
    }

    public IntegerDijkstraMinimumCostFlow() {
    }

    @Override
    protected void resize(int m) {
        super.resize(m);
        this.m = m - 1;
        this.segment = new IntegerPriorityQueueBasedOnSegment(0, m - 1);
    }

    @Override
    protected void dijkstra(int s) {
        int n = g.length;
        segment.reset(0, m);
        for (int i = 0; i < n; i++) {
            curDist[i] = INF;
            inq[i] = true;
            prev[i] = null;
        }
        curDist[s] = 0;
        segment.update(s, 0, m, 0);

        for (int i = 0; i < n; i++) {
            int head = segment.pop(0, m);
            inq[head] = false;
            if (curDist[head] >= INF) {
                break;
            }
            for (IntegerCostFlowEdge e : g[head]) {
                int dist;
                if (DigitUtils.equal(e.rev.flow, 0) || !inq[e.to] || curDist[e.to] <= (dist = curDist[head] + e.cost - lastDist[e.to] + lastDist[head])) {
                    continue;
                }
                prev[e.to] = (IntegerCostFlowEdge) e.rev;
                curDist[e.to] = dist;
                segment.update(e.to, 0, m, curDist[e.to]);
            }
        }
    }
}