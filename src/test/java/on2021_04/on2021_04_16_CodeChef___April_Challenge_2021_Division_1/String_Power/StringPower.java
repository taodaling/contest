package on2021_04.on2021_04_16_CodeChef___April_Challenge_2021_Division_1.String_Power;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow2;
import template.math.DigitUtils;
import template.math.Power;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class StringPower {
    int mod = 998244353;
    int R = 2000;
    int mask = (1 << 10) - 1;
    long[][] high = new long[2001][1 << 10];
    long[][] mid = new long[2001][1 << 10];
    long[][] low = new long[2001][1 << 10];

    {
        for (int i = 1; i <= R; i++) {
            low[i][0] = 1;
            for (int j = 1; j < 1 << 10; j++) {
                low[i][j] = low[i][j - 1] * i % mod;
            }
            long step = low[i][(1 << 10) - 1] * i % mod;
            mid[i][0] = 1;
            for (int j = 1; j < 1 << 10; j++) {
                mid[i][j] = mid[i][j - 1] * step % mod;
            }
            step = mid[i][(1 << 10) - 1] * i % mod;
            high[i][0] = 1;
            for (int j = 1; j < 1 << 10; j++) {
                high[i][j] = high[i][j - 1] * step % mod;
            }
        }
    }

    public long pow(int x, int n) {
        if (x < 0) {
            long ans = pow(-x, n);
            if ((n & 1) == 1) {
                ans = -ans;
            }
            return ans;
        }
        return high[x][n >> 20] * mid[x][(n >> 10) & mask] % mod * low[x][n & mask] % mod;
    }

    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        int[] p = new int[n];
        int[] b = new int[n];
        R = 0;
        for (int i = 0; i < n; i++) {
            a[i] = in.ri();
            p[i] = in.ri();
            b[i] = in.ri();
            R += p[i];
        }
        long[][] prev = new long[2][R * 2 + 1];
        long[][] next = new long[2][R * 2 + 1];
        prev[0][R] = 1;

        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debug("prev", prev);
            SequenceUtils.deepFill(next, 0L);
            int sign = b[i] == 0 ? 1 : -1;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < R * 2 + 1; k++) {
                    if (prev[j][k] == 0) {
                        continue;
                    }
                    next[j][k + p[i]] += prev[j][k];
                    next[j][k - p[i]] += prev[j][k] * sign;

                    //partial
                    if (j == 0) {
                        next[1][k + p[i]] += prev[j][k] * p[i] % mod * a[i] % mod;
                        next[1][k - p[i]] += prev[j][k] * -p[i] % mod * a[i] % mod * sign;
                    }
                }
            }
            long[][] tmp = prev;
            prev = next;
            next = tmp;
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < R * 2 + 1; k++) {
                    prev[j][k] = DigitUtils.modWithoutDivision(prev[j][k], mod);
                }
            }
        }
        debug.debug("i", n);
        debug.debug("prev", prev);

        Power pow = new Power(mod);
        int q = in.ri();
        for (int i = 0; i < q; i++) {
            int k = in.ri();
            long top = 0;
            long bot = 0;
            for (int j = 0; j < 2 * R + 1; j++) {
                top += prev[1][j] * pow(j - R, k - 1) % mod;
                bot += prev[0][j] * pow(j - R, k) % mod;
            }
            top = DigitUtils.mod(top, mod);
            bot = DigitUtils.mod(bot, mod);
            top = top * k % mod;
            long ans = top * pow.inverse((int) bot) % mod;
            out.println(ans);
        }
    }
}
