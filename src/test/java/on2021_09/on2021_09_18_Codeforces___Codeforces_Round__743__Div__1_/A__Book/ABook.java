package on2021_09.on2021_09_18_Codeforces___Codeforces_Round__743__Div__1_.A__Book;



import template.datastructure.RangeTree;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class ABook {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        List<DirectedEdge>[] g = Graph.createGraph(n);
        int[] deg = new int[n];
        for (int i = 0; i < n; i++) {
            int k = in.ri();
            deg[i] = k;
            for (int j = 0; j < k; j++) {
                Graph.addEdge(g, in.ri() - 1, i);
            }
        }
        RangeTree rt = new RangeTree(n);
        for (int i = 0; i < n; i++) {
            if (deg[i] == 0) {
                rt.add(i);
            }
        }
        int time = 0;
        while (rt.size() > 0) {
            time++;
            for (int iter = rt.first(); iter != -1; iter = rt.ceil(iter)) {
                rt.remove(iter);
                for (DirectedEdge e : g[iter]) {
                    deg[e.to]--;
                    if (deg[e.to] == 0) {
                        rt.add(e.to);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (deg[i] > 0) {
                out.println(-1);
                return;
            }
        }
        out.println(time);
    }
}
