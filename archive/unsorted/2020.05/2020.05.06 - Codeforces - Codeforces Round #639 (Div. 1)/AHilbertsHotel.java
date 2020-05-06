package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class AHilbertsHotel {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        boolean[] exists = new boolean[n];
        for (int i = 0; i < n; i++) {
            exists[DigitUtils.mod(i + a[i], n)] = true;
        }

        boolean allFill = true;
        for (boolean e : exists) {
            if (!e) {
                allFill = false;
            }
        }

        if (allFill) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }
}
