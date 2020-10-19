package contest;

import template.io.FastInput;
import template.math.CachedPow;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

import java.io.PrintWriter;

public class P6800ChirpZTransform {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = 998244353;
        int n = in.readInt();
        int c = in.readInt();
        int m = in.readInt();

        IntPoly poly = new IntPolyFFT(mod);
        int[] p = PrimitiveBuffers.allocIntPow2(n);
        for (int i = 0; i < n; i++) {
            p[i] = in.readInt();
        }
        int[] ans = new int[m + 1];
        poly.chripZ(p, c, m - 1, new CachedPow(c, mod), ans);
        for (int i = 0; i < m; i++) {
            out.print(ans[i]);
            out.append(' ');
        }

    }
}
