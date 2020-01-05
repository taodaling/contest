package on2020_01.on2020_01_03_Educational_Codeforces_Round_50__Rated_for_Div__2_.C__Classy_Numbers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitBase;
import template.utils.SequenceUtils;

public class CClassyNumbers {
    DigitBase base = new DigitBase(10);

    long[][][][] dp = new long[19][4][2][2];
    long l;
    long r;

    public long dp(int i, int j, int ceil, int floor) {
        if (j >= 4) {
            return 0;
        }
        if (i < 0) {
            return j >= 1 ? 1 : 0;
        }
        if (dp[i][j][ceil][floor] == -1) {
            dp[i][j][ceil][floor] = 0;
            int lVal = base.getBit(l, i);
            int rVal = base.getBit(r, i);
            for (int k = 0; k < 10; k++) {
                if (ceil == 1 && k > rVal || floor == 1 && k < lVal) {
                    continue;
                }
                dp[i][j][ceil][floor] += dp(i - 1, k == 0 ? j : j + 1, ceil == 1 && k == rVal ? 1 : 0,
                        floor == 1 && k == lVal ? 1 : 0);
            }
        }
        return dp[i][j][ceil][floor];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        l = in.readLong();
        r = in.readLong();

        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(18, 0, 1, 1);
        out.println(ans);
    }
}
