package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class TreeMatching {
    List<UndirectedEdge>[] g;
    int match = 0;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        dfs(0, -1);
        out.println(match);
    }

    public int dfs(int root, int p) {
        int sub = 0;
        for (UndirectedEdge e : g[root]) {
            if(e.to == p){
                continue;
            }
            sub += dfs(e.to, root);
        }
        if (sub > 0) {
            match++;
            return 0;
        }
        return 1;
    }
}
