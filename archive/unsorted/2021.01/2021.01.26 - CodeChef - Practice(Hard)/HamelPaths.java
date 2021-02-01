package contest;

import template.algo.GraphicMatroidPartition;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class HamelPaths {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = new int[m];
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            a[i] = in.ri() - 1;
            b[i] = in.ri() - 1;
        }
        GraphicMatroidPartition partition = new GraphicMatroidPartition();
        int[] type = partition.solve(n, 2, a, b);
        Edge[] edges = new Edge[m];
        g = Graph.createGraph(n);
        deg = new int[n];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = a[i];
            edges[i].b = b[i];
            edges[i].index = i;
            if (type[i] != -1) {
                deg[edges[i].a]++;
                deg[edges[i].b]++;
            }
            if (type[i] == 1) {
                g[a[i]].add(edges[i]);
                g[b[i]].add(edges[i]);
            }
        }
        dfs(0, null);
        seq = new ArrayList<>(m);
        for(int i = 0; i < m; i++){
            if(type[i] == 0){
                g[a[i]].add(edges[i]);
                g[b[i]].add(edges[i]);
            }
        }
        euler(0, null);
        out.append(seq.size()).append(' ');
        for(Edge e : seq){
            out.append(e.index + 1).append(' ');
        }
        out.println();
    }

    void euler(int root, Edge e) {
        while (!g[root].isEmpty()) {
            Edge back = CollectionUtils.pop(g[root]);
            if (!back.enable) {
                continue;
            }
            back.enable = false;
            euler(back.other(root), back);
        }
        if (e != null) {
            seq.add(e);
        }
    }

    void reverse(Edge e) {
        e.enable = !e.enable;
        deg[e.a] ^= 1;
        deg[e.b] ^= 1;
    }

    void dfs(int root, Edge p) {
        for (Edge e : g[root]) {
            if (e == p) {
                continue;
            }
            int node = e.other(root);
            dfs(node, e);
        }
        if (deg[root] % 2 == 1) {
            reverse(p);
        }
    }

    List<Edge>[] g;
    int[] deg;
    List<Edge> seq;
}

class Edge {
    int a;
    int b;
    int index;
    boolean enable = true;

    int other(int x) {
        return a == x ? b : a;
    }
}
