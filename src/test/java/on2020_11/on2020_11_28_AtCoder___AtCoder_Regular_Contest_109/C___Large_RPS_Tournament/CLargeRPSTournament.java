package on2020_11.on2020_11_28_AtCoder___AtCoder_Regular_Contest_109.C___Large_RPS_Tournament;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.utils.SequenceUtils;

public class CLargeRPSTournament {
    int[][] dp;
    char[] s;
    CachedPow pow;

    public int dp(int level, int left) {
        if (dp[level][left] == -1) {
            if (level == 0) {
                return dp[level][left] = s[left];
            }
            dp[level][left] = compete(dp(level - 1, left),
                    dp(level - 1, (left + pow.pow(level - 1)) % s.length));
        }
        return dp[level][left];
    }

    public int compete(int a, int b) {
        if (a == 'S' && b == 'R' ||
                a == 'R' && b == 'P' ||
                a == 'P' && b == 'S') {
            return b;
        } else {
            return a;
        }
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        s = in.rs().toCharArray();

        dp = new int[k + 1][n];
        SequenceUtils.deepFill(dp, -1);
        pow = new CachedPow(2, s.length);
        int ans = dp(k, 0);
        out.println((char) ans);
    }
}
