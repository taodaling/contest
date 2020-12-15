package contest;

import template.graph.DirectedEdge;
import template.graph.DirectedTarjanSCC;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class StronglyConnectedComponents {
    DirectedTarjanSCC tarjan;
    List<Integer>[] classify;
    List<DirectedEdge>[] g;
    boolean[] visited;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Graph.addEdge(g, in.ri(), in.ri());
        }
        tarjan = new DirectedTarjanSCC(n);
        tarjan.init(g);
        classify = Graph.createGraph(n);
        seq = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            classify[tarjan.set[i]].add(i);
        }
        visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            dfs(i);
        }
        SequenceUtils.reverse(seq);
        out.println(seq.size());
        for(List<Integer> set : seq){
            out.append(set.size()).append(' ');
            for (int node : set) {
                out.append(node).append(' ');
            }
            out.println();
        }
    }

    List<List<Integer>> seq;

    public void dfs(int root) {
        root = tarjan.set[root];
        if (visited[root]) {
            return;
        }
        visited[root] = true;
        for (int node : classify[root]) {
            for(DirectedEdge e : g[node]) {
                dfs(e.to);
            }
        }
        seq.add(classify[root]);
    }
}
