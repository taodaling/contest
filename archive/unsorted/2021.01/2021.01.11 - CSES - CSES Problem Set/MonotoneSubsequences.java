package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.Arrays;

public class MonotoneSubsequences {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = DigitUtils.ceilDiv(n, a);
        if (a + b - 1 > n || b > a) {
            out.println("IMPOSSIBLE");
            return;
        }
        int[] distribute = new int[a];
        Arrays.fill(distribute, b);
        int remove = a * b - n;
        for (int i = a - 1; i >= 0; i--) {
            int d = Math.min(remove, distribute[i] - 1);
            remove -= d;
            distribute[i] -= d;
        }
        int last = 0;
        for (int i = 0; i < a; i++) {
            last += distribute[i];
            for (int j = 0; j < distribute[i]; j++) {
                out.append(last - j).append(' ');
            }
        }
        out.println();
    }
}
