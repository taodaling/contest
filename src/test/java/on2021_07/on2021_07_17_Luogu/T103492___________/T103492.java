package on2021_07.on2021_07_17_Luogu.T103492___________;



import template.graph.Graph;
import template.graph.TarjanBiconnectedComponent;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.List;

public class T103492 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            if (a == b) {
                continue;
            }
            Graph.addUndirectedEdge(g, a, b);
        }
        TarjanBiconnectedComponent tbc = new TarjanBiconnectedComponent(n);
        tbc.init(g);
        for (int[] vcc : tbc.vcc) {
            for (int v : vcc) {
                out.append(v + 1).append(' ');
            }
            out.println();
        }
    }
}
