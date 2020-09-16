package on2020_09.on2020_09_10_Single_Round_Match_790.TheSocialNetwork;



import template.datastructure.DSU;
import template.math.Modular;
import template.math.Power;

import java.util.Arrays;

public class TheSocialNetwork {
    public int minimumCut(int n, int m, int[] u, int[] v, int[] l) {
        dsu = new DSU(n);
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = u[i] - 1;
            edges[i].b = v[i] - 1;
            edges[i].w = l[i];
        }

        Arrays.sort(edges, (a, b) -> Integer.compare(a.w, b.w));
        boolean[] deleted = new boolean[m];
        for (int i = m - 1; i >= 0; i--) {
            dsu.reset();
            for (int j = i; j < m; j++) {
                if (deleted[j]) {
                    continue;
                }
                dsu.merge(edges[j].a, edges[j].b);
            }
            boolean conn = true;
            for (int j = 1; j < n; j++) {
                if (dsu.find(0) != dsu.find(j)) {
                    conn = false;
                }
            }
            if (conn) {
                deleted[i] = true;
            }
        }

        long ans = 0;
        Modular mod = new Modular(1e9 + 7);
        Power pow = new Power(mod);
        for (int i = 0; i < m; i++) {
            if (!deleted[i]) {
                continue;
            }
            ans += pow.pow(2, edges[i].w);
        }
        return mod.valueOf(ans);
    }


    DSU dsu;
}

class Edge {
    int a;
    int b;
    int w;
}
