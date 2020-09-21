package on2020_09.on2020_09_21_2020_TopCoder_Open_North_America.PalindromicSubsequences;



import template.utils.SequenceUtils;

public class PalindromicSubsequences {
    char[] s;

    int mod = 10000019;
    long[][] dp;

    public long dp(int l, int r) {
        if (l > r) {
            return 1;
        }
        if (dp[l][r] == -1) {
            dp[l][r] = 0;
            for (int i = l; i < r; i++) {
                if (s[i] == s[l]) {
                    dp[l][r] += dp(l + 1, i - 1);
                }
            }
            for (int i = l + 1; i <= r; i++) {
                if (s[i] == s[r]) {
                    dp[l][r] += dp(i + 1, r - 1);
                }
            }
            if (s[l] == s[r]) {
                dp[l][r] += dp(l + 1, r - 1);
            }
            dp[l][r] += dp(l + 1, r - 1);
            dp[l][r] %= mod;
        }
        return dp[l][r];
    }

    public int count(String s) {
        this.s = s.toCharArray();
        int n = s.length();
        dp = new long[n][n];
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(0, n - 1) - 1 + mod;
        return (int)(ans % mod);
    }
}
