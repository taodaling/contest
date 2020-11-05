package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class BQuadruple {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        long[] way = new long[2 * n + 1];
        for (int i = 2; i <= 2 * n; i++) {
            int l = Math.max(1, i - n);
            int r = Math.min(n, i - 1);
            if (l <= r) {
                way[i] = r - l + 1;
            }
        }

        long ans = 0;
        for (int i = 2; i <= 2 * n; i++) {
            int other = i - k;
            if (other < 0 || other > 2 * n) {
                continue;
            }
            ans += way[i] * way[other];
        }
        out.println(ans);
    }

}
