package on2021_07.on2021_07_17_Virtual_Judge___UVALive.Mining_Your_Own_Business;



import template.graph.Graph;
import template.graph.TarjanBiconnectedComponent;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.List;

public class MiningYourOwnBusiness {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        if (m == 0) {
            throw new UnknownError();
        }
        int[][] edges = new int[2][m];
        int n = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                edges[j][i] = in.ri() - 1;
                n = Math.max(n, edges[j][i]);
            }
        }
        n++;
        debug.debug("m", m);
        debug.debug("n", n);

        List<UndirectedEdge>[] g = Graph.createGraph(n);
        for (int i = 0; i < m; i++) {
            Graph.addUndirectedEdge(g, edges[0][i], edges[1][i]);
        }
        TarjanBiconnectedComponent bc = new TarjanBiconnectedComponent(n);
        bc.init(g);
        int ans = 0;
        long way = 1;
        for (int[] vc : bc.vcc) {
            if (vc.length == 1) {
                continue;
            }
            int cut = 0;
            for (int x : vc) {
                if (bc.isCut(x)) {
                    cut++;
                }
            }
            if (cut == 1) {
                ans++;
                way *= vc.length - 1;
            } else if (cut == 0) {
                ans += 2;
                way *= (long) vc.length * (vc.length - 1) / 2;
            } else {

            }
        }

        out.printf("Case %d: %d %d", testNumber, ans, way).println();
    }
}
