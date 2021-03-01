package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

import java.io.PrintWriter;

public class P5245 {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long k = in.readModInt(mod);

        int[] p = PrimitiveBuffers.allocIntPow2(n);
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }
        IntPoly poly = new IntPolyFFT(mod);
        int[] ans = poly.modpow(p, k, n);
        for (int i = 0; i < n; i++) {
            out.append(i < ans.length ? ans[i] : 0);
            out.append(' ');
        }
    }
}
