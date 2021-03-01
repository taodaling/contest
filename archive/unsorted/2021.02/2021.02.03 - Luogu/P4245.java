package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyFFT;
import template.polynomial.IntPolyMTT;
import template.utils.PrimitiveBuffers;

public class P4245 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int p = in.readInt();
        IntPoly poly = new IntPolyMTT(p);
        int[] a = PrimitiveBuffers.allocIntPow2(n + 1);
        int[] b = PrimitiveBuffers.allocIntPow2(m + 1);
        for (int i = 0; i <= n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i <= m; i++) {
            b[i] = in.readInt();
        }
        int[] prod = poly.convolution(a, b);
        for (int i = 0; i < n + m + 1; i++) {
            out.append(i >= prod.length ? 0 : prod[i]);
            out.append(' ');
        }
        PrimitiveBuffers.release(a, b, prod);
        PrimitiveBuffers.check();
    }
}
