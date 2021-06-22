package on2021_06.on2021_06_21_Luogu.P6329_____________;



import template.graph.*;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class P6329 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        long[] weights = in.rl(n);
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            int u = in.ri() - 1;
            int v = in.ri() - 1;
            Graph.addUndirectedEdge(g, u, v);
        }
        DistanceOnTree dot = new DistanceOnTreeImpl(new LcaOnTreeBySchieberVishkin(g, 0), new DepthOnTreeImpl(g, 0));
        VertexDacTree vdt = new VertexDacTree(g, weights, dot);
        long last = 0;

        boolean encrypt = true;
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int x = in.ri();
            int y = in.ri();
            if (encrypt) {
                x ^= last;
                y ^= last;
            }
            x--;
            if (t == 0) {
                last = vdt.query(x, y);
                out.println(last);
            } else {
                vdt.update(x, y - weights[x]);
                weights[x] = y;
            }
        }
    }
}
