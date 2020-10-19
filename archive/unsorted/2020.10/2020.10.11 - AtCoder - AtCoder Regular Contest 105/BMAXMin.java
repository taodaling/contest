package contest;

import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;
import java.util.Arrays;

public class BMAXMin {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        while (true) {
            int x = Arrays.stream(a).min().orElse(-1);
            int y = x;
            for (int i = 0; i < n; i++) {
                a[i] = a[i] % x + x;
                if (a[i] % x != 0) {
                    y = Math.min(a[i] % x, y);
                }
            }
            if (y == x) {
                out.println(x);
                return;
            }
            for (int i = 0; i < n; i++) {
                if (a[i] % x == y) {
                    a[i] %= x;
                    break;
                }
            }
        }
    }
}
