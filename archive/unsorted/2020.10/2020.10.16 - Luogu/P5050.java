package contest;

import template.io.FastInput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyNTT;
import template.utils.PrimitiveBuffers;

import java.io.PrintWriter;

public class P5050 {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] p = PrimitiveBuffers.allocIntPow2(n + 1);
        for (int i = 0; i <= n; i++) {
            p[i] = in.readInt();
        }
        int[] x = new int[m];
        int[] y = new int[m];
        for (int i = 0; i < m; i++) {
            x[i] = in.readInt();
        }
        IntPoly poly = new IntPolyFFT(998244353);
        poly.multiApply(p, x, y);
        for (int i = 0; i < m; i++) {
            out.println(y[i]);
        }

        PrimitiveBuffers.release(p);
        PrimitiveBuffers.check();
    }
}
