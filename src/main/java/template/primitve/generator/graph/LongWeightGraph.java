package template.primitve.generated.graph;

import template.graph.LongWeightDirectedEdge;
import template.graph.LongWeightUndirectedEdge;
import template.primitve.generated.IntegerDeque;
import template.primitve.generated.IntegerDequeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class LongWeightGraph {
    public static void addEdge(List<LongWeightDirectedEdge>[] g, int s, int t, long w) {
        g[s].add(new LongWeightDirectedEdge(t, w));
    }

    public static void addUndirectedEdge(List<LongWeightUndirectedEdge>[] g, int s, int t, long w) {
        LongWeightUndirectedEdge toT = new LongWeightUndirectedEdge(t, w);
        LongWeightUndirectedEdge toS = new LongWeightUndirectedEdge(s, w);
        toS.rev = toT;
        toT.rev = toS;
        g[s].add(toT);
        g[t].add(toS);
    }

    public static List<LongWeightDirectedEdge>[] createDirectedGraph(int n) {
        List<LongWeightDirectedEdge>[] ans = new List[n];
        for (int i = 0; i < n; i++) {
            ans[i] = new ArrayList<>();
        }
        return ans;
    }

    public static List<LongWeightUndirectedEdge>[] createUndirectedGraph(int n) {
        List<LongWeightUndirectedEdge>[] ans = new List[n];
        for (int i = 0; i < n; i++) {
            ans[i] = new ArrayList<>();
        }
        return ans;
    }

    public static class LongDijkstraNode implements Comparable<LongDijkstraNode> {
        int node;
        long dist;

        @Override
        public int compareTo(LongDijkstraNode o) {
            return dist == o.dist ? Integer.compare(node, o.node) : Long.compare(dist, o.dist);
        }
    }

    public static <T extends LongWeightDirectedEdge> void dijkstraElogV(List<T>[] g, int s, long[] dists, long inf) {
        int n = g.length;
        LongDijkstraNode[] nodes = new LongDijkstraNode[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new LongDijkstraNode();
            nodes[i].dist = inf;
            nodes[i].node = i;
        }
        nodes[s].dist = 0;
        TreeSet<LongDijkstraNode> set = new TreeSet<>();
        set.add(nodes[s]);
        while (!set.isEmpty()) {
            LongDijkstraNode head = set.pollFirst();
            for (LongWeightDirectedEdge e : g[head.node]) {
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

    public static <T extends LongWeightDirectedEdge> void dijkstraV2(List<T>[] g, int s, long[] dists, long inf) {
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
            for (LongWeightDirectedEdge e : g[head]) {
                dists[e.to] = Math.min(dists[e.to], dists[head] + e.weight);
            }
        }
    }

    public static <T extends LongWeightDirectedEdge> void spfa(List<T>[] g, int s, long[] dists, long inf) {
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
            for (LongWeightDirectedEdge e : g[head]) {
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
    public static <T extends LongWeightDirectedEdge> boolean spfaWithPossibleNegativeCircle(List<T>[] g, int s, long[] dists, long inf) {
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

            for (LongWeightDirectedEdge e : g[head]) {
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
