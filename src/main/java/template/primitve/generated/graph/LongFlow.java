package template.primitve.generated.graph;


import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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

    public static <T extends LongFlowEdge> void send(T edge, long flow) {
        edge.flow += flow;
        edge.rev.flow -= flow;
    }

    public static LongFlowEdge addEdge(List<LongFlowEdge>[] g, int s, int t, long cap) {
        LongFlowEdge real = new LongFlowEdge(t, 0, true);
        LongFlowEdge virtual = new LongFlowEdge(s, cap, false);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static LongCostFlowEdge addCostEdge(List<LongCostFlowEdge>[] g, int s, int t, long cap, long cost) {
        LongCostFlowEdge real = new LongCostFlowEdge(t, 0, true, cost);
        LongCostFlowEdge virtual = new LongCostFlowEdge(s, cap, false, -cost);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static LongLRFlowEdge addLREdge(List<LongLRFlowEdge>[] g, int s, int t, long cap, long low) {
        LongLRFlowEdge real = new LongLRFlowEdge(t, 0, true, low);
        LongLRFlowEdge virtual = new LongLRFlowEdge(s, cap - low, false, low);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static List<LongLRFlowEdge>[] createLRFlow(int n) {
        return createGraph(n);
    }

    public static List<LongFlowEdge>[] createFlow(int n) {
        return createGraph(n);
    }

    public static List<LongCostFlowEdge>[] createCostFlow(int n) {
        return createGraph(n);
    }

    /**
     * find feasible flow with specified source and sink point or return false when it doesn't exist
     */
    public static boolean feasibleFlow(List<LongLRFlowEdge>[] g, int s, int t, LongMaximumFlow mf) {
        addLREdge(g, t, s, Long.MAX_VALUE / 4, 0);
        boolean ans = feasibleFlow(g, mf);
        g[s].remove(g[s].size() - 1);
        g[t].remove(g[t].size() - 1);
        return ans;
    }

    /**
     * find feasible flow without source and sink point or return false when it doesn't exist
     *
     * @param g
     * @return
     */
    public static boolean feasibleFlow(List<LongLRFlowEdge>[] g, LongMaximumFlow mf) {
        int n = g.length;
        List<LongFlowEdge>[] expand = expand(g, n + 2);
        int src = n;
        int dst = n + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < expand[i].size(); j++) {
                LongFlowEdge fe = expand[i].get(j);
                if (fe.to == src || fe.to == dst ||
                        !fe.real) {
                    continue;
                }
                LongLRFlowEdge e = (LongLRFlowEdge) fe;
                addEdge(expand, src, e.to, e.low);
                addEdge(expand, i, dst, e.low);
            }
        }

        mf.apply(expand, src, dst, Long.MAX_VALUE / 4);

        boolean ans = true;
        for (LongFlowEdge e : expand[src]) {
            ans = ans && DigitUtils.equal(e.rev.flow, 0);
        }
        for (LongFlowEdge e : expand[dst]) {
            ans = ans && DigitUtils.equal(e.flow, 0);
        }

        for (int i = 0; i < n; i++) {
            while (g[i].size() > 0) {
                LongFlowEdge tail = g[i].get(g[i].size() - 1);
                if (tail.to == src || tail.to == dst) {
                    g[i].remove(g[i].size() - 1);
                } else {
                    break;
                }
            }
        }

        return ans;
    }

    private static List[] expand(List[] g, int n) {
        List[] ans = Arrays.copyOf(g, n);
        for (int i = g.length; i < n; i++) {
            ans[i] = new ArrayList();
        }
        return ans;
    }

    private static List[] createGraph(int n) {
        return IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
    }

    public static <T extends LongFlowEdge> void bfsForFlow(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
        Arrays.fill(dist, 0, g.length, inf);
        dist[s] = 0;
        deque.clear();
        deque.addLast(s);
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            for (T e : g[head]) {
                if (!DigitUtils.equal(e.flow, 0) && dist[e.to] == inf) {
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
