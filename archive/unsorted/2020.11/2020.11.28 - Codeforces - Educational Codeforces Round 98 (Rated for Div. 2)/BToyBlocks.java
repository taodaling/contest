package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class BToyBlocks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        long max = Arrays.stream(a).max().orElse(-1);
        long sum = Arrays.stream(a).mapToLong(Long::valueOf).sum();
        long x = Math.max(((n - 1) - sum % (n - 1)) % (n - 1), (n - 1) * max - sum);
        out.println(x);
    }
}
