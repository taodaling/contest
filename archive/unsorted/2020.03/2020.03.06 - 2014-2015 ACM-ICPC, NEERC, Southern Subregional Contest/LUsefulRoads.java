package contest;

import template.graph.DirectedEdge;
import template.graph.DominatorTree;
import template.graph.Graph;
import template.graph.LcaOnTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.List;

public class LUsefulRoads {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        if (!in.hasMore()) {
            throw new UnknownError();
        }
        int n = in.readInt();
        int m = in.readInt();
        List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges[i][0] = a;
            edges[i][1] = b;
            Graph.addEdge(g, a, b);
        }

        DominatorTree dt = new DominatorTree(g, 0);
        List<DirectedEdge>[] tree = dt.getTree();
        LcaOnTree lcaOnTree = new LcaOnTree(tree, 0);

        debug.debug("tree", tree);
        IntegerList ans = new IntegerList(m);
        for (int i = 0; i < m; i++) {
            int a = edges[i][0];
            int b = edges[i][1];
            if ((a != 0 && dt.parent(a) == -1) || dt.parent(b) == -1) {
                continue;
            }
            int c = lcaOnTree.lca(a, b);
            if (c != b) {
                ans.add(i);
            }
        }

        out.println(ans.size());
        for (int i = 0; i < ans.size(); i++) {
            out.append(ans.get(i) + 1).append(' ');
        }
        out.println();
    }
}
