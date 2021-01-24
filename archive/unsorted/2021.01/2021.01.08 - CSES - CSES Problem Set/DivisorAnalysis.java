package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.IntMath;
import template.math.Power;

public class DivisorAnalysis {
    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);
    int[] x;
    int[] k;
    int n;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        x = new int[n];
        k = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.ri();
            k[i] = in.ri();
        }
        out.println(divisorNum());
        out.println(divisorSum());
        out.println(divisorProd());
    }

    public long divisorNum() {
        long prod = 1;
        for (int t : k) {
            prod = prod * (t + 1) % mod;
        }
        return prod;
    }

    public long divisorSum() {
        long prod = 1;
        for (int i = 0; i < n; i++) {
            long contrib = (pow.pow(x[i], k[i] + 1) - 1L) * pow.inverse(x[i] - 1) % mod;
            prod = prod * contrib % mod;
        }
        return DigitUtils.mod(prod, mod);
    }

    public long divisorProd() {
        long prod = 1;
        int pmod = mod - 1;
        long[] pre = new long[n];
        long[] post = new long[n];
        for (int i = 0; i < n; i++) {
            pre[i] = k[i] + 1;
            if (i > 0) {
                pre[i] = pre[i] * pre[i - 1] % pmod;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            post[i] = k[i] + 1;
            if (i + 1 < n) {
                post[i] = post[i] * post[i + 1] % pmod;
            }
        }
        for (int i = 0; i < n; i++) {
            long sum = IntMath.sumOfInterval(0, k[i]) % pmod;
            if (i > 0) {
                sum = sum * pre[i - 1] % pmod;
            }
            if (i + 1 < n) {
                sum = sum * post[i + 1] % pmod;
            }
            prod = prod * pow.pow(x[i], sum) % mod;
        }
        return prod;
    }
}
