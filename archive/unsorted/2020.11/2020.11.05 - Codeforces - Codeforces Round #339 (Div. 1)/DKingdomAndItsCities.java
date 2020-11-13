package contest;



import template.graph.UndirectedEdge;
import template.graph.VirtualTree;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class DKingdomAndItsCities {
    List<UndirectedEdge>[] g;
    int[] depth;
    int[] imp;
    int round;
    VirtualTree vt;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        vt = new VirtualTree(n, 0);
        for (int i = 0; i < n - 1; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            vt.addEdge(a, b);
        }

        int q = in.readInt();
        imp = new int[n];
        depth = new int[n];
        g = vt.getTree();
        dfsForDepth(0, -1);
        for (int i = 0; i < q; i++) {
            round++;
            int k = in.readInt();
            vt.nextRound();
            for (int j = 0; j < k; j++) {
                int x = in.readInt() - 1;
                imp[x] = round;
                vt.active(x);
            }
            vt.buildVirtualTree();
            valid = true;
            cost = 0;
            dfs(vt.getVirtualTop(), -1);
            if (!valid) {
                out.println(-1);
            } else {
                out.println(cost);
            }
        }
    }

    boolean valid = true;
    int cost;

    public boolean dfs(int root, int p) {
        int sub = imp[root] == round ? 1 : 0;
        for (int node : vt.adj[root]) {
            boolean ans = dfs(node, root);
            sub += ans ? 1 : 0;
        }
        if (sub > 1) {
            sub = 0;
            cost++;
        }
        if (imp[root] == round && p != -1 && imp[p] == round
                && depth[root] == depth[p] + 1) {
            valid = false;
        }
        if (sub > 0 && p != -1 && imp[p] == round) {
            sub = 0;
            cost++;
        }
        return sub > 0;
    }

    public void dfsForDepth(int root, int p) {
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForDepth(e.to, root);
        }
    }

}

