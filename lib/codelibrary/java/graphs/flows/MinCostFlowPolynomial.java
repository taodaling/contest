package graphs.flows;

import template.primitve.generated.graph.LongCostFlowEdge;
import template.primitve.generated.graph.LongFlow;
import template.primitve.generated.graph.LongMinimumCostFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Maximum flow of minimum cost with potentials in O(min(E^2*V*logV, E*logV*FLOW))
 */
public class MinCostFlowPolynomial implements LongMinimumCostFlow {
    static void bellmanFord(List<LongCostFlowEdge>[] graph, int s, long[] dist) {
        int n = graph.length;
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[s] = 0;
        boolean[] inqueue = new boolean[n];
        int[] q = new int[n];
        int qt = 0;
        q[qt++] = s;
        for (int qh = 0; (qh - qt) % n != 0; qh++) {
            int u = q[qh % n];
            inqueue[u] = false;
            for (int i = 0; i < graph[u].size(); i++) {
                LongCostFlowEdge e = graph[u].get(i);
                if (e.rev.flow == 0)
                    continue;
                int v = e.to;
                long ndist = dist[u] + e.cost;
                if (dist[v] > ndist) {
                    dist[v] = ndist;
                    if (!inqueue[v]) {
                        inqueue[v] = true;
                        q[qt++ % n] = v;
                    }
                }
            }
        }
    }

    public static class State implements Comparable<State> {
        public State(long priou, int u) {
            this.priou = priou;
            this.u = u;
        }

        long priou;
        int u;

        @Override
        public int compareTo(State o) {
            return priou == o.priou ? Integer.compare(u, o.u) : Long.compare(priou, o.priou);
        }
    }

    public static long[] minCostFlow(List<LongCostFlowEdge>[] graph, int s, int t, long maxf) {
        int n = graph.length;
        long[] prio = new long[n];
        long[] curflow = new long[n];
        int[] prevedge = new int[n];
        int[] prevnode = new int[n];
        long[] pot = new long[n];

        // bellmanFord invocation can be skipped if edges costs are non-negative
        bellmanFord(graph, s, pot);
        long flow = 0;
        long flowCost = 0;

        PriorityQueue<State> q = new PriorityQueue<>();
        while (flow < maxf) {
            q.clear();
            q.add(new State(0, s));
            Arrays.fill(prio, Integer.MAX_VALUE);
            prio[s] = 0;
            boolean[] finished = new boolean[n];
            curflow[s] = Integer.MAX_VALUE;
            while (!finished[t] && !q.isEmpty()) {
                State cur = q.remove();
                int u = cur.u;
                long priou = cur.priou;
                if (priou != prio[u])
                    continue;
                finished[u] = true;
                for (int i = 0; i < graph[u].size(); i++) {
                    LongCostFlowEdge e = graph[u].get(i);
                    if (e.rev.flow == 0)
                        continue;
                    int v = e.to;
                    long nprio = prio[u] + e.cost + pot[u] - pot[v];
                    if (prio[v] > nprio) {
                        prio[v] = nprio;
                        q.add(new State(nprio, v));
                        prevnode[v] = u;
                        prevedge[v] = i;
                        curflow[v] = Math.min(curflow[u], e.rev.flow);
                    }
                }
            }
            if (prio[t] == Integer.MAX_VALUE)
                break;
            for (int i = 0; i < n; i++)
                if (finished[i])
                    pot[i] += prio[i] - prio[t];
            long df = Math.min(curflow[t], maxf - flow);
            flow += df;
            for (int v = t; v != s; v = prevnode[v]) {
                LongCostFlowEdge e = graph[prevnode[v]].get(prevedge[v]);
                LongFlow.send(e, df);
                flowCost += df * e.cost;
            }
        }
        return new long[]{flow, flowCost};
    }

    @Override
    public long[] apply(List<LongCostFlowEdge>[] net, int s, int t, long send) {
        return minCostFlow(net, s, t, send);
    }

    // Usage example
    public static void main(String[] args) {
//        List<Edge>[] graph = createGraph(3);
//        addEdge(graph, 0, 1, 3, 1);
//        addEdge(graph, 0, 2, 2, 1);
//        addEdge(graph, 1, 2, 2, 1);
//        long[] res = minCostFlow(graph, 0, 2, Integer.MAX_VALUE);
//        long flow = res[0];
//        long flowCost = res[1];
//        System.out.println(4 == flow);
//        System.out.println(6 == flowCost);
    }
}