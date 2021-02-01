package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongRadix;

public class Tickets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long l = in.rl();
        long r = in.rl();
        k = in.ri();
        dp = new long[19][k + 1][k][];
        long[] ans = f(l, r, 0, 0, 18);
        out.println(ans[0]);
    }

    long[][][][] dp;
    int k;
    LongRadix radix = new LongRadix(10);


    public long[] f(long l, long r, int t, int x, int height) {
        if (l == 0 && r == 0) {
            long[] ans = new long[2];
            if (t + x >= k) {
                ans[0] = 1;
            } else {
                ans[1] = t + x;
            }
            return ans;
        }
        long valueOfBit = radix.valueOfBit(height);
        if (l == 0 && (r + 1) / 10 == valueOfBit) {
            return dp(height + 1, t, x);
        }

        long[] ans = new long[2];
        ans[1] = x;
        int begin = (int) (l / valueOfBit);
        int end = (int) (r / valueOfBit);
        for (int i = begin; i <= end; i++) {
            long v = i * valueOfBit;
            long a = v;
            long b = (i + 1) * valueOfBit - 1;
            a = Math.max(l, a);
            b = Math.min(r, b);
            long[] res = f(a - v, b - v, t + i, (int) ans[1], height - 1);
            ans[0] += res[0];
            ans[1] = res[1];
        }
        return ans;
    }


    public long[] dp(int h, int t, int x) {
        t = Math.min(t, k);
        if (dp[h][t][x] == null) {
            long[] ans = new long[2];
            if (h == 0) {
                if (t + x >= k) {
                    ans[0] = 1;
                } else {
                    ans[1] = t + x;
                }
                return dp[h][t][x] = ans;
            }
            if (t >= k) {
                ans[0] = radix.valueOfBit(h);
                return dp[h][t][x] = ans;
            }

            ans[1] = x;
            for (int top = 0; top < 10; top++) {
                long[] res = dp(h - 1, t + top, (int) ans[1]);
                ans[0] += res[0];
                ans[1] = res[1];
            }
            dp[h][t][x] = ans;
        }
        return dp[h][t][x];
    }
}
