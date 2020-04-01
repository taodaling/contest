package on2020_04.on2020_04_01_Educational_Codeforces_Round_2.F__Edge_coloring_of_bipartite_graph0;




import template.graph.BipartiteGraphEdgeColoring;
import template.io.FastInput;
import template.io.FastOutput;

public class FEdgeColoringOfBipartiteGraph {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int m = in.readInt();
        boolean[][] g = new boolean[a][b];
        int[][] colors = new int[a][b];
        int[][] edges = new int[2][m];
        for (int i = 0; i < m; i++) {
            int u = in.readInt() - 1;
            int v = in.readInt() - 1;
            g[u][v] = true;
            edges[0][i] = u;
            edges[1][i] = v;
        }

        BipartiteGraphEdgeColoring bgec = new BipartiteGraphEdgeColoring(a, b);
        int ans = bgec.solve(g, colors);
        out.println(ans);
        for (int i = 0; i < m; i++) {
            int u = edges[0][i];
            int v = edges[1][i];
            out.append(colors[u][v] + 1).append(' ');
        }
    }
}
