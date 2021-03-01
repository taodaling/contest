package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

public class P5245 {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        char[] m = new char[(int) 2e5];
        int len = in.rs(m);
        int[] p = PrimitiveBuffers.allocIntPow2(n);
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }

        int head = 0;
        while (head < len && m[head] == '0') {
            head++;
        }
        if (len - head >= 7 && p[0] == 0) {
            for (int i = 0; i < n; i++) {
                out.append(0).append(' ');
            }
            return;
        }
        long kModPhi = 0;
        long k = 0;
        for (int i = 0; i < len; i++) {
            k = k * 10 + m[i] - '0';
            k %= mod;
            kModPhi = kModPhi * 10 + m[i] - '0';
            kModPhi %= (mod - 1);
        }

        IntPoly poly = new IntPolyFFT(mod);
        int[] ans = poly.modpow(p, k, kModPhi, n);
        for (int i = 0; i < n; i++) {
            out.append(i < ans.length ? ans[i] : 0);
            out.append(' ');
        }
    }
}
