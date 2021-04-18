package on2021_03.on2021_03_27_Codeforces___Codeforces_Round__232__Div__1_.C__On_Changing_Tree;



import template.datastructure.ModBIT;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongBITExt;
import template.utils.Debug;

import java.util.List;

public class COnChangingTree {
    List<DirectedEdge>[] g;
    int[] depth;
    int[] open;
    int[] close;


    int order = 1;

    public void dfs(int root, int d) {
        depth[root] = d;
        open[root] = order++;
        for (DirectedEdge e : g[root]) {
            dfs(e.to, d + 1);
        }
        close[root] = order - 1;
    }

    int mod = 1000000007;
    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        g = Graph.createGraph(n);
        for (int i = 1; i < n; i++) {
            int p = in.ri() - 1;
            Graph.addEdge(g, p, i);
        }
        depth = new int[n];
        open = new int[n];
        close = new int[n];
        dfs(0, 0);
        int q = in.ri();
        LongBITExt aBit = new LongBITExt(n);
        LongBITExt bBit = new LongBITExt(n);

        debug.debugArray("depth", depth);
        debug.debugArray("open", open);
        debug.debugArray("close", close);
        for (int i = 0; i < q; i++) {
            debug.debug("i", i);
            debug.debug("aBit", aBit);
            debug.debug("bBit", bBit);
            int t = in.ri();
            if (t == 1) {
                int v = in.ri() - 1;
                long x = in.ri();
                long k = in.ri();
                x += depth[v] * k;
                x %= mod;
                aBit.update(open[v], close[v], -k);
                bBit.update(open[v], close[v], x);
            } else {
                int v = in.ri() - 1;
                long ans = aBit.query(open[v], open[v]) % mod * depth[v] + bBit.query(open[v], open[v]);
                ans = DigitUtils.mod(ans, mod);
                out.println(ans);
            }
        }
    }
}
