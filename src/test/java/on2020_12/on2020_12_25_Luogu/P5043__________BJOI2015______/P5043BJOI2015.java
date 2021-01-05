package on2020_12.on2020_12_25_Luogu.P5043__________BJOI2015______;



import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.DenseMultiSetHasher;
import template.rand.HashOnTree2;
import template.rand.MultiSetHasher;
import template.rand.SparseMultiSetHasher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P5043BJOI2015 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        MultiSetHasher hasher = new SparseMultiSetHasher(10000);
        HashOnTree2 hot = new HashOnTree2(50, hasher);
        Map<Long, Integer> map = new HashMap<>(m);
        for (int i = 0; i < m; i++) {
            int n = in.ri();
            List<UndirectedEdge>[] g = Graph.createGraph(n);
            for (int j = 0; j < n; j++) {
                int p = in.ri() - 1;
                if (p == -1) {
                    continue;
                }
                Graph.addUndirectedEdge(g, j, p);
            }
            hot.init(g);
            long tree = hot.hashWithoutRoot();
            map.putIfAbsent(tree, i);
            out.println(map.get(tree) + 1);
        }
    }
}
