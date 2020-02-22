package template.primitve.generated.graph;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Maximum flow of minimum cost with potentials in O(min(E^2*V*logV, E*logV*FLOW))
 */
public class DoubleMinCostFlowPolynomial implements DoubleMinimumCostFlow {
    static void bellmanFord(List<DoubleCostFlowEdge>[] graph, int s, double[] dist) {
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
                DoubleCostFlowEdge e = graph[u].get(i);
                if (e.rev.flow == 0)
                    continue;
                int v = e.to;
                double ndist = dist[u] + e.cost;
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
        public State(double priou, int u) {
            this.priou = priou;
            this.u = u;
        }

        double priou;
        int u;

        @Override
        public int compareTo(State o) {
            return priou == o.priou ? Integer.compare(u, o.u) : Double.compare(priou, o.priou);
        }
    }

    public static double[] minCostFlow(List<DoubleCostFlowEdge>[] graph, int s, int t, double maxf) {
        int n = graph.length;
        double[] prio = new double[n];
        double[] curflow = new double[n];
        int[] prevedge = new int[n];
        int[] prevnode = new int[n];
        double[] pot = new double[n];

        // bellmanFord invocation can be skipped if edges costs are non-negative
        bellmanFord(graph, s, pot);
        double flow = 0;
        double flowCost = 0;

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
                double priou = cur.priou;
                if (priou != prio[u])
                    continue;
                finished[u] = true;
                for (int i = 0; i < graph[u].size(); i++) {
                    DoubleCostFlowEdge e = graph[u].get(i);
                    if (e.rev.flow == 0)
                        continue;
                    int v = e.to;
                    double nprio = prio[u] + e.cost + pot[u] - pot[v];
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
            double df = Math.min(curflow[t], maxf - flow);
            flow += df;
            for (int v = t; v != s; v = prevnode[v]) {
                DoubleCostFlowEdge e = graph[prevnode[v]].get(prevedge[v]);
                DoubleFlow.send( e, df);
                flowCost += df * e.cost;
            }
        }
        return new double[]{flow, flowCost};
    }

    @Override
    public double[] apply(List<DoubleCostFlowEdge>[] net, int s, int t, double send) {
        return minCostFlow(net, s, t, send);
    }

    // Usage example
    public static void main(String[] args) {
//        List<Edge>[] graph = createGraph(3);
//        addEdge(graph, 0, 1, 3, 1);
//        addEdge(graph, 0, 2, 2, 1);
//        addEdge(graph, 1, 2, 2, 1);
//        double[] res = minCostFlow(graph, 0, 2, Integer.MAX_VALUE);
//        double flow = res[0];
//        double flowCost = res[1];
//        System.out.println(4 == flow);
//        System.out.println(6 == flowCost);
    }
}