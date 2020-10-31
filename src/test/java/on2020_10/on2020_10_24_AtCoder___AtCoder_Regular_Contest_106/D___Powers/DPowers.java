package on2020_10.on2020_10_24_AtCoder___AtCoder_Regular_Contest_106.D___Powers;



import template.io.FastInput;
import template.math.Combination;
import template.math.DigitUtils;
import template.math.Power;

import java.awt.*;
import java.io.PrintWriter;
import java.util.Arrays;

public class DPowers {
    int mod = 998244353;
    Combination comb = new Combination((int) 5e5, mod);
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n];

        in.populate(a);
        int[][] pa = new int[k + 1][n];
        Arrays.fill(pa[0], 1);
        for (int i = 1; i <= k; i++) {
            for (int j = 0; j < n; j++) {
                pa[i][j] = (int) ((long) pa[i - 1][j] * a[j] % mod);
            }
        }
        long[] sum = new long[k + 1];
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j < n; j++) {
                sum[i] += pa[i][j];
            }
            sum[i] %= mod;
        }

        int inv2 = pow.inverse(2);
        for (int x = 1; x <= k; x++) {
            long ans = 0;
            for (int t = 0; t <= x; t++) {
                ans += (long) comb.combination(x, t) * inv2 % mod * ((sum[t] * sum[x - t] - sum[x]) % mod) % mod;
            }
            ans = DigitUtils.mod(ans, mod);
            out.println(ans);
        }
    }
}
