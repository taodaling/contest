package template.primitve.generated.graph;

import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class IntegerWeightGraph {
    public static void addEdge(List<IntegerWeightDirectedEdge>[] g, int s, int t, int w) {
        g[s].add(new IntegerWeightDirectedEdge(t, w));
    }

    public static void addUndirectedEdge(List<IntegerWeightUndirectedEdge>[] g, int s, int t, int w) {
        IntegerWeightUndirectedEdge toT = new IntegerWeightUndirectedEdge(t, w);
        IntegerWeightUndirectedEdge toS = new IntegerWeightUndirectedEdge(s, w);
        toS.rev = toT;
        toT.rev = toS;
        g[s].add(toT);
        g[t].add(toS);
    }

    public static List<IntegerWeightDirectedEdge>[] createDirectedGraph(int n) {
        List<IntegerWeightDirectedEdge>[] ans = new List[n];
        for (int i = 0; i < n; i++) {
            ans[i] = new ArrayList<>();
        }
        return ans;
    }

    public static List<IntegerWeightUndirectedEdge>[] createUndirectedGraph(int n) {
        List<IntegerWeightUndirectedEdge>[] ans = new List[n];
        for (int i = 0; i < n; i++) {
            ans[i] = new ArrayList<>();
        }
        return ans;
    }

    public static class IntegerDijkstraNode implements Comparable<IntegerDijkstraNode> {
        int node;
        int dist;

        @Override
        public int compareTo(IntegerDijkstraNode o) {
            return dist == o.dist ? Integer.compare(node, o.node) : Integer.compare(dist, o.dist);
        }
    }

    public static <T extends IntegerWeightDirectedEdge> void dijkstraElogV(List<T>[] g, int s, int[] dists, int inf) {
        int n = g.length;
        IntegerDijkstraNode[] nodes = new IntegerDijkstraNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new IntegerDijkstraNode();
            nodes[i].dist = inf;
            nodes[i].node = i;
        }
        nodes[s].dist = 0;
        TreeSet<IntegerDijkstraNode> set = new TreeSet<>();
        set.add(nodes[s]);
        while (!set.isEmpty()) {
            IntegerDijkstraNode head = set.pollFirst();
            for (IntegerWeightDirectedEdge e : g[head.node]) {
                if (nodes[e.to].dist > head.dist + e.weight) {
                    set.remove(nodes[e.to]);
                    nodes[e.to].dist = head.dist + e.weight;
                    set.add(nodes[e.to]);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            dists[i] = nodes[i].dist;
        }
    }

    public static <T extends IntegerWeightDirectedEdge> void dijkstraV2(List<T>[] g, int s, int[] dists, int inf) {
        int n = g.length;
        for (int i = 0; i < g.length; i++) {
            dists[i] = inf;
        }
        boolean[] handled = new boolean[n];
        dists[s] = 0;
        for (int i = 0; i < n; i++) {
            int head = -1;
            for (int j = 0; j < n; j++) {
                if (!handled[j] && (head == -1 || dists[head] > dists[j])) {
                    head = j;
                }
            }
            handled[head] = true;
            for (IntegerWeightDirectedEdge e : g[head]) {
                dists[e.to] = Math.min(dists[e.to], dists[head] + e.weight);
            }
        }
    }

    public static <T extends IntegerWeightDirectedEdge> void spfa(List<T>[] g, int s, int[] dists, int inf) {
        int n = g.length;
        for (int i = 0; i < g.length; i++) {
            dists[i] = inf;
        }
        dists[s] = 0;
        IntegerDeque deque = new IntegerDequeImpl(n);
        boolean[] inque = new boolean[n];
        deque.addLast(s);
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            inque[head] = false;
            for (IntegerWeightDirectedEdge e : g[head]) {
                if (dists[e.to] > dists[head] + e.weight) {
                    dists[e.to] = dists[head] + e.weight;
                    if (!inque[e.to]) {
                        inque[e.to] = true;
                        deque.addLast(e.to);
                    }
                }
            }
        }
    }

    /**
     * @return whether exists a negative circle
     */
    public static <T extends IntegerWeightDirectedEdge> boolean spfaWithPossibleNegativeCircle(List<T>[] g, int s, int[] dists, int inf) {
        int n = g.length;
        for (int i = 0; i < g.length; i++) {
            dists[i] = inf;
        }
        dists[s] = 0;
        IntegerDeque deque = new IntegerDequeImpl(n);
        boolean[] inque = new boolean[n];
        deque.addLast(s);
        int[] added = new int[n];
        added[s] = -1;
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            inque[head] = false;
            added[head]++;
            if (added[head] >= n) {
                return true;
            }

            for (IntegerWeightDirectedEdge e : g[head]) {
                if (dists[e.to] > dists[head] + e.weight) {
                    dists[e.to] = dists[head] + e.weight;
                    if (!inque[e.to]) {
                        inque[e.to] = true;
                        deque.addLast(e.to);
                    }
                }
            }
        }
        return false;
    }
}
