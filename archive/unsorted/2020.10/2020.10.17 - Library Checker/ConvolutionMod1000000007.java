package contest;

import template.io.FastInput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyMTT;
import template.utils.PrimitiveBuffers;

import java.io.PrintWriter;

public class ConvolutionMod1000000007 {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int mod = (int) (1e9 + 7);
        int n = in.readInt();
        int m = in.readInt();
        int[] a = PrimitiveBuffers.allocIntPow2(n);
        int[] b = PrimitiveBuffers.allocIntPow2(m);
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
        }
        IntPoly poly = new IntPolyMTT(mod);
        int[] c = poly.convolution(a, b);
        for (int i = 0; i <= n + m - 2; i++) {
            out.print(i >= c.length ? 0 : c[i]);
            out.append(' ');
        }
    }
}
