package template.primitve.generated.graph;


import template.primitve.generated.datastructure.IntegerDeque;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongFlow {
    /**
     * Return each vertex belong to s(true) or t
     */
    public static void findSetST(List<LongFlowEdge>[] g, int s, boolean[] visited) {
        Arrays.fill(visited, false);
        dfs(g, visited, s);
    }

    /**
     * Return each vertex belong to s(true) or t
     */
    public static boolean[] findSetST(List<LongFlowEdge>[] g, int s) {
        boolean[] visited = new boolean[g.length];
        dfs(g, visited, s);
        return visited;
    }

    private static void dfs(List<LongFlowEdge>[] g, boolean[] visited, int root) {
        if (visited[root]) {
            return;
        }
        visited[root] = true;
        for (LongFlowEdge e : g[root]) {
            if (e.rev.flow > 0) {
                dfs(g, visited, e.to);
            }
        }
    }

    public static interface MinCutEdgeConsumer {
        void accept(int s, LongFlowEdge edge);
    }

    /**
     * Find the minimum cut
     *
     * @param g
     * @param belong returned by {@link #findSetST(List[], int)}
     * @return
     */
    public static void findMinimumCut(List<LongFlowEdge>[] g, boolean[] belong,
                                      MinCutEdgeConsumer consumer) {
        int n = g.length;
        for (int i = 0; i < n; i++) {
            if (!belong[i]) {
                continue;
            }
            for (LongFlowEdge e : g[i]) {
                if (e.real && !belong[e.to]) {
                    consumer.accept(i, e);
                }
            }
        }
    }

    public static <T extends LongFlowEdge> void send(List<T>[] g, T edge, long flow) {
        edge.flow += flow;
        edge.rev.flow -= flow;
    }

    public static LongFlowEdge addEdge(List<LongFlowEdge>[] g, int s, int t, long cap) {
        LongFlowEdge real = new LongFlowEdge(t,0, true);
        LongFlowEdge virtual = new LongFlowEdge(s, cap, false);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static LongCostFlowEdge addEdge(List<LongCostFlowEdge>[] g, int s, int t, long cap, long cost) {
        LongCostFlowEdge real = new LongCostFlowEdge(t,0, true, cost);
        LongCostFlowEdge virtual = new LongCostFlowEdge(s, cap, false, cost);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static List<LongFlowEdge>[] createFlow(int n) {
        List<LongFlowEdge>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        return g;
    }

    public static List<LongCostFlowEdge>[] createCostFlow(int n) {
        List<LongCostFlowEdge>[] g = new List[n];
        for (int i = 0; i < n; i++) {
            g[i] = new ArrayList<>();
        }
        return g;
    }

    public static <T extends LongFlowEdge> void bfsForFlow(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
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

    public static String flowToString(List<LongFlowEdge>[] g) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            for (LongFlowEdge e : g[i]) {
                if (e.real) {
                    builder.append(String.format("%d-[%d]->%d", i, e.flow, e.to));
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }

    public static String costFlowToString(List<LongCostFlowEdge>[] g) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            for (LongCostFlowEdge e : g[i]) {
                if (e.real) {
                    builder.append(String.format("%d-[%d x %d]->%d", i, e.cost, e.flow, e.to));
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }

    public static <T extends LongFlowEdge> void rewind(List<T>[] g) {
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
