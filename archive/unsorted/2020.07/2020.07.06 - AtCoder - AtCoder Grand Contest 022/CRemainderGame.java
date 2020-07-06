package contest;

import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;

public class CRemainderGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        in.populate(a);
        in.populate(b);

        long ans = 0;
        for (int i = 50; i >= 0; i--) {
            long mask = ans | ((1L << i) - 1);
            if (!check(mask, a, b)) {
                ans = Bits.set(ans, i);
            }
        }

        if (!check(ans, a, b)) {
            out.println(-1);
            return;
        }

        out.println(ans);
    }

    public boolean check(long divisor, int[] a, int[] b) {
        long[] next = new long[51];
        for (int i = 0; i <= 50; i++) {
            next[i] = Bits.set(next[i], i);
            for (int j = 1; j <= 50; j++) {
                if (Bits.get(divisor, j) == 1) {
                    next[i] |= next[i % j];
                }
            }
        }

        for (int i = 0; i < a.length; i++) {
            if (Bits.get(next[a[i]], b[i]) == 0) {
                return false;
            }
        }
        return true;
    }
}
