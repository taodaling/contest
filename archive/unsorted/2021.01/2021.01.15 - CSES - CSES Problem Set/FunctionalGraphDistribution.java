package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Power;
import template.utils.SequenceUtils;

public class FunctionalGraphDistribution {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);
    int limit = 5000;
    int[][] comb = new int[limit + 1][limit + 1];
    int[][] stirling = new int[limit + 1][limit + 1];

    {
        SequenceUtils.deepFill(comb, -1);
        SequenceUtils.deepFill(stirling, -1);
    }

    public int comb(int n, int m) {
        if (m > n) {
            return 0;
        }
        if (comb[n][m] == -1) {
            if (m == 0) {
                return comb[n][m] = 1;
            }
            comb[n][m] = comb(n - 1, m - 1) + comb(n - 1, m);
            if (comb[n][m] >= mod) {
                comb[n][m] -= mod;
            }
        }
        return comb[n][m];
    }

    public int stirling(int n, int m) {
        if (m > n) {
            return 0;
        }
        if (stirling[n][m] == -1) {
            if (m == 0) {
                return stirling[n][m] = n == 0 ? 1 : 0;
            }
            long ans = ((long) (n - 1) * stirling(n - 1, m) + stirling(n - 1, m - 1)) % mod;
            stirling[n][m] = (int) ans;
        }
        return stirling[n][m];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        long[] pn = new long[n + 1];
        pn[0] = pow.inverse(n);
        for (int i = 1; i <= n; i++) {
            pn[i] = pn[i - 1] * n % mod;
        }
        for (int j = 1; j <= n; j++) {
            long sum = 0;
            for (int i = j; i <= n; i++) {
                long contrib = (long) comb(n, i) * stirling(i, j) % mod * pn[n - i] % mod * i % mod;
                sum += contrib;
            }
            out.println(sum);
        }
    }
}
