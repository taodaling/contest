package contest;

import template.datastructure.ModBIT;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class IncreasingSubsequenceII {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> a[i] == a[j] ? -Integer.compare(i, j) :
                Integer.compare(a[i], a[j]), 0, n);
        ModBIT bit = new ModBIT(n, mod);
        for (int i : indices) {
            int sum = bit.query(i + 1);
            sum = DigitUtils.modplus(sum, 1, mod);
            bit.update(i + 1, sum);
        }
        int ans = bit.query(n);
        out.println(ans);
    }
}
