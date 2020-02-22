package template.primitve.generated.graph;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Maximum flow of minimum cost with potentials in O(min(E^2*V*logV, E*logV*FLOW))
 */
public class IntegerMinCostFlowPolynomial implements IntegerMinimumCostFlow {
    static void bellmanFord(List<IntegerCostFlowEdge>[] graph, int s, int[] dist) {
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
                IntegerCostFlowEdge e = graph[u].get(i);
                if (e.rev.flow == 0)
                    continue;
                int v = e.to;
                int ndist = dist[u] + e.cost;
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
        public State(int priou, int u) {
            this.priou = priou;
            this.u = u;
        }

        int priou;
        int u;

        @Override
        public int compareTo(State o) {
            return priou == o.priou ? Integer.compare(u, o.u) : Integer.compare(priou, o.priou);
        }
    }

    public static int[] minCostFlow(List<IntegerCostFlowEdge>[] graph, int s, int t, int maxf) {
        int n = graph.length;
        int[] prio = new int[n];
        int[] curflow = new int[n];
        int[] prevedge = new int[n];
        int[] prevnode = new int[n];
        int[] pot = new int[n];

        // bellmanFord invocation can be skipped if edges costs are non-negative
        bellmanFord(graph, s, pot);
        int flow = 0;
        int flowCost = 0;

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
                int priou = cur.priou;
                if (priou != prio[u])
                    continue;
                finished[u] = true;
                for (int i = 0; i < graph[u].size(); i++) {
                    IntegerCostFlowEdge e = graph[u].get(i);
                    if (e.rev.flow == 0)
                        continue;
                    int v = e.to;
                    int nprio = prio[u] + e.cost + pot[u] - pot[v];
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
            int df = Math.min(curflow[t], maxf - flow);
            flow += df;
            for (int v = t; v != s; v = prevnode[v]) {
                IntegerCostFlowEdge e = graph[prevnode[v]].get(prevedge[v]);
                IntegerFlow.send( e, df);
                flowCost += df * e.cost;
            }
        }
        return new int[]{flow, flowCost};
    }

    @Override
    public int[] apply(List<IntegerCostFlowEdge>[] net, int s, int t, int send) {
        return minCostFlow(net, s, t, send);
    }

    // Usage example
    public static void main(String[] args) {
//        List<Edge>[] graph = createGraph(3);
//        addEdge(graph, 0, 1, 3, 1);
//        addEdge(graph, 0, 2, 2, 1);
//        addEdge(graph, 1, 2, 2, 1);
//        int[] res = minCostFlow(graph, 0, 2, Integer.MAX_VALUE);
//        int flow = res[0];
//        int flowCost = res[1];
//        System.out.println(4 == flow);
//        System.out.println(6 == flowCost);
    }
}