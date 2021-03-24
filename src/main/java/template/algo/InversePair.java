package template.algo;

import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class InversePair {
    public static long inversePairCount(int[] a) {
        int n = a.length;
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (x, y) -> a[x] == a[y] ? Integer.compare(x, y) :
                Integer.compare(a[x], a[y]), 0, n);
        long ans = 0;
        IntegerBIT bit = new IntegerBIT(n);
        for (int i : indices) {
            ans += bit.query(i + 1, n);
            bit.update(i + 1, 1);
        }
        return ans;
    }
}
