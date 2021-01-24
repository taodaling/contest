package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class MissingCoinSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] x = new int[n];
        in.populate(x);
        Randomized.shuffle(x);
        Arrays.sort(x);
        long l = 0;
        long r = 0;
        for (int y : x) {
            if (l + y > r + 1) {
                out.println(r + 1);
                return;
            }
            r = r + y;
        }
        out.println(r + 1);
    }
}
