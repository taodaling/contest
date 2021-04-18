package template.graph;

import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Graph {
    public static <T extends DirectedEdge> void bfs(List<T>[] g, int s, int[] dist, int inf, IntegerDeque deque) {
        Arrays.fill(dist, inf);
        dist[s] = 0;
        deque.clear();
        deque.addLast(s);
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            for (T e : g[head]) {
                if (dist[e.to] == inf) {
                    dist[e.to] = dist[head] + 1;
                    deque.addLast(e.to);
                }
            }
        }
    }

    public static <T extends DirectedEdge> void multiSourceBfs(List<T>[] g, IntegerArrayList sources, int[] dist, int inf, IntegerDeque deque) {
        Arrays.fill(dist, inf);
        deque.clear();
        for (int i = 0; i < sources.size(); i++) {
            int v = sources.get(i);
            dist[v] = 0;
            deque.addLast(v);
        }
        while (!deque.isEmpty()) {
            int head = deque.removeFirst();
            for (T e : g[head]) {
                if (dist[e.to] == inf) {
                    dist[e.to] = dist[head] + 1;
                    deque.addLast(e.to);
                }
            }
        }
    }

    public static DirectedEdge addEdge(List<DirectedEdge>[] g, int s, int t) {
        DirectedEdge e = new DirectedEdge(t);
        g[s].add(e);
        return e;
    }

    public static UndirectedEdge addUndirectedEdge(List<UndirectedEdge>[] g, int s, int t) {
        UndirectedEdge toT = new UndirectedEdge(t);
        UndirectedEdge toS = new UndirectedEdge(s);
        toT.rev = toS;
        toS.rev = toT;
        g[s].add(toT);
        g[t].add(toS);
        return toT;
    }

    public static List[] createGraph(int n) {
        return IntStream.range(0, n).mapToObj(i -> new ArrayList<>()).toArray(i -> new List[i]);
    }

    public static int[][] createGraph(int n, int m, int[] u, int[] v) {
        int[] degs = new int[n];
        for (int i = 0; i < m; i++) {
            degs[u[i]]++;
        }
        int[][] g = new int[n][];
        for (int i = 0; i < n; i++) {
            g[i] = new int[degs[i]];
        }
        for (int i = 0; i < m; i++) {
            int a = u[i];
            int b = v[i];
            g[a][--degs[a]] = b;
        }
        return g;
    }

    public static int[][] createUndirectedGraph(int n, int m, int[] u, int[] v) {
        int[] degs = new int[n];
        for (int i = 0; i < m; i++) {
            degs[u[i]]++;
            degs[v[i]]++;
        }
        int[][] g = new int[n][];
        for (int i = 0; i < n; i++) {
            g[i] = new int[degs[i]];
        }
        for (int i = 0; i < m; i++) {
            int a = u[i];
            int b = v[i];
            g[a][--degs[a]] = b;
            g[b][--degs[b]] = a;
        }
        return g;
    }
}
