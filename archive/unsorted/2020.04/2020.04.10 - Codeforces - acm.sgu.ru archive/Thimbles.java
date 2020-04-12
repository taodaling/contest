package contest;

import template.datastructure.DSU;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.graph.UndirectedOneWeightMinCircle;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.List;

public class Thimbles {
    List<UndirectedEdge>[] g;
    int[] depths;
    boolean[] circle;
    Debug debug = new Debug(true);
    int[] parent;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        depths = new int[n];
        parent = new int[n];
        Arrays.fill(depths, -1);
        g = Graph.createUndirectedGraph(n);
        int[][] edges = new int[m][2];
        boolean[] added = new boolean[m];
        circle = new boolean[n];
        DSU dsu = new DSU(n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges[i][0] = a;
            edges[i][1] = b;
            if (dsu.find(a) == dsu.find(b)) {
                continue;
            }
            added[i] = true;
            dsu.merge(a, b);
            Graph.addUndirectedEdge(g, a, b);
        }

        debug.debug("g", g);
        LcaOnTree lot = new LcaOnTree(g, 0);
        dfs(0, -1, 0);

        int longCircle = 0;
        int smallCircle = 0;
        int[] deg = new int[n];
        for (int i = 0; i < m; i++) {
            if (edges[i][0] == 0) {
                deg[edges[i][1]]++;
            }
        }
        for (int i = 0; i < n; i++) {
            if (deg[i] >= 2) {
                smallCircle++;
            }
        }

        for (int i = 0; i < m; i++) {
            if (added[i]) {
                continue;
            }
            int a = edges[i][0];
            int b = edges[i][1];
            int lca = lot.lca(a, b);
            if (lca == 0 && depths[a] + depths[b] > 1) {
                longCircle++;
            } else if (lca != 0 && depths[a] != -1) {
                up(a, lca);
                up(b, lca);
            }
        }

        boolean[] ans = new boolean[n];
        for (int i = 1; i < n; i++) {
            if (depths[i] == -1) {
                continue;
            }
            if (depths[i] >= 2 || longCircle > 0 || smallCircle >= 2 || deg[i] % 2 == 1 || circle[i]) {
                ans[i] = true;
            }
        }
        if (longCircle > 0 || smallCircle >= 2) {
            ans[0] = true;
        }
        if (g[0].isEmpty()) {
            ans[0] = true;
        }
        for (int i = 0; i < n; i++) {
            if (deg[i] >= 2) {
                if (deg[i] % 2 == 0 || circle[i]) {
                    ans[0] = true;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (ans[i]) {
                out.append(i + 1).append(' ');
            }
        }
    }

    public void up(int root, int target) {
        circle[root] = true;
        if (root != target) {
            up(parent[root], target);
        }
    }

    public void dfs(int root, int p, int d) {
        depths[root] = d;
        parent[root] = p;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, d + 1);
        }
    }
}
