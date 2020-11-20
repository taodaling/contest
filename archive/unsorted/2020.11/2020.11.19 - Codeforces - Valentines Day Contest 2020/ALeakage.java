package contest;

import template.graph.CutVertexes;
import template.graph.DfsTree;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.KthAncestorOnTreeByBinaryLift;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class ALeakage {
    CutVertexes cv;
    List<DirectedEdge>[] tree;
    int[] prefix;

    public void dfs(int root, int p) {
        prefix[root] = p == -1 ? 0 : prefix[p];
        if (cv.beCut[root]) {
            prefix[root]++;
        }
        for (DirectedEdge e : tree[root]) {
            dfs(e.to, root);
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        prefix = new int[n];
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Graph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1);
        }
        tree = Graph.createGraph(n);
        DfsTree dt = new DfsTree(n);
        dt.consumer = (root, e) -> tree[root].add(e);
        dt.init(g);
        cv = new CutVertexes(n);
        cv.init(dt);
        KthAncestorOnTreeByBinaryLift ka = new KthAncestorOnTreeByBinaryLift(n);
        ka.init(i -> dt.parents[i], n);
        dfs(0, -1);

        int q = in.readInt();
        for (int i = 0; i < q; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            if (dt.depth[u] > dt.depth[v]) {
                int tmp = u;
                u = v;
                v = tmp;
            }
            int lca = ka.lca(u, dt.depth[u], v, dt.depth[v]);
            int au = u;
            int av = v;
            if (au != lca) {
                au = ka.kthAncestor(u, dt.depth[u] - (dt.depth[lca] + 1));
            }
            if (av != lca) {
                av = ka.kthAncestor(v, dt.depth[v] - (dt.depth[lca] + 1));
            }

            int ans = prefix[u] + prefix[v] - 2 * prefix[lca];
            if (u == lca) {
                ans -= cv.beCut[av] ? 1 : 0;
            } else if (cv.beCut[au] && cv.beCut[av]) {
                ans--;
            }
            out.println(ans);
        }
    }
}
