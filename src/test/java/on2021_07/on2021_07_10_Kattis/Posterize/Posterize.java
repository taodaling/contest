package on2021_07.on2021_07_10_Kattis.Posterize;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class Posterize {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int N = 256;
        int d = in.ri();
        int k = in.ri();
        long[] cnt = new long[N];
        for (int i = 0; i < d; i++) {
            cnt[in.ri()] += in.rl();
        }
        long[][] f = new long[N][N];
        long inf = (long) 1e18;
        SequenceUtils.deepFill(f, inf);
        for (int t = 0; t < N; t++) {
            for (int i = 0; i < N; i++) {
                long sum = 0;
                for (int j = i; j < N; j++) {
                    long delta = t - j;
                    sum += cnt[j] * delta * delta;
                    f[i][j] = Math.min(f[i][j], sum);
                }
            }
        }

        long[][] dp = new long[N + 1][k + 1];
        SequenceUtils.deepFill(dp, inf);
        dp[0][0] = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < k; j++) {
                for (int t = i; t < N; t++) {
                    dp[t + 1][j + 1] = Math.min(dp[t + 1][j + 1], dp[i][j] + f[i][t]);
                }
            }
        }

        long ans = inf;
        for(int i = 0; i <= k; i++){
            ans = Math.min(ans, dp[N][i]);
        }
        out.println(ans);
    }
}
