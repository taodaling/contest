package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

public class CFolia {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long[] a = new long[n + 1];
        in.populate(a);

        long[][] range = new long[n + 1][2];
        range[n][0] = range[n][1] = a[n];
        for (int i = n - 1; i >= 0; i--) {
            range[i][0] = DigitUtils.ceilDiv(range[i + 1][0], 2);
            range[i][1] = range[i + 1][1];
            range[i][0] += a[i];
            range[i][1] += a[i];
        }

        if (!inrange(range[0], 1)) {
            out.println(-1);
            return;
        }


        long cnt = 1;
        long sum = cnt;

        for (int i = 1; i <= n; i++) {
            long level = Math.min(cnt * 2, range[i][1]);
            sum += level;
            cnt = level - a[i];
        }

        out.println(sum);
    }

    public boolean inrange(long[] lr, long v) {
        return lr[0] <= v && v <= lr[1];
    }
}
