package on2020_12.on2020_12_13_TopCoder_SRM__795.DiscountedShortestPaths2;



import template.binary.Bits;
import template.graph.SubsetMST;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;
import java.util.function.IntConsumer;

public class DiscountedShortestPaths {
    //Debug debug = new Debug(false);
    public long minimumCostSum(int n, int[] a, int[] b, int[] c, int[] d) {
        if (d.length < n) {
            d = Arrays.copyOf(d, n);
        }
        Arrays.sort(d);
        long[] w = Arrays.stream(c).asLongStream().toArray();
        SubsetMST mst = new SubsetMST(n, a, b, w);
        long[] set = new long[1 << n];
        long inf = (long) 2e18;
        IntegerArrayList costs = new IntegerArrayList();
        IntConsumer addToCosts = x -> costs.add(c[x]);
        for (int i = 0; i < 1 << n; i++) {
            costs.clear();
            mst.mst(addToCosts, i);
            int k = Integer.bitCount(i) - 1;
            if (costs.size() != k) {
                set[i] = inf;
                continue;
            }
            for (int j = 0; j < k; j++) {
                set[i] += Math.max(0, costs.get(j) - d[d.length - (k - j)]);
            }
        }
       // debug.debug("set", set);
        for (int i = (1 << n) - 1; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (Bits.get(i, j) == 1) {
                    int next = Bits.clear(i, j);
                    set[next] = Math.min(set[next], set[i]);
                }
            }
        }
        long ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                ans += set[(1 << i) | (1 << j)];
            }
        }
        return ans;
    }
}
