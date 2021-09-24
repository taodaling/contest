package on2021_08.on2021_08_20_CS_Academy___Virtual_Beta_Round__2.Circular_Subarrays;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CircularSubarrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        DSU dsu = new DSU(n);
        dsu.init();
        int[] a = in.ri(n);
        for (int i = 0; i < n; i++) {
            dsu.merge(i, (i + k) % n);
        }
        Map<Integer, List<Integer>> groupBy = IntStream.range(0, n)
                .boxed().collect(Collectors.groupingBy(x -> dsu.find(x)));
        long ans = 0;
        for (List<Integer> indices : groupBy.values()) {
            List<Integer> values = indices.stream().map(x -> a[x]).collect(Collectors.toList());
            values.sort(Comparator.naturalOrder());
            int mid = values.get(values.size() / 2);
            long contrib = 0;
            for (int x : values) {
                contrib += Math.abs(x - mid);
            }
            ans += contrib;
        }
        out.println(ans);
    }
}
