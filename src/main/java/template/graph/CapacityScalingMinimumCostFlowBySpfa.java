package template.graph;

import template.binary.Log2;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Solving minimum cost flow by removing negative circle.
 */
public class CapacityScalingMinimumCostFlowBySpfa {
    private static long inf = 1L << 60;
    private long[] dist;
    private LongCostFlowEdge[] pre;
    private boolean[] inque;
    private List<LongCostFlowEdge>[] g;
    private IntegerDeque dq;
    private List<LongCostFlowEdge> marked = new ArrayList<>();

    public CapacityScalingMinimumCostFlowBySpfa() {
    }

    public void prepare(int n) {
        if (dist != null && dist.length >= n) {
            return;
        }
        dist = new long[n];
        pre = new LongCostFlowEdge[n];
        inque = new boolean[n];
        dq = new IntegerDequeImpl(n);
    }

    private long flow(LongCostFlowEdge e, int shift) {
        return e.flow >> shift;
    }


    private void send(LongCostFlowEdge e, int shift) {
        LongFlow.send(e, 1L << shift);
    }

    private void spfa(int from, int shift) {
        Arrays.fill(dist, inf);
        Arrays.fill(pre, null);
        dist[from] = 0;
        dq.clear();
        dq.addLast(from);
        inque[from] = true;
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            inque[head] = false;
            for (LongCostFlowEdge e : g[head]) {
                if (flow(e.rev, shift) == 0) {
                    continue;
                }
                if (dist[e.to] > dist[head] + e.cost) {
                    dist[e.to] = dist[head] + e.cost;
                    pre[e.to] = e;
                    if (!inque[e.to]) {
                        inque[e.to] = true;
                        dq.addLast(e.to);
                    }
                }
            }
        }
    }

    /**
     * O(VE^2 \log_2 max_capacity_of_each_edges)
     *
     * @param maxFlow whether ensure it's a max flow (else only negative cost flow will be sent)
     */
    public long[] apply(List<LongCostFlowEdge>[] g, int s, int t, boolean maxFlow) {
        this.g = g;
        prepare(g.length);
        long maxCapacity = 0;
        for (List<LongCostFlowEdge> list : g) {
            for (LongCostFlowEdge e : list) {
                if (!e.real) {
                    continue;
                }
                maxCapacity = Math.max(maxCapacity, e.rev.flow);
            }
        }

        LongFlow.addCostEdge(g, t, s, inf, maxFlow ? -inf : 0);
        for (int i = Log2.floorLog(maxCapacity); i >= 0; i--) {
            long bit = 1L << i;
            marked.clear();
            for (List<LongCostFlowEdge> list : g) {
                for (LongCostFlowEdge e : list) {
                    if (!e.real) {
                        continue;
                    }
                    if ((e.rev.flow & bit) != 0) {
                        e.rev.flow -= bit;
                        marked.add(e);
                    }
                }
            }
            for (LongCostFlowEdge e : marked) {
                int u = e.rev.to;
                int v = e.to;
                if (flow(e.rev, i) != 0) {
                    e.rev.flow += bit;
                    continue;
                }
                spfa(v, i);
                e.rev.flow += bit;
                if (dist[u] + e.cost >= 0) {
                    continue;
                }
                send(e, i);
                for (int cur = u; cur != v; cur = pre[cur].rev.to) {
                    send(pre[cur], i);
                }
            }
        }

        long flow = g[t].get(g[t].size() - 1).flow;
        g[t].remove(g[t].size() - 1);
        g[s].remove(g[s].size() - 1);

        long cost = 0;
        for (List<LongCostFlowEdge> list : g) {
            for (LongCostFlowEdge e : list) {
                if (!e.real) {
                    continue;
                }
                cost += e.cost * e.flow;
            }
        }
        return new long[]{flow, cost};
    }
}
