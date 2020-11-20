package template.graph;

import java.util.Arrays;

public class CutVertexes {
    public boolean[] cut;
    public boolean[] visited;
    public boolean[] beCut;
    DfsTree dt;

    public CutVertexes(int n) {
        cut = new boolean[n];
        visited = new boolean[n];
        beCut = new boolean[n];
    }

    public void init(DfsTree dt) {
        this.dt = dt;
        int n = dt.g.length;
        Arrays.fill(visited, 0, n, false);
        for (int i = 0; i < n; i++) {
            if (dt.parents[i] != -1) {
                continue;
            }
            dfs(i);
            cut[i] = false;
            int lastChild = -1;
            for (DirectedEdge e : dt.g[i]) {
                if (dt.parents[e.to] == i) {
                    if (lastChild == -1 || lastChild == e.to) {
                        lastChild = e.to;
                    } else {
                        cut[i] = true;
                    }
                }
            }
        }
    }

    private int dfs(int root) {
        visited[root] = true;
        beCut[root] = cut[root] = false;
        int min = dt.depth[root];
        for (DirectedEdge e : dt.g[root]) {
            if (dt.parents[e.to] == root && !visited[e.to]) {
                int sub = dfs(e.to);
                if (sub >= dt.depth[root]) {
                    cut[root] = true;
                    beCut[e.to] = true;
                }
                min = Math.min(sub, min);
            } else {
                min = Math.min(dt.depth[e.to], min);
            }
        }
        return min;
    }
}
