package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.util.stream.IntStream;

public class PyramidArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] indices = IntStream.range(0, n).toArray();
        CompareUtils.radixSort(indices, 0, n - 1, i -> a[i]);
        SequenceUtils.reverse(indices);
        int[] before = new int[n];
        int[] after = new int[n];
        IntegerBIT bit = new IntegerBIT(n);
        for (int i : indices) {
            before[i] = bit.query(1, i + 1);
            after[i] = bit.query(i + 1, n);
            bit.update(i + 1, 1);
        }

        for (int i = 0; i < n; i++) {
            if (before[i] >= after[i]) {
                a[i] = Integer.MAX_VALUE - a[i];
            }
        }

        bit.clear();
        CompareUtils.radixSort(indices, 0, n - 1, i -> a[i]);
        long ans = 0;
        for (int i : indices) {
            ans += bit.query(i + 1, n);
            bit.update(i + 1, 1);
        }
        out.println(ans);
    }
}
