package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class DOrientation {
    List<Edge>[] g;
    boolean[] visited;
    int[] depth;

    public void dfs(int root, Edge p, int d) {
        visited[root] = true;
        depth[root] = d;
        for (Edge e : g[root]) {
            if (p == e) {
                continue;
            }
            int to = e.a == root ? e.b : e.a;
            if (visited[to]) {
                if (depth[root] > depth[to]) {
                    e.fromA = root == e.a;
                }
            } else {
                e.fromA = root == e.a;
                dfs(to, e, d + 1);
            }
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        visited = new boolean[n];
        depth = new int[n];
        g = Graph.createGraph(n);
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge(in.ri() - 1, in.ri() - 1);
        }
        int[] c = in.ri(n);
        for (Edge e : edges) {
            if (c[e.a] == c[e.b]) {
                g[e.a].add(e);
                g[e.b].add(e);
            } else if (c[e.a] > c[e.b]) {
                e.fromA = true;
            }
        }
        for (int i = 0; i < n; i++) {
            if (visited[i]) {
                continue;
            }
            dfs(i, null, 0);
        }
        for (Edge e : edges) {
            if (e.fromA) {
                out.println("->");
            }else{
                out.println("<-");
            }
        }
    }
}

class Edge {
    int a;
    int b;

    public Edge(int a, int b) {
        this.a = a;
        this.b = b;
    }

    boolean fromA;
}
