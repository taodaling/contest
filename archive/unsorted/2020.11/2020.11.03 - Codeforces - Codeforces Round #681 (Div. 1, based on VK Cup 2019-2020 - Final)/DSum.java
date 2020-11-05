package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Buffer;

import java.util.Arrays;

public class DSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();
        ps = new long[n][];
        for (int i = 0; i < n; i++) {
            int t = in.readInt();
            ps[i] = new long[t + 1];
            for (int j = 1; j <= t; j++) {
                ps[i][j] = in.readInt();
                ps[i][j] += ps[i][j - 1];
            }
        }

        long[] dp = buffer.alloc();
        dp[0] = 0;

        dac(dp, 0, n - 1);
        out.println(best);
    }

    long inf = (long) 1e18;
    int k;
    Buffer<long[]> buffer = new Buffer<>(() -> {
        long[] ans = new long[k + 1];
        Arrays.fill(ans, -inf);
        return ans;
    }, x -> {
        Arrays.fill(x, -inf);
    });
    long[][] ps;
    long best = 0;

    public long[] solve(int l, int r, long[] cur) {
        long[] prev = buffer.alloc();
        System.arraycopy(cur, 0, prev, 0, k);
        for (int i = l; i <= r; i++) {
            int w = ps[i].length - 1;
            long v = ps[i][w];
            for (int j = k; j >= w; j--) {
                prev[j] = Math.max(prev[j], prev[j - w] + v);
            }
        }
        return prev;
    }

    public void dac(long[] cur, int l, int r) {
        if (l == r) {
            for (int j = 0; j < ps[l].length && j <= k; j++) {
                best = Math.max(best, ps[l][j] + cur[k - j]);
            }
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        long[] left = solve(l, m, cur);
        dac(left, m + 1, r);
        buffer.release(left);

        long[] right = solve(m + 1, r, cur);
        dac(right, l, m);
        buffer.release(right);
    }
}
