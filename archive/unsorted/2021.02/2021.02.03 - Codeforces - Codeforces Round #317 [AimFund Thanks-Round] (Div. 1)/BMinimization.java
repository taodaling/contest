package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BMinimization {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        int basicLength = n / k;
        int A = n % k;
        int AL = basicLength + 1;
        int B = k - A;
        int BL = basicLength;
        long[] prev = new long[A + 1];
        long[] next = new long[A + 1];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(prev, inf);
        prev[0] = 0;
        for (int i = 0; i < k; i++) {
            Arrays.fill(next, inf);
            for (int j = 0; j <= A; j++) {
                int l = basicLength * i + j;
                if (j < A) {
                    next[j + 1] = Math.min(next[j + 1], prev[j] + a[l + AL - 1] - a[l]);
                }
                next[j] = Math.min(next[j], prev[j] + a[l + BL - 1] - a[l]);
            }
            long[] tmp = prev;
            prev = next;
            next = tmp;
        }
        long ans = prev[A];
        out.println(ans);
    }
}
