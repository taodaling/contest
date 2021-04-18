package on2021_03.on2021_03_28_Codeforces___Codeforces_Round__228__Div__1_.D__Fox_and_Perfect_Sets;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class DFoxAndPerfectSets {
    int mod = (int) 1e9 + 7;
    long[][][] dp;

    int n;

    public long comb(int n, int x) {
        if (x == 1) {
            if (n == 0) {
                return 0;
            }
            return 1L << (n - 1);
        }
        if (n == 0) {
            return 1;
        }
        return 1L << (n - 1);
    }

    public long dp(int bit, int row, int ceil) {
        if (bit < 0) {
            return 1;
        }
        if (dp[bit][row][ceil] == -1) {
            long ans = 0;
            int val = Bits.get(n, bit);
            for (int i = 0; i <= 1; i++) {
                if (ceil == 1 && val < i) {
                    continue;
                }
                ans += dp(bit - 1, row, ceil == 1 && val == i ? 1 : 0) * comb(row, i) % mod;
            }
            //add new row
            if (!(ceil == 1 && val < 1)) {
                ans += dp(bit - 1, row + 1, ceil == 1 && val == 1 ? 1 : 0);
            }
            dp[bit][row][ceil] = ans % mod;
        }
        return dp[bit][row][ceil];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        dp = new long[31][31][2];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(30, 0, 1);
        out.println(ans);
    }
}
