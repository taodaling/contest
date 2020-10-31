package contest;

import template.io.FastInput;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.IntStream;

public class EActiveInfants {
    long[] a;
    int[] order;
    long[][] dp;

    public long dp(int l, int r) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r] == -1) {
            int cur = order[r - l];
            dp[l][r] = Math.max(dp[l][r], dp(l + 1, r) + Math.abs(cur - l) * a[cur]);
            dp[l][r] = Math.max(dp[l][r], dp(l, r - 1) + Math.abs(cur - r) * a[cur]);
        }
        return dp[l][r];
    }

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        a = new long[n];
        in.populate(a);
        order = IntStream.range(0, n).toArray();
        CompareUtils.quickSort(order, (i, j) -> Long.compare(a[i], a[j]), 0, n);
        dp = new long[n][n];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(0, n - 1);
        out.println(ans);
    }
}
