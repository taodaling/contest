package template.primitve.generated.graph;


import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerDeque;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntegerFlow {
    /**
     * Return each vertex beint to s(true) or t
     */
    public static void findSetST(List<IntegerFlowEdge>[] g, int s, boolean[] visited) {
        Arrays.fill(visited, false);
        dfs(g, visited, s);
    }

    /**
     * Return each vertex beint to s(true) or t
     */
    public static boolean[] findSetST(List<IntegerFlowEdge>[] g, int s) {
        boolean[] visited = new boolean[g.length];
        dfs(g, visited, s);
        return visited;
    }

    public static int[] costFlowSummary(List<IntegerCostFlowEdge>[] g, int s){
        int cost = 0;
        for(List<IntegerCostFlowEdge> adj : g){
            for(IntegerCostFlowEdge e : adj){
                if(e.real){
                    cost += e.flow * e.cost;
                }
            }
        }
        int flow = 0;
        for(IntegerCostFlowEdge e : g[s]){
            if(e.real){
                flow += e.flow;
            }
        }
        return new int[]{flow, cost};
    }

    public static int flowSummary(List<IntegerFlowEdge>[] g, int s){
        int flow = 0;
        for(IntegerFlowEdge e : g[s]){
            if(e.real){
                flow += e.flow;
            }
        }
        return flow;
    }

    private static void dfs(List<IntegerFlowEdge>[] g, boolean[] visited, int root) {
        if (visited[root]) {
            return;
        }
        visited[root] = true;
        for (IntegerFlowEdge e : g[root]) {
            if (!DigitUtils.equal(e.rev.flow, 0)) {
                dfs(g, visited, e.to);
            }
        }
    }

    public static interface MinCutEdgeConsumer {
        void accept(int s, IntegerFlowEdge edge);
    }

    /**
     * Find the minimum cut
     *
     * @param g
     * @param beint returned by {@link #findSetST(List[], int)}
     * @return
     */
    public static void findMinimumCut(List<IntegerFlowEdge>[] g, boolean[] beint,
                                      MinCutEdgeConsumer consumer) {
        int n = g.length;
        for (int i = 0; i < n; i++) {
            if (!beint[i]) {
                continue;
            }
            for (IntegerFlowEdge e : g[i]) {
                if (e.real && !beint[e.to]) {
                    consumer.accept(i, e);
                }
            }
        }
    }

    public static <T extends IntegerFlowEdge> void send(T edge, int flow) {
        edge.flow += flow;
        edge.rev.flow -= flow;
    }

    public static IntegerFlowEdge addFlowEdge(List[] g, int s, int t, int cap) {
        IntegerFlowEdge real = new IntegerFlowEdge(t, 0, true);
        IntegerFlowEdge virtual = new IntegerFlowEdge(s, cap, false);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static IntegerCostFlowEdge addCostFlowEdge(List[] g, int s, int t, int cap, int cost) {
        IntegerCostFlowEdge real = new IntegerCostFlowEdge(t, 0, true, cost);
        IntegerCostFlowEdge virtual = new IntegerCostFlowEdge(s, cap, false, -cost);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static IntegerLRFlowEdge addLRFlowEdge(List[] g, int s, int t, int cap, int low) {
        IntegerLRFlowEdge real = new IntegerLRFlowEdge(t, 0, true, low);
        IntegerLRFlowEdge virtual = new IntegerLRFlowEdge(s, cap - low, false, low);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    public static IntegerLRCostFlowEdge addLRCostFlowEdge(List[] g, int s, int t, int cap, int cost, int low) {
        IntegerLRCostFlowEdge real = new IntegerLRCostFlowEdge(t, 0, true, cost, low);
        IntegerLRCostFlowEdge virtual = new IntegerLRCostFlowEdge(s, cap - low, false, -cost, low);
        real.rev = virtual;
        virtual.rev = real;
        g[s].add(real);
        g[t].add(virtual);
        return real;
    }

    /**
     * find feasible flow with specified source and sink point or return false when it doesn't exist
     */
    public static <T extends IntegerFlowEdge & IntegerLowBound> boolean feasibleFlow(List<T>[] g, int s, int t, IntegerMaximumFlow mf) {
        addLRFlowEdge(g, t, s, Integer.MAX_VALUE / 4, 0);
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
    public static <T extends IntegerFlowEdge & IntegerLowBound> boolean feasibleFlow(List<T>[] g, IntegerMaximumFlow mf) {
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

        mf.apply((List[]) expand, src, dst, Integer.MAX_VALUE / 4);

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
     *
     * Minimize the cost of the feasible flow instead of maximize the flow
     */
    public static <T extends IntegerCostFlowEdge & IntegerLowBound> boolean feasibleMinCostFlow(List<T>[] g, int s, int t, IntegerMinimumCostFlow mf) {
        addLRCostFlowEdge(g, t, s, Integer.MAX_VALUE / 4, 0, 0);
        boolean ans = feasibleMinCostFlow(g, mf);
        g[s].remove(g[s].size() - 1);
        g[t].remove(g[t].size() - 1);
        return ans;
    }

    /**
     * find feasible flow without source and sink point or return false when it doesn't exist
     *
     * Minimize the cost of the feasible flow instead of maximize the flow
     *
     * @param g
     * @return
     */
    public static <T extends IntegerCostFlowEdge & IntegerLowBound> boolean feasibleMinCostFlow(List<T>[] g, IntegerMinimumCostFlow mf) {
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

        int[] flow = mf.apply((List[]) expand, src, dst, Integer.MAX_VALUE / 4);

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


    public static <T extends IntegerFlowEdge> void bfsForFlow(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
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

    public static String flowToString(List<IntegerFlowEdge>[] g) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            for (IntegerFlowEdge e : g[i]) {
                if (e.real) {
                    builder.append(String.format("%d-[%d]->%d", i, e.flow, e.to));
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }

    public static <T extends IntegerCostFlowEdge> String costFlowToString(List<T>[] g) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < g.length; i++) {
            for (IntegerCostFlowEdge e : g[i]) {
                if (e.real) {
                    builder.append(String.format("%d-[%d x %d]->%d", i, e.cost, e.flow, e.to));
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }

    public static <T extends IntegerFlowEdge> void rewind(List<T>[] g) {
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
