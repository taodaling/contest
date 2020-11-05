package template.primitve.generated.graph;


import template.math.DigitUtils;
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
            if (!DigitUtils.equal(e.rev.flow, 0)) {
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

    public static DoubleFlowEdge addFlowEdge(List[] g, int s, int t, double cap) {
        DoubleFlowEdge real = new DoubleFlowEdge(t, 0, true);
        DoubleFlowEdge virtual = new DoubleFlowEdge(s, cap, false);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static DoubleCostFlowEdge addCostFlowEdge(List[] g, int s, int t, double cap, double cost) {
        DoubleCostFlowEdge real = new DoubleCostFlowEdge(t, 0, true, cost);
        DoubleCostFlowEdge virtual = new DoubleCostFlowEdge(s, cap, false, -cost);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static DoubleLRFlowEdge addLRFlowEdge(List[] g, int s, int t, double cap, double low) {
        DoubleLRFlowEdge real = new DoubleLRFlowEdge(t, 0, true, low);
        DoubleLRFlowEdge virtual = new DoubleLRFlowEdge(s, cap - low, false, low);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static DoubleLRCostFlowEdge addLRCostFlowEdge(List[] g, int s, int t, double cap, double cost, double low) {
        DoubleLRCostFlowEdge real = new DoubleLRCostFlowEdge(t, 0, true, cost, low);
        DoubleLRCostFlowEdge virtual = new DoubleLRCostFlowEdge(s, cap - low, false, -cost, low);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    /**
     * find feasible flow with specified source and sink point or return false when it doesn't exist
     */
    public static <T extends DoubleFlowEdge & DoubleLowBound> boolean feasibleFlow(List<T>[] g, int s, int t, DoubleMaximumFlow mf) {
        addLRFlowEdge(g, t, s, Double.MAX_VALUE / 4, 0);
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
    public static <T extends DoubleFlowEdge & DoubleLowBound> boolean feasibleFlow(List<T>[] g, DoubleMaximumFlow mf) {
        int n = g.length;
        List<T>[] expand = expand(g, n + 2);
        int src = n;
        int dst = n + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < expand[i].size(); j++) {
                T fe = expand[i].get(j);
                if (fe.to == src || fe.to == dst ||
                        !fe.real) {
                    continue;
                }
                T e = fe;
                if (e.low() > 0) {
                    addFlowEdge(expand, src, e.to, e.low());
                    addFlowEdge(expand, i, dst, e.low());
                }
            }
        }

        mf.apply((List[]) expand, src, dst, Double.MAX_VALUE / 4);

        boolean ans = true;
        for (T e : expand[src]) {
            ans = ans && DigitUtils.equal(e.rev.flow, 0);
        }
        for (T e : expand[dst]) {
            ans = ans && DigitUtils.equal(e.flow, 0);
        }

        for (int i = 0; i < n; i++) {
            while (g[i].size() > 0) {
                T tail = g[i].get(g[i].size() - 1);
                if (tail.to == src || tail.to == dst) {
                    g[i].remove(g[i].size() - 1);
                } else {
                    break;
                }
            }
        }

        return ans;
    }

    /**
     * find feasible flow with specified source and sink point or return false when it doesn't exist
     */
    public static <T extends DoubleCostFlowEdge & DoubleLowBound> boolean feasibleMinCostFlow(List<T>[] g, int s, int t, DoubleMinimumCostFlow mf) {
        addLRCostFlowEdge(g, t, s, Double.MAX_VALUE / 4, 0, 0);
        boolean ans = feasibleMinCostFlow(g, mf);
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
    public static <T extends DoubleCostFlowEdge & DoubleLowBound> boolean feasibleMinCostFlow(List<T>[] g, DoubleMinimumCostFlow mf) {
        int n = g.length;
        List<T>[] expand = expand(g, n + 2);
        int src = n;
        int dst = n + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < expand[i].size(); j++) {
                T fe = expand[i].get(j);
                if (fe.to == src || fe.to == dst ||
                        !fe.real) {
                    continue;
                }
                T e = fe;
                if (e.low() > 0) {
                    addCostFlowEdge(expand, src, e.to, e.low(), 0);
                    addCostFlowEdge(expand, i, dst, e.low(), 0);
                }
            }
        }

        double[] flow = mf.apply((List[]) expand, src, dst, Double.MAX_VALUE / 4);

        boolean ans = true;
        for (T e : expand[src]) {
            ans = ans && DigitUtils.equal(e.rev.flow, 0);
        }
        for (T e : expand[dst]) {
            ans = ans && DigitUtils.equal(e.flow, 0);
        }

        for (int i = 0; i < n; i++) {
            while (g[i].size() > 0) {
                T tail = g[i].get(g[i].size() - 1);
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


    public static <T extends DoubleFlowEdge> void bfsForFlow(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
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

    public static <T extends DoubleCostFlowEdge> String costFlowToString(List<T>[] g) {
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
