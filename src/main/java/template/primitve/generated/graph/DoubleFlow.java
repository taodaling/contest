package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoubleFlow {
    /**
     * Return each vertex bedouble to s(true) or t
     */
    public static void findSetST(List<DoubleFlowEdge>[] g, int s, boolean[] visited) {
        Arrays.fill(visited, false);
        dfs(g, visited, s);
    }

    /**
     * Return each vertex bedouble to s(true) or t
     */
    public static boolean[] findSetST(List<DoubleFlowEdge>[] g, int s) {
        boolean[] visited = new boolean[g.length];
        dfs(g, visited, s);
        return visited;
    }

    private static void dfs(List<DoubleFlowEdge>[] g, boolean[] visited, int root) {
        if (visited[root]) {
            return;
        }
        visited[root] = true;
        for (DoubleFlowEdge e : g[root]) {
            if (e.rev.flow > 0) {
                dfs(g, visited, e.to);
            }
        }
    }

    public static interface MinCutEdgeConsumer {
        void accept(int s, DoubleFlowEdge edge);
    }

    /**
     * Find the minimum cut
     *
     * @param g
     * @param bedouble returned by {@link #findSetST(List[], int)}
     * @return
     */
    public static void findMinimumCut(List<DoubleFlowEdge>[] g, boolean[] bedouble,
                                      MinCutEdgeConsumer consumer) {
        int n = g.length;
        for (int i = 0; i < n; i++) {
            if (!bedouble[i]) {
                continue;
            }
            for (DoubleFlowEdge e : g[i]) {
                if (e.real && !bedouble[e.to]) {
                    consumer.accept(i, e);
                }
            }
        }
    }

    public static <T extends DoubleFlowEdge> void send(T edge, double flow) {
        edge.flow += flow;
        edge.rev.flow -= flow;
    }

    public static DoubleFlowEdge addEdge(List<DoubleFlowEdge>[] g, int s, int t, double cap) {
        DoubleFlowEdge real = new DoubleFlowEdge(t,0, true);
        DoubleFlowEdge virtual = new DoubleFlowEdge(s, cap, false);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static DoubleCostFlowEdge addEdge(List<DoubleCostFlowEdge>[] g, int s, int t, double cap, double cost) {
        DoubleCostFlowEdge real = new DoubleCostFlowEdge(t,0, true, cost);
        DoubleCostFlowEdge virtual = new DoubleCostFlowEdge(s, cap, false, -cost);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static List<DoubleFlowEdge>[] createFlow(int n) {
        List<DoubleFlowEdge>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        return g;
    }

    public static List<DoubleCostFlowEdge>[] createCostFlow(int n) {
        List<DoubleCostFlowEdge>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        return g;
    }

    public static <T extends DoubleFlowEdge> void bfsForFlow(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
        Arrays.fill(dist, 0, g.length, inf);
        dist[s] = 0;
        deque.clear();
        deque.addLast(s);
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            for (T e : g[head]) {
                if (e.flow > 0 && dist[e.to] == inf) {
                    dist[e.to] = dist[head] + 1;
                    deque.addLast(e.to);
                }
            }
        }
    }

    public static String flowToString(List<DoubleFlowEdge>[] g) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            for (DoubleFlowEdge e : g[i]) {
                if (e.real) {
                    builder.append(String.format("%d-[%d]->%d", i, e.flow, e.to));
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }

    public static String costFlowToString(List<DoubleCostFlowEdge>[] g) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            for (DoubleCostFlowEdge e : g[i]) {
                if (e.real) {
                    builder.append(String.format("%d-[%d x %d]->%d", i, e.cost, e.flow, e.to));
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }

    public static <T extends DoubleFlowEdge> void rewind(List<T>[] g) {
        for (int i = 0; i < g.length; i++) {
            for (T e : g[i]) {
                if (e.real) {
                    e.rev.flow += e.flow;
                    e.flow = 0;
                }
            }
        }
    }
}
