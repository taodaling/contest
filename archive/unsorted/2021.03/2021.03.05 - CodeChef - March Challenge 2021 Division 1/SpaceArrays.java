package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class SpaceArrays {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        long remain = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] > i + 1) {
                out.println("Second");
                return;
            } else {
                remain += (i + 1) - a[i];
            }
        }
        out.println(remain % 2 == 0 ? "Second" : "First");
    }
}
