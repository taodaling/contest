package on2021_05.on2021_05_06_Codeforces___Codeforces_Global_Round_14.F__Phoenix_and_Earthquake;



import template.datastructure.DSU;
import template.datastructure.LinkedListBeta;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FPhoenixAndEarthquake {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int x = in.ri();
        DSUExt dsu = new DSUExt(n);
        dsu.a = in.rl(n);
        long sum = Arrays.stream(dsu.a).sum();
        if (sum < (long) (n - 1) * x) {
            out.println("NO");
            return;
        }

        out.println("YES");
        Edge[] edges = new Edge[m];
        dsu.init(n);
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = in.ri() - 1;
            edges[i].b = in.ri() - 1;
            edges[i].id = i;
            dsu.adj[edges[i].a].addLast(edges[i]);
            dsu.adj[edges[i].b].addLast(edges[i]);
        }
        List<Edge> pick = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int root;
            while (dsu.a[root = dsu.find(i)] >= x && !dsu.adj[root].isEmpty()) {
                Edge e = dsu.adj[root].removeFirst();
                int u = dsu.find(e.a);
                int v = dsu.find(e.b);
                int node = u ^ v ^ root;
                if (node == root) {
                    continue;
                }
                dsu.a[root] -= x;
                dsu.merge(root, node);
                pick.add(e);
            }
        }

        for (Edge e : edges) {
            if (dsu.find(e.a) != dsu.find(e.b)) {
                pick.add(e);
                dsu.merge(e.a, e.b);
            }
        }

        for(Edge e : pick){
            out.append(e.id + 1).append(' ');
        }
    }
}

class Edge {
    int a;
    int b;
    int id;
}

class DSUExt extends DSU {
    LinkedListBeta<Edge>[] adj;
    long[] a;


    public DSUExt(int n) {
        super(n);
        adj = new LinkedListBeta[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new LinkedListBeta<>();
        }
    }

    @Override
    protected void preMerge(int a, int b) {
        super.preMerge(a, b);
        adj[a].migrate(adj[b]);
        this.a[a] += this.a[b];
    }
}
