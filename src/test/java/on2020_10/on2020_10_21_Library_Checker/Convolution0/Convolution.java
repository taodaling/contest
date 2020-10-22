package on2020_10.on2020_10_21_Library_Checker.Convolution0;



import template.binary.Log2;
import template.io.FastInput;
import template.math.Power;
import template.polynomial.IntPoly;
import template.polynomial.IntPolyNTT;
import template.polynomial.NumberTheoryTransform;
import template.utils.PrimitiveBuffers;

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
        int mod = 998244353;
        IntPoly poly = new IntPolyNTT(mod);
        int[] c = poly.convolution(a, b);
        for (int i = 0; i < n + m - 1; i++) {
            out.print(c[i]);
            out.append(' ');
        }
    }
}
