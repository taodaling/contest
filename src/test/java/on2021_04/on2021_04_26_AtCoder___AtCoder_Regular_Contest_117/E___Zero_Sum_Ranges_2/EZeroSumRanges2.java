package on2021_04.on2021_04_26_AtCoder___AtCoder_Regular_Contest_117.E___Zero_Sum_Ranges_2;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class EZeroSumRanges2 {
    public int choose2(int n) {
        return n * (n - 1) / 2;
    }

    long[][] comb = new long[100][100];

    {
        SequenceUtils.deepFill(comb, -1L);
    }

    public long comb(int i, int j) {
        if (comb[i][j] == -1) {
            if (i < j) {
                return comb[i][j] = 0;
            }
            if (j == 0) {
                return comb[i][j] = 1;
            }
            comb[i][j] = comb(i - 1, j - 1) + comb(i - 1, j);
        }
        return comb[i][j];
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri() * 2 + 1;
        int k = in.ri();
        int max = choose2(n);
        long[][][] dp = new long[n + 1][n + 1][max + 1];
        dp[0][0][0] = 1;
        for (int size = 0; size <= n; size++) {
            for (int cc = 0; cc <= n; cc++) {
                for (int pair = 0; pair <= max; pair++) {
                    if (dp[size][cc][pair] == 0) {
                        continue;
                    }
                    for (int x = cc - 1 + 2; x + size <= n; x++) {
                        int to = x - (cc - 1 + 2) + 1;
                        dp[size + x][to][pair + choose2(x)] += dp[size][cc][pair] * comb(x - 1, cc);
                    }
                }
            }
        }

        debug.debug("dp", dp);
        long ans = 0;
        for (int size = 1; size <= n; size++) {
            for (int cc = 1; cc <= n; cc++) {
                for (int pair = 0; pair <= k && pair <= max; pair++) {
                    ans += dp[size][cc][pair] * dp[n - size][cc - 1][k - pair];
                }
            }
        }

        out.println(ans);
    }
}
