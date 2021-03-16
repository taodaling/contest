package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DKindergarten {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] a = in.rl(n);
        long A = a[0];
        long B = -a[0];
        for (int i = 0; i < n; i++) {
            long dp = Math.max(A - a[i], B + a[i]);
            if (i + 1 < n) {
                A = Math.max(A, dp + a[i + 1]);
                B = Math.max(B, dp - a[i + 1]);
            } else {
                out.println(dp);
            }
        }
    }
}
