package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.MinimumFraction;

public class NewElementsPart2 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        out.printf("Case #%d: ", testNumber);

        int n = in.readInt();
        long[] max = new long[]{1, (long) 2e9 + 1};
        long[] min = new long[]{(long) 2e9 + 1, 1};
        long lastC = in.readInt();
        long lastJ = in.readInt();

        boolean valid = true;
        for (int i = 1; i < n; i++) {
            long c = in.readInt();
            long j = in.readInt();
            if (c <= lastC && j <= lastJ) {
                valid = false;
            } else if (c >= lastC && j >= lastJ) {
            } else if (c > lastC && j < lastJ) {
                max = min(max, new long[]{lastJ - j, c - lastC});
            } else {
                min = max(min, new long[]{j - lastJ, lastC - c});
            }
            lastC = c;
            lastJ = j;
        }

        valid = valid && compare(min, max) < 0;
        if(!valid){
            out.println("IMPOSSIBLE");
            return;
        }

        long[] ans = MinimumFraction.findMinimumFraction(min[0], min[1], max[0], max[1], false, false);
        out.append(ans[0]).append(' ').append(ans[1]).println();
    }

    public int compare(long[] a, long[] b) {
        return Long.compare(a[1] * b[0], b[1] * a[0]);
    }

    public long[] min(long[] a, long[] b) {
        return compare(a, b) <= 0 ? a : b;
    }

    public long[] max(long[] a, long[] b) {
        return compare(a, b) >= 0 ? a : b;
    }
}
