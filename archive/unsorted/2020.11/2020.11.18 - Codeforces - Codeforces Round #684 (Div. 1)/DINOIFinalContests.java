package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Combination;
import template.math.Factorial;
import template.math.IntMath;
import template.utils.Debug;

import java.util.Arrays;

public class DINOIFinalContests {
    int mod;
    Factorial fact;
    Combination comb;

    long[] f;
    long[] g;


    Debug debug = new Debug(true);
    public long g(int n) {
        if (n == 0) {
            return 0;
        }
        if (g[n] == -1) {
            long sum = 0;
            for (int i = 0; i < n; i++) {
                int left = i;
                int right = n - 1 - i;
                //0.. left
                //0.. right
                long contrib = (g(left) * f(right) + g(right) * f(left)) % mod * (n - 1 + 2) % mod + f(left) * f(right) % mod * ((IntMath.sumOfInterval(0, left) +
                        IntMath.sumOfInterval(0, right)) % mod) % mod;
                contrib = contrib % mod * comb.combination(n - 1, left) % mod;
                sum += contrib;
            }
            g[n] = sum % mod;
        }
        return g[n];
    }

    public long f(int n) {
        if (n == 0) {
            return 1;
        }
        if (f[n] == -1) {
            long sum = 0;
            for (int i = 0; i < n; i++) {
                int left = i;
                int right = n - 1 - i;
                sum += f(left) * comb.combination(n - 1, left) % mod * f(right) % mod * (n - 1 + 2) % mod;
            }
            f[n] = sum % mod;
        }
        return f[n];
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int p = in.readInt();
        mod = p;
        fact = new Factorial((int) 1e4, p);
        comb = new Combination(fact);

        f = new long[m + 1];
        g = new long[m + 1];
        Arrays.fill(f, -1L);
        Arrays.fill(g, -1L);

        long[][] dpWay = new long[n + 1][m + 1];
        long[][] dpSum = new long[n + 1][m + 1];
        dpWay[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= m && j <= i; j++) {
                dpWay[i][j] = dpWay[i - 1][j];
                dpSum[i][j] = dpSum[i - 1][j];
                for (int k = 1; k <= j; k++) {
                    int prev = Math.max(i - k - 1, 0);
                    dpWay[i][j] += dpWay[prev][j - k] * comb.combination(j, k) % mod * f(k) % mod;
                    long local = dpSum[prev][j - k] * f(k) +
                            dpWay[prev][j - k] * g(k);
                    local = local % mod * comb.combination(j, k) % mod;
                    dpSum[i][j] += local;
                }
                dpWay[i][j] %= mod;
                dpSum[i][j] %= mod;
            }
        }

        long ans = dpSum[n][m];
        debug.debug("f", f);
        debug.debug("g", g);
        debug.debug("dpWay", dpWay);
        debug.debug("dpSum", dpSum);
        out.println(ans);
    }
}
