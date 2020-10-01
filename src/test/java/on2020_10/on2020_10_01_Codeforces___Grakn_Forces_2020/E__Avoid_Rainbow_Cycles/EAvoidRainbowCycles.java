package on2020_10.on2020_10_01_Codeforces___Grakn_Forces_2020.E__Avoid_Rainbow_Cycles;



import template.datastructure.DSU;
import template.io.FastInput;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EAvoidRainbowCycles {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int m = in.readInt();
        int n = in.readInt();

        int[] a = new int[m];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);

        int g = n + m;

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int s = in.readInt();
            for (int j = 0; j < s; j++) {
                int x = in.readInt() - 1;
                edges.add(new Edge(x, n + i, a[i] + b[x]));
            }
        }

        edges.sort((x, y) -> -Long.compare(x.cost, y.cost));
        DSU dsu = new DSU(g);
        dsu.reset();

        long ans = edges.stream().mapToLong(x -> x.cost).sum();
        for (Edge e : edges) {
            if (dsu.find(e.a) != dsu.find(e.b)) {
                dsu.merge(e.a, e.b);
                ans -= e.cost;
            }
        }

        out.println(ans);
    }
}

class Edge {
    int a;
    int b;
    long cost;

    public Edge(int a, int b, long cost) {
        this.a = a;
        this.b = b;
        this.cost = cost;
    }
}
