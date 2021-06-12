package on2021_05.on2021_05_25_AtCoder___AtCoder_Grand_Contest_050__Good_Bye_rng_58_Day_1_.D___Shopping;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.InverseNumber;
import template.math.ModPrimeInverseNumber;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class DShopping {
    int mod = 998244353;
    InverseNumber inv = new ModPrimeInverseNumber((int) 1e5, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        debug.debug("1 / 8", 1L * inv.inverse(8) % mod);
        debug.debug("2 / 8", 2L * inv.inverse(8) % mod);
        debug.debug("5 / 8", 5L * inv.inverse(8) % mod);

        //item, person, total, kick
        long[][][][] f = new long[k + 1][n + 1][k + 1][n + 1];
        long[] prev = new long[n + 1];
        long[] next = new long[n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                f[0][i][j][0] = 1;
            }
        }
        for (int i = 1; i <= k; i++) {
            for (int j = 0; j <= n; j++) {
                for (int a = i; a <= k; a++) {
                    long inva = inv.inverse(a);
                    SequenceUtils.deepFill(prev, 0L);
                    prev[0] = 1;
                    for (int x = 0; x < j; x++) {
                        Arrays.fill(next, 0);
                        for (int go = 0; go <= j; go++) {
                            //consider people x + 1
                            if (prev[go] == 0) {
                                continue;
                            }
                            int item = i - go;
                            long winProb = item * inva % mod;
                            if (go + 1 <= j) {
                                next[go + 1] += winProb * prev[go] % mod;
                            }
                            next[go] += (1 - winProb) * prev[go] % mod;
                        }

                        long[] tmp = prev;
                        prev = next;
                        next = tmp;
                    }
                    for (int x = 0; x <= n; x++) {
                        f[i][j][a][x] = prev[x];
                    }
                }
            }
        }

        //unknown item, person, the k-th
        long[][][] dp = new long[k + 1][n + 1][n + 1];
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= n; j++) {
                for (int t = 1; t <= j; t++) {
                    if (i == 0) {
                        dp[i][j][t] = 1;
                        continue;
                    }
                    int item = k - (n - j);
                    if (item < 0) {
                        continue;
                    }
                    int front = t - 1;
                    int back = j - t;
                    long sum = 0;
                    for (int a = 0; a <= front && a <= item; a++) {
                        for (int b = 0; b <= back && a + b <= item; b++) {
                            long winProb = (item - a) * (long) inv.inverse(i) % mod;
                            long contrib = dp[i - 1][j - a - b][t - a];
                            contrib = contrib * f[item][front][i][a] % mod;
                            contrib = contrib * f[item - a][back][i][b] % mod;
                            contrib = contrib * (1 - winProb) % mod;
                            sum += contrib;
                        }
                    }
                    dp[i][j][t] = sum % mod;
                }
            }
        }

        debug.debug("f", f);
        debug.debug("dp", dp);

        long sum = 0;
        for (int i = 0; i < n; i++) {
            long ans = 1 - dp[k][n][i + 1];
            ans = DigitUtils.mod(ans, mod);
            sum += ans;
            out.println(ans);
        }
        sum %= mod;
        assert sum == k;
    }

    Debug debug = new Debug(false);
}
