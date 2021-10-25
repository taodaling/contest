package on2021_10.on2021_10_22_Codeforces___Codeforces_Round__749__Div__1___Div__2__based_on_Technocup_2022_Elimination_Round_1_.E__Moment_of_Bloom;



import template.datastructure.DSU;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.List;

public class EMomentOfBloom {

    List<UndirectedEdge>[] g;
    IntegerArrayList dq = new IntegerArrayList();

    public boolean dfs(int root, int p, int target) {
        dq.add(root);

        if (root == target) {
            return true;
        }

        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            if (dfs(e.to, root, target)) {
                return true;
            }
        }

        dq.pop();
        return false;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();

        DSU dsu = new DSU(n);
        g = Graph.createGraph(n);
        dsu.init();

        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;

            if (dsu.find(a) == dsu.find(b)) {
                continue;
            }
            dsu.merge(a, b);
            Graph.addUndirectedEdge(g, a, b);
        }

        int q = in.ri();
        int[][] qs = new int[q][2];
        int[] deg = new int[n];
        for (int i = 0; i < q; i++) {
            qs[i][0] = in.ri() - 1;
            qs[i][1] = in.ri() - 1;
            deg[qs[i][0]] ^= 1;
            deg[qs[i][1]] ^= 1;
        }

        int sum = Arrays.stream(deg).sum();
        if (sum > 0) {
            out.println("NO");
            out.println(sum / 2);
            return;
        }
        out.println("YES");

        for (int[] query : qs) {
            dq.clear();
            dfs(query[0], -1, query[1]);
            out.println(dq.size());
            for (int x : dq.toArray()) {
                out.append(x + 1).append(' ');
            }
            out.println();
        }
    }
}
