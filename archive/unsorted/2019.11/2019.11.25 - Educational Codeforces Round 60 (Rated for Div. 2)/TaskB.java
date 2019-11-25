package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int k = in.readInt();
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        Randomized.randomizedArray(a, 0, n);
        Arrays.sort(a);
        if (n == 1) {
            out.println(a[0] * Math.min(m, k));
            return;
        }
        int time = m / (k + 1);
        long ans = time * (a[n - 1] * k + a[n - 2]) + (m % (k + 1)) * a[n - 1];
        out.println(ans);
    }
}
