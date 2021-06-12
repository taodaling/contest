package on2021_05.on2021_05_20_AtCoder___AtCoder_Beginner_Contest_194.F___Digits_Paradise_in_Hexadecimal;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class FDigitsParadiseInHexadecimal {
    int mod = (int) 1e9 + 7;
    int[][][][] dp;
    char[] s = new char[(int) 3e5];
    int[] mask;
    int n;
    int k;

    public int dp(int i, int distinct, int start, int ceil) {
        if (i == n) {
            return start == 1 && distinct == k ? 1 : 0;
        }
        if (dp[ceil][start][distinct][i] == -1) {
            long res = 0;
            if (ceil == 1) {
                for (int j = 0; j < 16; j++) {
                    if (j > s[i]) {
                        continue;
                    }
                    int nextStart = start == 1 || j > 0 ? 1 : 0;
                    int nextDistinct = Integer.bitCount(mask[i] | (1 << j));
                    if (nextStart == 0) {
                        nextDistinct = 0;
                    }
                    res += dp(i + 1, nextDistinct, nextStart, j == s[i] ? 1 : 0);
                }
            } else {
                if (start == 1) {
                    res += dp(i + 1, distinct, start, ceil) * (long) distinct;
                    if (distinct < 16) {
                        res += dp(i + 1, distinct + 1, 1, ceil) * (long) (16 - distinct);
                    }
                } else {
                    res += dp(i + 1, 1, 1, ceil) * 15L;
                    res += dp(i + 1, 0, 0, ceil);
                }

            }
            res %= mod;
            dp[ceil][start][distinct][i] = (int) res;
        }
        return dp[ceil][start][distinct][i];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.rs(s);
        k = in.ri();
        mask = new int[n];
        for (int i = 0; i < n; i++) {
            if (Character.isDigit(s[i])) {
                s[i] -= '0';
            } else {
                s[i] -= 'A';
                s[i] += 10;
            }
        }
        for (int i = 1; i < n; i++) {
            mask[i] = mask[i - 1] | (1 << s[i - 1]);
        }
        dp = new int[2][2][16 + 1][n];
        SequenceUtils.deepFill(dp, -1);

        long ans = dp(0, 0, 0, 1);
        out.println(ans);
    }
}
