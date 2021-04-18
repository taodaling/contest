package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__225__Div__1_.C__Propagating_tree;



import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.LongBITExt;
import template.utils.Pair;

import java.util.List;

public class CPropagatingTree {
    List<UndirectedEdge>[] g;
    int[] depth;
    int[] open;
    int[] close;
    int order = 1;

    public void dfs(int root, int p) {
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        open[root] = order++;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
        close[root] = order - 1;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        open = new int[n];
        close = new int[n];
        depth = new int[n];
        dfs(0, -1);
        LongBITExt[] bit = new LongBITExt[2];
        for (int i = 0; i < 2; i++) {
            bit[i] = new LongBITExt(n);
        }

        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int x = in.ri() - 1;
            if (t == 1) {
                int val = in.ri();
                bit[depth[x] & 1].update(open[x], close[x], val);
            } else {
                long ans = a[x] + bit[depth[x] & 1].query(open[x], open[x]) - bit[(depth[x] & 1) ^ 1].query(open[x], open[x]);
                out.println(ans);
            }
        }
    }
}
