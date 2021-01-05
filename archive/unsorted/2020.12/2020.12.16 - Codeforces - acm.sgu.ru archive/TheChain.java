package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.Random;

public class TheChain {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        Randomized.shuffle(a);
        Arrays.sort(a);
        int ans = n - 1;
        int ps = 0;
        for (int i = 0; i < n; i++) {
            ps += a[i];
            if (ps <= n - i - 2) {
                ans = Math.min(ans, n - i - 2);
            }
        }
        out.println(ans);
    }
}
