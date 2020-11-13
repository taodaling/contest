package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.graph.VirtualTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.IntegerWeightGraph;
import template.primitve.generated.graph.IntegerWeightUndirectedEdge;
import template.primitve.generated.graph.LongWeightDirectedEdge;
import template.primitve.generated.graph.LongWeightGraph;
import template.primitve.generated.graph.LongWeightUndirectedEdge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P2495SDOI2011 {
    List<IntegerWeightUndirectedEdge>[] g;
    long[] minEdge;
    boolean[] special;
    List<Integer>[] adj;
    long[][] dp;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        minEdge = new long[n];
        special = new boolean[n];
        dp = new long[2][n];
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            IntegerWeightGraph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1, in.readInt());
        }
        dfsForPath(0, -1, (int) 1e9);
        VirtualTree vt = new VirtualTree(g, 0);
        int m = in.readInt();
        List<Integer> activeNodes = new ArrayList<>(n);
        for (int i = 0; i < m; i++) {
            int k = in.readInt();
            activeNodes.clear();
            for (int j = 0; j < k; j++) {
                int x = in.readInt() - 1;
                activeNodes.add(x);
                special[x] = true;
                vt.active(x);
            }
            adj = vt.buildVirtualTree();
            dfs(vt.getVirtualTop(), -1);
            long ans = dp[0][vt.getVirtualTop()];
            out.println(ans);
            for (int x : activeNodes) {
                special[x] = false;
            }
        }
    }

    long inf = (long) 1e18;

    private void dfs(int root, int p) {
        long notUse = 0;
        long any = 0;

        for (int node : adj[root]) {
            if (node == p) {
                continue;
            }
            dfs(node, root);
            notUse += dp[0][node];
            any += Math.min(dp[1][node], dp[0][node]);
        }

        if (special[root]) {
            notUse = inf;
        }
        dp[0][root] = Math.min(notUse, any + minEdge[root]);
        dp[1][root] = any;
        if (p == 0) {
            dp[1][root] = inf;
        }
    }

    public void dfsForPath(int root, int p, long path) {
        minEdge[root] = path;
        for (IntegerWeightUndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfsForPath(e.to, root, Math.min(path, e.weight));
        }
    }
}
