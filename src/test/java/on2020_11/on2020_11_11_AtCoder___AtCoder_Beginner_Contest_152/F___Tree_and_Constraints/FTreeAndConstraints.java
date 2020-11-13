package on2020_11.on2020_11_11_AtCoder___AtCoder_Beginner_Contest_152.F___Tree_and_Constraints;



import graphs.lca.Lca;
import template.binary.Bits;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FTreeAndConstraints {
    List<UndirectedEdge>[] g;
    int[] cnts;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        g = Graph.createGraph(n);
        cnts = new int[n];
        for (int i = 0; i < n - 1; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        LcaOnTree lca = new LcaOnTree(g, 0);
        int m = in.readInt();
        int[][] cs = new int[m][3];
        for (int i = 0; i < m; i++) {
            cs[i][0] = in.readInt() - 1;
            cs[i][1] = in.readInt() - 1;
            cs[i][2] = lca.lca(cs[i][0], cs[i][1]);
        }
        long ans = 0;
        for (int i = 0; i < 1 << m; i++) {
            Arrays.fill(cnts, 0);
            for (int j = 0; j < m; j++) {
                if (Bits.get(i, j) == 1) {
                    cnts[cs[j][0]]++;
                    cnts[cs[j][1]]++;
                    cnts[cs[j][2]] -= 2;
                }
            }
            long contrib = 1L << dfs(0, -1);
            if (Integer.bitCount(i) % 2 == 1) {
                contrib = -contrib;
            }
            ans += contrib;
        }
        out.println(ans);
    }

    public int dfs(int root, int p) {
        int ans = 0;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            ans += dfs(e.to, root);
            cnts[root] += cnts[e.to];
        }
        if (cnts[root] == 0 && p != -1) {
            ans++;
        }
        return ans;
    }
}

