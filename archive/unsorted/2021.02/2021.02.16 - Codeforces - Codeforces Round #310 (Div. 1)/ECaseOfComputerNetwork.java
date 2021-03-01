package contest;

import template.datastructure.DSU;
import template.graph.Graph;
import template.graph.KthAncestorOnTreeByBinaryLift;
import template.graph.UndirectedEdge;
import template.graph.UndirectedTarjanSCC;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class ECaseOfComputerNetwork {
    int[] upAdd;
    int[] botAdd;
    int[] p;
    int[] depth;
    List<UndirectedEdge>[] tree;
    boolean[] visited;
    boolean ok = true;

    void dfs(int root, int p) {
        visited[root] = true;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        this.p[root] = p;
        for (UndirectedEdge e : tree[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
    }

    void trySet(int root, int p) {
        visited[root] = true;
        for (UndirectedEdge e : tree[root]) {
            if (e.to == p) {
                continue;
            }
            trySet(e.to, root);
            upAdd[root] += upAdd[e.to];
            botAdd[root] += botAdd[e.to];
        }
        assert upAdd[root] >= 0;
        assert botAdd[root] >= 0;
        if (upAdd[root] > 0 && botAdd[root] > 0) {
            ok = false;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        p = new int[n];
        Arrays.fill(p, -1);
        depth = new int[n];
        visited = new boolean[n];
        upAdd = new int[n];
        botAdd = new int[n];
        tree = Graph.createGraph(n);
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        DSU dsu = new DSU(n);
        dsu.init();
        for (int i = 0; i < m; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            Graph.addUndirectedEdge(g, u, v);
            dsu.merge(u, v);
        }
        UndirectedTarjanSCC scc = new UndirectedTarjanSCC(n);

        scc.init(g);
        for (int i = 0; i < n; i++) {
            for (UndirectedEdge e : g[i]) {
                if (scc.set[e.to] < scc.set[i]) {
                    Graph.addUndirectedEdge(tree, scc.set[i], scc.set[e.to]);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (scc.set[i] != i || visited[i]) {
                continue;
            }
            dfs(i, -1);
        }
        KthAncestorOnTreeByBinaryLift lca = new KthAncestorOnTreeByBinaryLift(n);
        lca.init(i -> p[i], n);
        for (int i = 0; i < q; i++) {
            int u = scc.set[in.ri() - 1];
            int v = scc.set[in.ri() - 1];
            if (dsu.find(u) != dsu.find(v)) {
                out.println("No");
                return;
            }
            int l = lca.lca(u, depth[u], v, depth[v]);
            //u to v
            upAdd[u]++;
            upAdd[l]--;
            botAdd[v]++;
            botAdd[l]--;
        }

        Arrays.fill(visited, false);
        for (int i = 0; i < n; i++) {
            if (scc.set[i] != i || visited[i]) {
                continue;
            }
            trySet(i, -1);
        }
        if (ok) {
            out.println("Yes");
        } else {
            out.println("No");
        }
    }
}
