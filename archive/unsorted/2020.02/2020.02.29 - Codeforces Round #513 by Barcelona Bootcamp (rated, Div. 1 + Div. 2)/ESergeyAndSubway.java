package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class ESergeyAndSubway {
    long[][] dp;
    long[] odd;
    List<UndirectedEdge>[] g;
    long[] sum;
    int[] size;
    long[] path;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        dp = new long[n][2];
        odd = new long[n];
        sum = new long[n];
        size = new int[n];
        path = new long[n];
        g = Graph.createUndirectedGraph(n);
        for(int i = 1; i < n; i++){
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            Graph.addUndirectedEdge(g, a, b);
        }

        dfs(0, -1);
        long total = 0;
        long oddTotal = 0;
        for(int i = 0; i < n; i++){
            total += path[i];
            oddTotal += odd[i];
        }
        long ans = (total - oddTotal) / 2 + oddTotal;
        out.println(ans);
    }



    public void dfs(int root, int p){
        dp[root][0] = 1;
        size[root] = 1;
        sum[root] = 0;
        for(UndirectedEdge e : g[root]){
            if(e.to == p){
                continue;
            }
            dfs(e.to, root);
            odd[root] += dp[root][0] * dp[e.to][0] + dp[root][1] * dp[e.to][1];
            dp[root][0] += dp[e.to][1];
            dp[root][1] += dp[e.to][0];
            path[root] += sum[root] * size[e.to] + size[root] * (sum[e.to] + size[e.to]);
            size[root] += size[e.to];
            sum[root] += sum[e.to] + size[e.to];
        }
    }
}
