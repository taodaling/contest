package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class MaximiseFunction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        out.println((a[n - 1] - a[0]) * 2);
    }
}
