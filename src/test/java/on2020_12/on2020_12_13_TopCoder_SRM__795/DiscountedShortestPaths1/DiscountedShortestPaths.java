package on2020_12.on2020_12_13_TopCoder_SRM__795.DiscountedShortestPaths1;



import template.binary.Bits;
import template.datastructure.DSU;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;

public class DiscountedShortestPaths {
    public long minimumCostSum(int n, int[] a, int[] b, int[] c, int[] d) {
        int m = a.length;
        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            edges[i] = new Edge();
            edges[i].a = a[i];
            edges[i].b = b[i];
            edges[i].w = c[i];
        }
        if (d.length < n - 1) {
            d = Arrays.copyOf(d, n - 1);
        }
        Arrays.sort(d);
        Arrays.sort(edges, (x, y) -> Integer.compare(x.w, y.w));
        DSU dsu = new DSU(n);

        long inf = (long)1e18;
        long[][] dists = new long[n][n];
        SequenceUtils.deepFill(dists, inf);
        for (int i = 0; i < 1 << n; i++) {
            int k = 0;
            int size = Integer.bitCount(i);
            long ans = 0;
            dsu.init();
            for (Edge e : edges) {
                if (Bits.get(i, e.a) + Bits.get(i, e.b) != 2) {
                    continue;
                }
                if (dsu.find(e.a) != dsu.find(e.b)) {
                    dsu.merge(e.a, e.b);
                    k++;
                    int discount = d[d.length - (size - k)];
                    ans += Math.max(e.w - discount, 0);
                }
            }
            if(k != size - 1){
                continue;
            }
            for(int j = 0; j < n; j++){
                for(int t = j + 1; t < n; t++){
                    if(Bits.get(i, j) + Bits.get(i, t) != 2){
                        continue;
                    }
                    dists[j][t] = Math.min(dists[j][t], ans);
                }
            }
        }

        long ans = 0;
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                ans += dists[i][j];
            }
        }
        return ans;
    }
}

class Edge {
    int a;
    int b;
    int w;
}
