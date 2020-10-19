package contest;

import template.io.FastInput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

import java.io.PrintWriter;

public class P5245 {
    int mod = 998244353;

    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();

        char[] s = new char[(int) 1e5 + 1];
        int len = in.readString(s, 0);
        long k = 0;
        for (int i = 0; i < len; i++) {
            k = k * 10 + s[i] - '0';
            k %= mod;
        }
        int[] p = PrimitiveBuffers.allocIntPow2(n);
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }
        IntPoly poly = new IntPolyNTT(mod);
        int[] ans = poly.modpowByLnExp(p, k, n);
        for (int i = 0; i < n; i++) {
            out.print(i < ans.length ? ans[i] : 0);
            out.append(' ');
        }
    }
}
