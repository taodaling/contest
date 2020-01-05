package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BDiagonalWalkingV2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = Math.abs(in.readLong());
        long m = Math.abs(in.readLong());
        long k = in.readLong();
        if (n > m) {
            long tmp = n;
            n = m;
            m = tmp;
        }

        if (m > k) {
            out.println(-1);
            return;
        }

        if ((m - n) % 2 == 1) {
            out.println(k - 1);
            return;
        }

        if ((k - n) % 2 == 1) {
            out.println(k - 2);
            return;
        }

        out.println(k);
    }
}
