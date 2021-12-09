package on2021_11.on2021_11_17_Library_Checker.Associative_Array0;





import template.datastructure.PerfectHashing;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongComparator;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class AssociativeArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int q = in.ri();
        long[][] qs = new long[q][];
        long[] keys = new long[q];
        for (int i = 0; i < q; i++) {
            long t = in.rl();
            long k = in.rl();
            keys[i] = k;
            if (t == 0) {
                qs[i] = new long[]{k, in.rl()};
            } else {
                qs[i] = new long[]{k};
            }
        }
        SortUtils.radixSort(keys, 0, q - 1);
        keys = SortUtils.unique(keys, 0, q - 1, LongComparator.NATURE_ORDER);
        long[] V = new long[keys.length];
        Integer[] indices = IntStream.range(0, keys.length).boxed().toArray(x -> new Integer[x]);
        PerfectHashing<Integer> ph = new PerfectHashing<>(keys, indices);
        for (long[] query : qs) {
            if (query.length == 1) {
                out.println(V[ph.get(query[0])]);
            } else {
                V[ph.get(query[0])] = query[1];
            }
        }
    }
}

