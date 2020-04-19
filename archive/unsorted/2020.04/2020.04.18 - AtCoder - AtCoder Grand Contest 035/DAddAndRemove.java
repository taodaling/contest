package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongHashMap;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.TreeSet;

public class DAddAndRemove {
    int n;
    int m;
    int mask;
    long[] a;
    LongHashMap[][] dp;

    public long dp(int l, int r, int x, int y) {
        if (l + 1 >= r) {
            return 0;
        }
        long key = DigitUtils.asLong(x, y);
        long ans;
        if ((ans = dp[l][r].getOrDefault(key, -1)) == -1) {
            ans = (long) 1e18;
            for (int i = l + 1; i < r; i++) {
                ans = Math.min(ans,
                        dp(l, i, x, x + y) + dp(i, r, x + y, y) +
                                a[i] * (x + y));
            }
            dp[l][r].put(key, ans);
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }

        dp = new LongHashMap[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 2; j < n; j++) {
                dp[i][j] = new LongHashMap(1 << (n - 1), false);
            }
        }

        long ans = dp(0, n - 1, 1, 1) + a[0] + a[n - 1];

        out.println(ans);
    }
}
