package on2021_04.on2021_04_18_AtCoder___AtCoder_Regular_Contest_117.D___Miracle_Tree;



import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class DMiracleTree {
    List<UndirectedEdge>[] g;
    int[] open;
    int[] close;
    int[] depth;
    int order;

    public void dfs(int root, int p) {
        open[root] = order++;
        depth[root] = p == -1 ? 0 : depth[p] + 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root);
        }
        close[root] = order - 1;
    }

    public int maxDepth() {
        int best = 0;
        for (int i = 0; i < depth.length; i++) {
            if (depth[i] > depth[best]) {
                best = i;
            }
        }
        return best;
    }

    int[] id;

    public boolean sub(int a, int b) {
        return open[a] <= open[b] && close[a] >= close[b];
    }

    public void alloc(int root, int p, int leave) {
        order++;
        id[root] = order;
        int sub = -1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            if (sub(e.to, leave)) {
                sub = e.to;
                continue;
            }
            alloc(e.to, root, leave);
        }
        if (sub != -1) {
            alloc(sub, root, leave);
        }

        order++;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        open = new int[n];
        close = new int[n];
        depth = new int[n];
        id = new int[n];
        dfs(0, -1);
        int end1 = maxDepth();
        order = 0;
        dfs(end1, -1);
        int end2 = maxDepth();
        order = 0;
        alloc(end1, -1, end2);
        for(int i = 0; i < n; i++){
            out.append(id[i]).append(' ');
        }
    }
}


