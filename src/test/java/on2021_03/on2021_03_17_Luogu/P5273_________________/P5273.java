package on2021_03.on2021_03_17_Luogu.P5273_________________;



import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;

public class P5273 {
    int mod = 998244353;

    char[] s = new char[(int) 2e5];
    int slen;

    public long readModInt(long mod) {
        long ans = 0;
        for (int i = 0; i < slen; i++) {
            ans = ans * 10 + s[i] - '0';
            if (ans > 1e17) {
                ans %= mod;
            }
        }
        return ans % mod;
    }

    public long estimate() {
        long ans = 0;
        for (int i = 0; i < slen; i++) {
            ans = ans * 10 + s[i] - '0';
            if (ans > 1e17) {
                return ans;
            }
        }
        return ans;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        slen = in.rs(s);
        long k = readModInt(mod);
        long kModPhi = readModInt(mod - 1);
        long rawK = estimate();
        int[] a = in.ri(n);
        IntPoly poly = new IntPolyFFT(mod);
        int[] b = poly.modpow(a, k, kModPhi, rawK, n);
        for (int i = 0; i < n; i++) {
            int x = i >= b.length ? 0 : b[i];
            out.append(x).append(' ');
        }
    }
}
