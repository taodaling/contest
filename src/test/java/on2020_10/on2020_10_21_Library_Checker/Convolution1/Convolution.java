package on2020_10.on2020_10_21_Library_Checker.Convolution1;



import template.io.FastInput;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyMTT;
import template.polynomial.IntPolyNTT;

import java.io.PrintWriter;

public class Convolution {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
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
        int mod = (int)1e9 + 7;
        IntPoly poly = new IntPolyMTT(mod);
        int[] c = poly.convolution(a, b);
        for (int i = 0; i < n + m - 1; i++) {
            out.print(i >= c.length ? 0 : c[i]);
            out.append(' ');
        }
    }
}
