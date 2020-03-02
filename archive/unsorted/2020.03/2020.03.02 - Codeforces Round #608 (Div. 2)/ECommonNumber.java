package contest;

import template.algo.LongBinarySearch;
import template.binary.Bits;
import template.binary.CachedLog2;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class ECommonNumber {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long m = in.readLong();

        LongBinarySearch lbs1 = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                mid = mid * 2 + 1;
                return count(n, mid) < m;
            }
        };
        LongBinarySearch lbs2 = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                mid = mid * 2;
                return count(n, mid) + count(n, mid + 1) < m;
            }
        };

        long ans1 = lbs1.binarySearch(0, (long) 1e18);
        if (ans1 == 0) {
            ans1 = -1;
        } else if (lbs1.check(ans1)) {
            ans1 = (ans1 - 1) * 2 + 1;
        } else {
            ans1 = ans1 * 2 + 1;
        }

        long ans2 = lbs2.binarySearch(1, (long) 1e18);
        if (lbs2.check(ans2)) {
            ans2 = (ans2 - 1) * 2;
        }else{
            ans2 = ans2 * 2;
        }

        long ans = Math.max(ans1, ans2);
        out.println(ans);
    }

    private long[][] dp = new long[62][2];
    private long n;

    public long dp(int i, int ceil) {
        if (i < 0) {
            return 1;
        }
        if (dp[i][ceil] == -1) {
            int bit = Bits.bitAt(n, i);
            dp[i][ceil] = 0;
            for (int j = 0; j <= 1; j++) {
                if (ceil == 1 && j > bit) {
                    continue;
                }
                dp[i][ceil] += dp(i - 1, ceil == 1 &&
                        bit == j ? 1 : 0);
            }
        }
        return dp[i][ceil];
    }

    public long count(long n, long prefix) {
        if (prefix > n) {
            return 0;
        }
        int len = CachedLog2.floorLog(prefix);
        int totalLen = CachedLog2.floorLog(n);
        long ans = 0;
        for (int i = len; i < totalLen; i++) {
            ans += 1L << (i - len);
        }
        this.n = n;
        SequenceUtils.deepFill(dp, -1L);
        if (prefix > (n >>> (totalLen - len))) {
            return ans;
        }
        int ceil = (prefix == (n >>> (totalLen - len))) ? 1 : 0;
        long plus = dp(totalLen - len - 1, ceil);
        return ans + plus;
    }
}
