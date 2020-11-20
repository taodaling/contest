package contest;

import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.HashOnTreeBeta;
import template.utils.Debug;

import java.util.List;

public class TreeIsomorphismII {
    HashOnTreeBeta frt = new HashOnTreeBeta(31, 61, (int) 1e5);


    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        List<UndirectedEdge>[] t = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.readInt() - 1, in.readInt() - 1);
        }

        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(t, in.readInt() - 1, in.readInt() - 1);
        }

        debug.elapse("read");

        frt.init(g);
        long gh = frt.hashTree();
        frt.init(t);
        long th = frt.hashTree();
        debug.elapse("hashOnTree");
        out.println(gh == th ? "YES" : "NO");
        debug.elapse("lazy over");
    }
}
