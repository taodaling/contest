package contest;

import template.io.FastInput;
import template.rand.Randomized;
import template.utils.SortUtils;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class SumOfThreeValues {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] p = new int[n];
        int[] indices = IntStream.range(0, n).toArray();
        in.populate(p);
        SortUtils.radixSort(indices, 0, n - 1, i -> p[i]);
        for (int i = 0; i < n; i++) {
            int target = x - p[indices[i]];
            int l = i + 1;
            int r = n - 1;
            while (l < r) {
                if (p[indices[l]] + p[indices[r]] < target) {
                    l++;
                } else if (p[indices[l]] + p[indices[r]] > target) {
                    r--;
                } else {
                    out.println(indices[i] + 1);
                    out.println(indices[l] + 1);
                    out.println(indices[r] + 1);
                    return;
                }
            }
        }
        out.println("IMPOSSIBLE");
    }
}
