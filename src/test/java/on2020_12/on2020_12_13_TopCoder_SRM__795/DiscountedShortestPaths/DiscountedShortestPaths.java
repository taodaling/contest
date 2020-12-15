package on2020_12.on2020_12_13_TopCoder_SRM__795.DiscountedShortestPaths;



import template.binary.Bits;
import template.rand.Randomized;
import template.utils.Buffer;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DiscountedShortestPaths {
    long inf = (long) 1e18;

    int[] d;
    long[] weights;
    long[] tmp;
    int src;
    long[][] dists;
    long[][] edges;
    Buffer<int[]> perms = new Buffer<>(() -> new int[edges.length]);

    public void dfs(int root, int state) {
        if (Bits.get(state, root) == 1) {
            return;
        }
        int k = Integer.bitCount(state);
        state |= 1 << root;
        long dist = 0;
        System.arraycopy(weights, 0, tmp, 0, k);
        Arrays.sort(tmp, 0, k);
        for (int i = k - 1; i >= 0; i--) {
            dist += Math.max(tmp[i] - d[d.length - (k - i)], 0);
        }
        if (dists[src][root] <= dist) {
            return;
        }
        dists[src][root] = dist;
        int[] p = perms.alloc();
        for (int i = 0; i < p.length; i++) {
            p[i] = i;
        }
        Randomized.shuffle(p);
        for (int i : p) {
            weights[k] = edges[root][p[i]];
            dfs(p[i], state);
        }
        perms.release(p);
    }

    public long minimumCostSum(int n, int[] a, int[] b, int[] c, int[] d) {
        edges = new long[n][n];
        tmp = new long[n];
        weights = new long[n];
        dists = new long[n][n];
        SequenceUtils.deepFill(dists, inf);
        SequenceUtils.deepFill(edges, inf);
        if (d.length < n) {
            d = Arrays.copyOf(d, n);
        }
        this.d = d;

        Arrays.sort(d);
        for (int i = 0; i < a.length; i++) {
            edges[b[i]][a[i]] = edges[a[i]][b[i]] = Math.min(c[i], edges[b[i]][a[i]]);
        }
        for (int i = 0; i < n; i++) {
            src = i;
            dfs(i, 0);
        }
        long sum = 0;
        for(int i = 0; i < n; i++){
            for(int j = i + 1; j < n; j++){
                sum += dists[i][j];
            }
        }
        return sum;
    }
}
