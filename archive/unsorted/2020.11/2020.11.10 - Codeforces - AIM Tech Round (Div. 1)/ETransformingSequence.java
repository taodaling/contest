package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.CachedPow;
import template.math.Combination;
import template.math.Factorial;
import template.math.Power;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.utils.PrimitiveBuffers;

import java.util.Arrays;

public class ETransformingSequence {
    int mod = (int) 1e9 + 7;
    IntPoly poly = new IntPolyFFT(mod);
    Factorial fact = new Factorial((int) 1e5, mod);
    CachedPow pow = new CachedPow(2, mod);
    Combination comb = new Combination(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        int k = in.readInt();
        if (n > k) {
            out.println(0);
            return;
        }

        int[] x = new int[k + 1];
        Arrays.fill(x, 1, k + 1, 1);
        int[] ans = pow(x, (int)n);
        long sum = 0;
        for(int i = 0; i <= k; i++){
            sum += (long)ans[i] * comb.combination(k, i) % mod;
        }
        sum %= mod;
        out.println(sum);
    }

    public int[] mul(int[] dpL, int[] dpR, int l, int r) {
        int n = dpL.length;
        int[] f = PrimitiveBuffers.allocIntPow2(n);
        int[] g = PrimitiveBuffers.allocIntPow2(n);
        for (int i = 0; i < n; i++) {
            f[i] = (int) ((long) dpL[i] * fact.invFact(i) % mod * pow.pow((long) r * i) % mod);
            g[i] = (int) ((long) dpR[i] * fact.invFact(i) % mod);
        }
        int[] ans = poly.convolution(f, g);
        int[] ret = Arrays.copyOf(ans, n);
        for(int i = 0; i < n; i++){
            ret[i] = (int) ((long)ret[i] * fact.fact(i) % mod);
        }
        PrimitiveBuffers.release(f, g, ans);
        return ret;
    }

    public int[] pow(int[] x, int n) {
        if (n == 0) {
            int[] ans = new int[x.length];
            ans[0] = 1;
            return ans;
        }
        int[] ans = pow(x, n / 2);
        ans = mul(ans, ans, n / 2, n / 2);
        if (n % 2 == 1) {
            ans = mul(ans, x, n - 1, 1);
        }
        return ans;
    }

}
