package contest;

import graphs.lca.Lca;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class EModernizationOfTreeland {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        g = Graph.createUndirectedGraph(n);
        for (int i = 1; i < n; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }
        if (k == 1) {
            out.println("Yes");
            out.println(1);
            out.println(1);
            return;
        }
        if (k > n) {
            out.println("No");
            return;
        }
        if (k == 2) {
            out.println("Yes");
            out.println(2);
            out.append(1).append(' ').append(g[0].get(0).to + 1).println();
            return;
        }

        int root = -1;
        for (int i = 0; i < n; i++) {
            if (g[i].size() == 1) {
                continue;
            }
            root = i;
            break;
        }

        if (root == -1) {
            out.println("No");
            return;
        }

        add = new boolean[n];
        add[root] = true;
        now = 1;
        dfs(root, -1);
        if (now != k) {
            out.println("No");
            return;
        }
        out.println("Yes");
        int cnt = 0;
        for (boolean x : add) {
            if (x) {
                cnt++;
            }
        }
        out.println(cnt);
        for (int i = 0; i < n; i++) {
            if (add[i]) {
                out.append(i + 1).append(' ');
            }
        }
        out.println();
    }

    int k;
    int now;
    List<UndirectedEdge>[] g;
    boolean[] add;

    public void dfs(int root, int p) {
        if (now == k) {
            return;
        }
        if (g[root].size() == 1) {
            return;
        }
        now--;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            add[e.to] = true;
            now++;
            if (now == k) {
                break;
            }
        }

        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            if (add[e.to]) {
                dfs(e.to, root);
            }
        }
    }
}
