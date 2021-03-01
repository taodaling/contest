package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BMinimalCost {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long u = in.ri();
        long v = in.ri();
        int[] a = in.ri(n);
        boolean canPass = false;
        int sum = 0;
        for (int i = 1; i < n; i++) {
            int d = Math.abs(a[i] - a[i - 1]);
            if (d > 1) {
                canPass = true;
            }
            sum += d;
        }
        if (canPass) {
            out.println(0);
        } else if (sum == 0) {
            out.println(Math.min(u, v) + v);
        } else {
            out.println(Math.min(u, v));
        }
    }
}
