package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.Randomized;

import java.util.Arrays;

public class BCowAndFriend {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        Randomized.shuffle(a);
        Arrays.sort(a);
        for (int i = 0; i < n; i++) {
            if (a[i] == x) {
                out.println(1);
                return;
            }
        }
        if (a[n - 1] >= x) {
            out.println(2);
            return;
        }
        out.println(DigitUtils.ceilDiv(x, a[n - 1]));
    }
}
