package on2021_11.on2021_11_09_AtCoder___AtCoder_Beginner_Contest_226.C___Martial_artist;



import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;

public class CMartialArtist {
    int[] time;
    List<Integer>[] adj;
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        adj = Graph.createGraph(n);
        time = new int[n];
        visited = new boolean[n];
        for(int i = 0; i < n; i++){
            time[i] = in.ri();
            int k = in.ri();
            for(int j = 0; j < k; j++){
                adj[i].add(in.ri() - 1);
            }
        }
        out.println(dfs(n - 1));
    }

    boolean[] visited;
    public long dfs(int root){
        if(visited[root]){
            return 0;
        }
        visited[root] = true;
        long ans = time[root];
        for(int node : adj[root]){
            ans += dfs(node);
        }
        return ans;
    }
}
