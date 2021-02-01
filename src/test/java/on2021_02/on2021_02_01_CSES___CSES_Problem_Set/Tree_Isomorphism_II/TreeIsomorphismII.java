package on2021_02.on2021_02_01_CSES___CSES_Problem_Set.Tree_Isomorphism_II;



import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashOnTree2;
import template.rand.SparseMultiSetHasher;

import java.util.List;

public class TreeIsomorphismII {
    SparseMultiSetHasher hasher = new SparseMultiSetHasher((int) 1e6);
    HashOnTree2 hot = new HashOnTree2((int) 1e5, hasher);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        out.println(readGraph(in, n) == readGraph(in, n) ? "YES" : "NO");
    }

    public long readGraph(FastInput in, int n) {
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        hot.init(g);
        return hot.hashWithoutRoot();
    }
}
