package contest;

import template.FastInput;
import template.FastOutput;
import template.Randomized;
import template.SequenceUtils;

import java.util.Arrays;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readLong();
        int[] a = new int[n];
        int[] f = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }
        for(int i = 0; i < n; i++){
            f[i] = in.readInt();
        }
        Randomized.randomizedArray(a, 0, n);
        Randomized.randomizedArray(f, 0, n);
        Arrays.sort(a);
        Arrays.sort(f);
        SequenceUtils.reverse(f, 0, n);

        long l = 0;
        long r = (long) 1e12;

        while (l < r) {
            long m = (l + r) / 2;
            long c = minCost(a, f, m);
            if (c <= k) {
                r = m;
            } else {
                l = m + 1;
            }
        }

        out.println(l);
    }

    public long minCost(int[] a, int[] f, long limit) {
        int n = a.length;
        long cost = 0;
        for (int i = 0; i < n; i++) {
            long aAtMost = limit / f[i];
            if (aAtMost < a[i]) {
                cost += a[i] - aAtMost;
            }
        }
        return cost;
    }
}
