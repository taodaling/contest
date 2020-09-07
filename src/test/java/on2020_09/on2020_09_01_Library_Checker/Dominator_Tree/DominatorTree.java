package on2020_09.on2020_09_01_Library_Checker.Dominator_Tree;



import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class DominatorTree {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int s = in.readInt();

        List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.readInt();
            int b = in.readInt();
            Graph.addEdge(g, a, b);
        }

        template.graph.DominatorTree dt = new template.graph.DominatorTree(g, s);
        for (int i = 0; i < n; i++) {
            if (i == s) {
                out.append(s).append(' ');
                continue;
            }
            out.append(dt.parent(i)).append(' ');
        }
    }
}
