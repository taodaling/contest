package on2021_03.on2021_03_28_AtCoder___AtCoder_Regular_Contest_116.B___Products_of_Min_Max;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow2;
import template.math.DigitUtils;
import template.rand.Randomized;
import template.utils.Debug;

import java.util.Arrays;

public class BProductsOfMinMax {
    int mod = 998244353;
    CachedPow2 cp = new CachedPow2(2, mod);

    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        Randomized.shuffle(a);
        Arrays.sort(a);
        long ans = 0;
        for (int i = 0; i < n; i++) {
            ans += a[i] * (long) a[i] % mod;
        }
        long[] A = new long[n];
        for (int i = n - 1; i >= 0; i--) {
            A[i] = (long) a[i] * cp.pow(i);
            if (i + 1 < n) {
                A[i] += A[i + 1];
            }
            A[i] %= mod;
        }
        debug.debugArray("a", a);
        debug.debug("ans", ans);
        long other = 0;
        for (int i = 0; i + 1 < n; i++) {
            long contrib = (long)a[i] * cp.inverse(i) % mod * A[i + 1] % mod;
            other += contrib;
        }
        other = other % mod * cp.inverse(1) % mod;

        debug.debug("other", other);
        ans += other;
        ans = DigitUtils.mod(ans, mod);
        out.println(ans);
    }
}
