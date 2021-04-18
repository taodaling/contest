package on2021_04.on2021_04_09_Codeforces___Codeforces_Round__219__Div__1_.D__Choosing_Subtree_is_Fun;



import template.algo.BinarySearch;
import template.graph.Graph;
import template.graph.MinSpanningGraphOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;
import java.util.function.IntPredicate;

public class DChoosingSubtreeIsFun {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }
        MinSpanningGraphOnTree msg = new MinSpanningGraphOnTree(n);
        msg.init(g);
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            r[i] = i > 0 ? r[i - 1] : -1;
            while (r[i] + 1 < n && msg.minSpanningGraph() <= k) {
                r[i]++;
                msg.add(r[i]);
            }
            if (msg.minSpanningGraph() <= k) {
                r[i] = n;
            }
            msg.remove(i);
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            ans = Math.max(ans, r[i] - i);
        }
        out.println(ans);
    }
}
