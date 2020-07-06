package on2020_07.on2020_07_06_Luogu.P5903________k____;



import template.graph.DirectedEdge;
import template.graph.Graph;
import template.graph.KthAncestorOnTree;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.List;

public class P5903K {
    int s;
    long UI_MASK = (1L << 32) - 1;

    long get(int x) {
        x ^= x << 13;
        x ^= x >>> 17;
        x ^= x << 5;
        return (s = x) & UI_MASK;
    }

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();
        s = (int) in.readLong();
        int[] father = new int[n];
        in.populate(father);
        int root = -1;
        List<DirectedEdge>[] g = Graph.createDirectedGraph(n);
        for (int i = 0; i < n; i++) {
            father[i]--;
            if (father[i] == -1) {
                root = i;
            } else {
                Graph.addEdge(g, father[i], i);
            }
        }
        debug.elapse("build graph");

        long xor = 0;
        KthAncestorOnTree tree = new KthAncestorOnTree(g, root);
        debug.elapse("build tree");

        int ans = 0;
        for (int i = 0; i < q; i++) {
            int x = (int) ((get(s) ^ ans) % n);
            int depth = tree.getDepth(x) + 1;
            int k = (int) ((get(s) ^ ans) % depth);
            ans = tree.kthAncestor(x, k) + 1;
            xor ^= ans * (long) (i + 1);
//            debug.debug("x", x);
//            debug.debug("k", k);
//            debug.debug("ans", ans);
        }
        debug.elapse("process query");

        out.println(xor);
    }
}
