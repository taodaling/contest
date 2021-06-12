package on2021_05.on2021_05_23_AtCoder___AtCoder_Regular_Contest_120.C___Swaps_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CSwaps2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri() + i;
        }
        for (int i = 0; i < n; i++) {
            b[i] = in.ri() + i;
        }
        Map<Integer, List<Integer>> ma = IntStream.range(0, n)
                .boxed().collect(Collectors.groupingBy(x -> a[x]));
        Map<Integer, List<Integer>> mb = IntStream.range(0, n)
                .boxed().collect(Collectors.groupingBy(x -> b[x]));
        int[] target = new int[n];
        for (Integer key : ma.keySet()) {
            List<Integer> la = ma.getOrDefault(key, Collections.emptyList());
            List<Integer> lb = mb.getOrDefault(key, Collections.emptyList());
            la.sort(Comparator.naturalOrder());
            lb.sort(Comparator.naturalOrder());
            if (la.size() != lb.size()) {
                out.println(-1);
                return;
            }
            debug.debug("la", la);
            debug.debug("lb", lb);
            for (int i = 0; i < la.size(); i++) {
                target[la.get(i)] = lb.get(i);
            }
        }

        debug.debug("target", target);
        IntegerBIT bit = new IntegerBIT(n);
        long sum = 0;
        for(int i = 0; i < n; i++){
            sum += i - bit.query(target[i] + 1);
            bit.update(target[i] + 1, 1);
        }
        out.println(sum);
    }

    Debug debug = new Debug(false);
}
