package on2020_02.on2020_02_21_Codeforces_Round__468__Div__1__based_on_Technocup_2018_Final_Round_.A__Peculiar_apple_tree;



import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class APeculiarAppleTree {
    int[] depths;
    List<UndirectedEdge>[] g;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        depths = new int[n];
        g = Graph.createUndirectedGraph(n);
        for (int i = 1; i < n; i++) {
            int p = in.readInt() - 1;
            Graph.addUndirectedEdge(g, p, i);
        }
        dfs(0, -1, 0);
        int[] xor = new int[n];
        for (int d : depths) {
            xor[d] ^= 1;
        }

        int ans = 0;
        for (int x : xor) {
            ans += x;
        }

        out.println(ans);
    }

    public void dfs(int root, int p, int depth) {
        depths[root] = depth;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, depth + 1);
        }
    }
}
