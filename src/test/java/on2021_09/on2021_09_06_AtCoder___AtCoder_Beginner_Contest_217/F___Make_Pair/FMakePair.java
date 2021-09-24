package on2021_09.on2021_09_06_AtCoder___AtCoder_Beginner_Contest_217.F___Make_Pair;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.utils.SequenceUtils;

public class FMakePair {
    boolean[][] adj;
    long[][] dp;
    int mod = 998244353;
    Combination comb = new Combination((int) 1e5, mod);

    public long dp(int l, int r) {
        if (l > r) {
            return 1;
        }
        if (dp[l][r] == -1) {
            long ans = 0;
            int total = (r - l + 1) / 2;
            for (int i = l + 1; i <= r; i++) {
                if(!adj[l][i]){
                    continue;
                }
                int cnt = (i - l + 1) / 2;
                ans += dp(i + 1, r) * dp(l + 1, i - 1) % mod * comb.combination(total, cnt) % mod;
            }
            dp[l][r] = ans % mod;
        }
        return dp[l][r];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() * 2;
        int m = in.ri();
        adj = new boolean[n][n];
        dp = new long[n][n];
        for(int i = 0; i < m; i++){
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            adj[a][b] = adj[b][a] = true;
        }
        SequenceUtils.deepFill(dp, -1L);
        long ans = dp(0, n - 1);
        out.println(ans);
    }
}
