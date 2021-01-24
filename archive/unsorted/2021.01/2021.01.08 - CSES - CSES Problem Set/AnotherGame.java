package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class AnotherGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        in.populate(a);
        Randomized.shuffle(a);
        Arrays.sort(a);
        int last = 0;
        for (int i = 0; i < n; i++) {
            int x = a[i] - last;
            if (x % 2 == 1) {
                out.println("first");
                return;
            }
            last += x;
        }
        out.println("second");
    }
}
