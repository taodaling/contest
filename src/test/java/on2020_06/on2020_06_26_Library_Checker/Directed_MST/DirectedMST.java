package on2020_06.on2020_06_26_Library_Checker.Directed_MST;



import template.graph.DirectedEdge;
import template.graph.DirectedMinSpanningTree;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.List;

public class DirectedMST {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();
        DirectedMinSpanningTree dmst = new DirectedMinSpanningTree(n);
        g = Graph.createDirectedGraph(n);
        int[][] edges = new int[m][3];
        parent = new int[n];
        for (int i = 0; i < m; i++) {
            edges[i][0] = in.readInt();
            edges[i][1] = in.readInt();
            edges[i][2] = in.readInt();
            dmst.addEdge(i, edges[i][0], edges[i][1], edges[i][2]);
        }

        dmst.contract();
        IntegerArrayList selected = new IntegerArrayList(m);
        dmst.dismantle(s, selected);

        long sum = 0;
        for (int i = 0; i < selected.size(); i++) {
            int[] e = edges[selected.get(i)];
            sum += e[2];
            Graph.addEdge(g, e[0], e[1]);
        }

        out.println(sum);
        dfs(s, s);
        for(int i = 0; i < n; i++){
            out.append(parent[i]).append(' ');
        }
    }

    int[] parent;
    List<DirectedEdge>[] g;

    public void dfs(int root, int p) {
        parent[root] = p;
        for (DirectedEdge e : g[root]) {
            dfs(e.to, root);
        }
    }
}
