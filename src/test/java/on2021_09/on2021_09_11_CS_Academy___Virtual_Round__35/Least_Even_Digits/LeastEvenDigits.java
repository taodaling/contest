package on2021_09.on2021_09_11_CS_Academy___Virtual_Round__35.Least_Even_Digits;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.LongRadix;
import template.utils.SequenceUtils;

import java.math.BigInteger;
import java.util.function.LongPredicate;

public class LeastEvenDigits {
    LongRadix radix = new LongRadix(10);
    int[][][][] dp = new int[20][2][2][2];
    long low;
    long hi;

    public int dp(int pos, int ceil, int floor, int start) {
        if (pos < 0) {
            return 0;
        }
        if (dp[pos][ceil][floor][start] == -1) {
            int best = (int) 1e9;
            int l = radix.get(low, pos);
            int r = radix.get(hi, pos);
            for (int i = 0; i < 10; i++) {
                if (i < l && floor == 1) {
                    continue;
                }
                if (i > r && ceil == 1) {
                    continue;
                }
                int nceil = ceil == 1 && i == r ? 1 : 0;
                int nfloor = floor == 1 && i == l ? 1 : 0;
                int nstart = start == 0 && i == 0 ? 0 : 1;
                best = Math.min(best, dp(pos - 1, nceil, nfloor, nstart) + (i % 2 == 0 ? 1 : 0) * nstart);
            }
            dp[pos][ceil][floor][start] = best;
        }
        return dp[pos][ceil][floor][start];
    }

    public int countEven(int x) {
        if (x == 0) {
            return 0;
        }
        return countEven(x / 10) + ((x % 10 % 2 == 0) ? 1 : 0);
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int x = in.ri();
        int count = countEven(x);
        if (count == 0) {
            out.println(-1);
            return;
        }

        long from = BinarySearch.firstTrue(new LongPredicate() {
            @Override
            public boolean test(long value) {
                low = value;
                hi = x;
                SequenceUtils.deepFill(dp, -1);
                int cur = dp(19, 1, 1, 0);
                return cur >= count;
            }
        }, 1, x);

        long end = BinarySearch.lastTrue(new LongPredicate() {
            @Override
            public boolean test(long value) {
                low = x;
                hi = value;
                SequenceUtils.deepFill(dp, -1);
                int cur = dp(19, 1, 1, 0);
                return cur >= count;
            }
        }, x, (long) 2e18);

        out.println(BigInteger.valueOf(x - from + 1).multiply(BigInteger.valueOf(end - x + 1)));
    }
}
