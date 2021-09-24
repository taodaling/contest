package on2021_08.on2021_08_16_CS_Academy___Virtual_Round__10.Subarray_Medians;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SubarrayMedians {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        int[] indices = IntStream.range(0, n).toArray();
        SortUtils.quickSort(indices, (i, j) -> Long.compare(a[i], a[j]), 0, n);
        SequenceUtils.reverse(indices);
        int[] b = new int[n];
        Arrays.fill(b, -1);
        int zero = n + 5;
        long[][] reg = new long[2][n + n + 10];
        long ans = 0;

        for (int index : indices) {
            debug.debug("index", index);
            SequenceUtils.deepFill(reg, 0L);
            b[index] = 1;
            int ps = 0;
            reg[1][zero + ps] += 1;
            for (int i = 0; i < index; i++) {
                ps += b[i];
                reg[i & 1][zero + ps] += i + 2;
            }
            long prodSum = 0;
            for (int i = index; i < n; i++) {
                ps += b[i];
                //sum is one
                prodSum += reg[(i & 1) ^ 1][ps - 1 + zero] * (i + 1);
            }
            debug.debug("prod", prodSum);
            ans += prodSum * a[index];
        }
        out.println(ans);
    }
    Debug debug = new Debug(false);
}
