package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyMTT;

public class ConvolutionMod1000000007 {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] a = new int[n];
        int[] b = new int[m];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            b[i] = in.readInt();
        }
        int mod = (int) 1e9 + 7;
        IntPoly poly = new IntPolyMTT(mod);
        int[] c = poly.convolution(a, b);
        for (int i = 0; i < n + m - 1; i++) {
            out.append(i >= c.length ? 0 : c[i]);
            out.append(' ');
        }
    }
}
