package contest;

import template.datastructure.RangeAffineRangeSum;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;

import java.util.List;

public class FPaintTree {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);
    List<UndirectedEdge>[] g;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }

        int[] depth = new int[n];
        dfs(0, -1, depth);
        int end1 = findFarthest(depth);
        dfs(end1, -1, depth);
        int end2 = findFarthest(depth);
        int diameter = depth[end2];


        int[] a = new int[n];
        int[] b = new int[n];
        dfs(end1, -1, a);
        dfs(end2, -1, b);

        RangeAffineRangeSum.mod = mod;
        RangeAffineRangeSum st = new RangeAffineRangeSum(0, diameter);
        st.init(0, diameter, i -> 0);
        st.update(0, 0, 0, diameter, 0, 1);
        for (int i = 0; i < n; i++) {
            if (i == end1 || i == end2) {
                continue;
            }
            int x = a[i];
            int y = b[i];
            if (x > y) {
                int tmp = x;
                x = y;
                y = tmp;
            }
            int sx = st.query(0, x, 0, diameter);
            int sy = st.query(0, y, 0, diameter);
            st.update(0, x, 0, diameter, 0, 0);
            st.update(x, x, 0, diameter, 0, sx);
            st.update(y, y, 0, diameter, 1, sy);
            st.update(y + 1, diameter, 0, diameter, 2, 0);
        }

        long ans = 0;
        for (int i = 0; i <= diameter; i++) {
            long way = st.query(i, i, 0, diameter);
            ans += way * i % mod;
        }
        ans += (long) pow.pow(2, n - 2) * diameter % mod;
        ans = ans * 2 % mod;
        out.println(ans);
    }

    public int findFarthest(int[] d) {
        int ans = 0;
        for (int i = 1; i < d.length; i++) {
            if (d[ans] < d[i]) {
                ans = i;
            }
        }
        return ans;
    }

    public void dfs(int root, int p, int[] d) {
        d[root] = p == -1 ? 0 : d[p] + 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, d);
        }
    }
}
