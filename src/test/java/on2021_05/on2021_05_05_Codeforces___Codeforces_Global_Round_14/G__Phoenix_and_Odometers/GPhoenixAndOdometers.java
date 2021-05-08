package on2021_05.on2021_05_05_Codeforces___Codeforces_Global_Round_14.G__Phoenix_and_Odometers;



import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.GCDs;
import template.primitve.generated.graph.IntegerWeightDirectedEdge;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.utils.Debug;

import java.util.Arrays;
import java.util.List;

public class GPhoenixAndOdometers {
    Debug debug = new Debug(true);
    List<IntegerWeightDirectedEdge>[] g;

    DirectedTarjanSCC scc;
    long[] D;

    void dfs(int root, long d) {
        if (D[root] != 0) {
            return;
        }
        D[root] = d;
        for (IntegerWeightDirectedEdge e : g[root]) {
            if (scc.set[root] != scc.set[e.to]) {
                continue;
            }
            dfs(e.to, d + e.weight);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int w = in.ri();
            IntegerWeightGraph.addEdge(g, a, b, w);
        }
        scc = new DirectedTarjanSCC(n);
        scc.init(g);
        D = new long[n];
        Arrays.fill(D, 0);
        long[] G = new long[n];
        for (int i = 0; i < n; i++) {
            if (scc.set[i] != i) {
                continue;
            }
            dfs(i, 0);
            G[i] = D[i];
        }

        for (int i = 0; i < n; i++) {
            int set = scc.set[i];
            for (IntegerWeightDirectedEdge e : g[i]) {
                int to = e.to;
                if (scc.set[i] != scc.set[to]) {
                    continue;
                }
                G[set] = GCDs.gcd(G[set], Math.abs(D[i] + e.weight - D[e.to]));
            }
        }

        debug.debug("G", G);
        String yes = "YES";
        String no = "NO";
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            int v = in.ri() - 1;
            long s = in.rl();
            long t = in.rl();
            long target = t - s;
            long circle = G[scc.set[v]];
            if (GCDs.gcd(target, t) % GCDs.gcd(circle, t) == 0) {
                out.println(yes);
            } else {
                out.println(no);
            }
        }
    }
}
